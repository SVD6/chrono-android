package ca.chronofit.chrono.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.FragmentStopwatchBinding
import ca.chronofit.chrono.util.adapters.LapViewAdapter
import ca.chronofit.chrono.util.components.Chronometer
import ca.chronofit.chrono.util.objects.LapObject
import ca.chronofit.chrono.util.objects.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_stopwatch.*
import java.text.DecimalFormat

class StopwatchFrag : Fragment() {
    private lateinit var bind: FragmentStopwatchBinding
    private lateinit var recyclerView: RecyclerView

    private val settingsViewModel: SettingsViewModel by activityViewModels()

    enum class SwatchState { INIT, RUNNING, STOPPED }

    private lateinit var swatch: Chronometer
    private var swatchState: SwatchState = SwatchState.INIT
    private var offset: Int = 0

    private var lapCount = 0
    private var prevTime = 0.toLong()
    private var maxLapCount = 99
    private lateinit var laps: ArrayList<LapObject>

    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_stopwatch, container, false)

        // Initialize Everything
        swatch = bind.chronometer
        initialize()

        // Button Logic
        bind.startButton.setOnClickListener {
            startStopwatch()
        }

        bind.stopButton.setOnClickListener {
            stopStopwatch()
        }

        bind.resumeButton.setOnClickListener {
            resumeStopwatch()
        }

        bind.resetButton.setOnClickListener {
            resetStopwatch()
        }

        bind.lapButton.setOnClickListener {
            if (lapCount >= (maxLapCount - 1)) {
                Toast.makeText(
                    requireContext(),
                    "What in marathon...max laps reached! \uD83E\uDD75",
                    Toast.LENGTH_LONG
                ).show()
                bind.lapButton.isEnabled = false
            }
            lap()
        }

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {
                when (intent?.action) {
                    STOP -> stopStopwatch()
                    RESET -> resetStopwatch()
                    RESUME -> resumeStopwatch()
                }
            }
        }

        requireContext().registerReceiver(broadcastReceiver, IntentFilter(STOP))
        requireContext().registerReceiver(broadcastReceiver, IntentFilter(RESET))
        requireContext().registerReceiver(broadcastReceiver, IntentFilter(RESUME))

        return bind.root
    }

    private fun initialize() {
        recyclerView = bind.recyclerView
        val manager = LinearLayoutManager(requireContext())
        manager.stackFromEnd = true
        manager.scrollToPosition(lapCount)
        recyclerView.layoutManager = manager

        offset = 0
        lapCount = 0
        prevTime = 0.toLong()

        bind.lapButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        laps = ArrayList()
        recyclerView.adapter = LapViewAdapter(laps)
    }

    private fun startStopwatch() {
        swatch.base = SystemClock.elapsedRealtime()

        if (settingsViewModel.notifications.value == null) swatch.setNotificationEnabled(true)
        else settingsViewModel.notifications.value?.let { swatch.setNotificationEnabled(it) }
        swatch.start()
        swatchState = SwatchState.RUNNING
        updateButtonUI()
    }

    private fun stopStopwatch() {
        swatch.stop()
        offset = (SystemClock.elapsedRealtime() - chronometer!!.base).toInt()
        swatchState = SwatchState.STOPPED
        updateButtonUI()
    }

    fun resetStopwatch() {
        swatch.stop()
        swatch.base = SystemClock.elapsedRealtime()
        offset = 0
        swatchState = SwatchState.INIT
        initialize()
        updateButtonUI()
    }

    fun resumeStopwatch() {
        swatch.base = SystemClock.elapsedRealtime() - offset
        swatch.start()
        swatchState = SwatchState.RUNNING
        updateButtonUI()
    }

    private fun lap() {
        lapCount += 1

        val currTime = SystemClock.elapsedRealtime() - chronometer!!.base
        val timeDiff = currTime - prevTime

        // Create Lap Object
        val lap = LapObject()
        val dec = DecimalFormat("#00")

        lap.lapNum = dec.format(lapCount)
        lap.lapTime = timeDiff
        lap.totalTime = currTime

        laps.add(lap)
        recyclerView.adapter?.notifyItemInserted(lapCount)
        recyclerView.scrollToPosition(recyclerView.adapter!!.itemCount - 1)
        prevTime = currTime
    }

    // Update the buttons layout based on the current state of the timer
    private fun updateButtonUI() {
        when (swatchState) {
            SwatchState.INIT -> {
                bind.lapButton.isEnabled = true
                bind.initButtonLay.visibility = View.VISIBLE
                bind.runButtonLay.visibility = View.GONE
                bind.stopButtonLay.visibility = View.GONE
            }
            SwatchState.RUNNING -> {
                bind.initButtonLay.visibility = View.GONE
                bind.runButtonLay.visibility = View.VISIBLE
                bind.stopButtonLay.visibility = View.GONE
            }
            SwatchState.STOPPED -> {
                bind.initButtonLay.visibility = View.GONE
                bind.runButtonLay.visibility = View.GONE
                bind.stopButtonLay.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val STOP = "stop"
        const val RESUME = "resume"
        const val RESET = "reset"
    }
}
