package com.example.chrono.main.stopwatch

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chrono.R
import com.example.chrono.databinding.FragmentStopwatchBinding
import com.example.chrono.util.adapters.LapViewAdapter
import com.example.chrono.util.components.Chronometer
import com.example.chrono.util.objects.LapObject
import kotlinx.android.synthetic.main.fragment_stopwatch.*

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

    private lateinit var  broadcastReceiver: BroadcastReceiver

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
            stop()
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

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {
                when (intent?.action) {
                    STOP -> stop()
                }
            }
        }


        requireContext().registerReceiver(broadcastReceiver, IntentFilter(STOP))
//        val filter = IntentFilter(STOP)
//        requireContext().registerReceiver(Receiver(),filter)

        createNotificationChannel()
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

    fun stop(){
        swatch.stop()
        offset = (SystemClock.elapsedRealtime() - chronometer!!.base).toInt()
        swatchState = SwatchState.STOPPED
        updateButtonUI()
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

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.stopwatch_notification_channel_id)
            val descriptionText = "Stopwatch notification"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(name, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object{
        const val STOP = "stop"
        val RESMUE = "resume"
        val RESET = "reset"
    }

}
