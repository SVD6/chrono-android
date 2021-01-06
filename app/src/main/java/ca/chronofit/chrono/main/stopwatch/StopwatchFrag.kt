package ca.chronofit.chrono.main.stopwatch

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.FragmentStopwatchBinding
import ca.chronofit.chrono.util.adapters.LapViewAdapter
import ca.chronofit.chrono.util.components.Chronometer
import ca.chronofit.chrono.util.objects.LapObject
import kotlinx.android.synthetic.main.fragment_stopwatch.*
import java.lang.RuntimeException

class StopwatchFrag : Fragment() {
    private var bind: FragmentStopwatchBinding? = null
    private lateinit var recyclerView: RecyclerView

    enum class SwatchState { INIT, RUNNING, STOPPED }

    private lateinit var swatch: Chronometer
    private var swatchState: SwatchState = SwatchState.INIT
    private var offset: Int = 0

    private var lapCount = 0
    private var prevTime = 0.toLong()
    private lateinit var laps: ArrayList<LapObject>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_stopwatch, container, false)

        // Initialize Everything
        swatch = bind!!.chronometer
        initialize()

        // Button Logic
        bind!!.startButton.setOnClickListener {
            swatch.base = SystemClock.elapsedRealtime()
            swatch.start()
            swatchState = SwatchState.RUNNING
            updateButtonUI()
        }

        bind!!.stopButton.setOnClickListener {
            swatch.stop()
            offset = (SystemClock.elapsedRealtime() - chronometer!!.base).toInt()
            swatchState = SwatchState.STOPPED
            updateButtonUI()
        }

        bind!!.resumeButton.setOnClickListener {
            swatch.base = SystemClock.elapsedRealtime() - offset
            swatch.start()
            swatchState = SwatchState.RUNNING
            updateButtonUI()
        }

        bind!!.resetButton.setOnClickListener {
            swatch.stop()
            swatch.base = SystemClock.elapsedRealtime()
            offset = 0
            swatchState = SwatchState.INIT
            reset()
            updateButtonUI()
        }

        bind!!.lapButton.setOnClickListener {
            lap()
        }
        return bind!!.root
    }

    private fun initialize() {
        recyclerView = bind!!.recyclerView
        val manager = LinearLayoutManager(requireContext())
        manager.stackFromEnd = true
        manager.scrollToPosition(lapCount)
        recyclerView.layoutManager = manager

        offset = 0
        lapCount = 0
        prevTime = 0.toLong()

        laps = ArrayList()
        recyclerView.adapter = LapViewAdapter(laps)
    }

    private fun lap() {
        lapCount += 1
        val currTime = SystemClock.elapsedRealtime() - chronometer!!.base
        val timeDiff = currTime - prevTime

        // Create Lap Object
        val lap = LapObject()
        lap.lapNum = lapCount
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
                bind!!.initButtonLay.visibility = View.VISIBLE
                bind!!.runButtonLay.visibility = View.GONE
                bind!!.stopButtonLay.visibility = View.GONE
            }
            SwatchState.RUNNING -> {
                bind!!.initButtonLay.visibility = View.GONE
                bind!!.runButtonLay.visibility = View.VISIBLE
                bind!!.stopButtonLay.visibility = View.GONE
            }
            SwatchState.STOPPED -> {
                bind!!.initButtonLay.visibility = View.GONE
                bind!!.runButtonLay.visibility = View.GONE
                bind!!.stopButtonLay.visibility = View.VISIBLE
            }
        }
    }

    private fun reset() {
        initialize()
    }
}
