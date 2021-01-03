package com.example.chrono.main.stopwatch

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.chrono.R
import com.example.chrono.databinding.FragmentStopwatchBinding
import com.example.chrono.util.components.Chronometer
import kotlinx.android.synthetic.main.fragment_stopwatch.*
import kotlinx.android.synthetic.main.lap_row.view.*
import java.text.DecimalFormat

class StopwatchFrag : Fragment() {
    private var bind: FragmentStopwatchBinding? = null

    enum class SwatchState { INIT, RUNNING, STOPPED }

    private lateinit var swatch: Chronometer
    private var swatchState: SwatchState = SwatchState.INIT
    private var offset: Int = 0

    private var lapCount = 0

    private var lapHeaderActive = false
    private var headerView: View? = null
    private var lastLap = 0.toLong()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_stopwatch, container, false)
        headerView = LayoutInflater.from(requireContext()).inflate(R.layout.lap_header, null)
        swatch = bind!!.chronometer

        swatch.base = SystemClock.elapsedRealtime() - offset

        bind!!.startButton.setOnClickListener {
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

        bind!!.resetbutton.setOnClickListener {
            swatch.stop()
            swatch.base = SystemClock.elapsedRealtime()
            offset = 0
            swatchState = SwatchState.INIT
            updateButtonUI()
        }

        bind!!.lapButton.setOnClickListener {
            lap()
        }

        return bind!!.root
    }

    private fun lap() {
        if (!lapHeaderActive) {
            //we need to add in the lap table header
            container.addView(headerView)
            lapHeaderActive = true
        }

        //track lap numbers
        lapCount += 1
        val lapView = LayoutInflater.from(requireContext()).inflate(R.layout.lap_row, null)
        val timeNow = SystemClock.elapsedRealtime() - chronometer!!.base

        // get overall time that the current lap finished at.
        val overallTime = getTime(timeNow)

        // get lap time for current lap
        val lapTimeDiff = timeNow - lastLap
        lastLap = lapTimeDiff
        val lapTime = getTime(lapTimeDiff)

        //set text views
        lapView.lap_num.text = lapCount.toString()
        lapView.lap_times.text = lapTime
        lapView.overall_time.text = overallTime
        bind!!.container.addView(lapView)
    }

    private fun getTime(timeElapsed: Long): String {

        val df = DecimalFormat("00")

        val hours = (timeElapsed / (3600 * 1000))
        var remaining = (timeElapsed % (3600 * 1000))

        val minutes = remaining / (60 * 1000)
        remaining %= (60 * 1000)

        val seconds = remaining / 1000
        remaining %= 1000

        val milliseconds = timeElapsed % 1000 / 100
        remaining %= 100

        val tenthMillisecond = remaining % 10

        var text = ""

        if (hours > 0) {
            text += df.format(hours) + ":"
        }

        text += df.format(minutes) + ":"
        text += df.format(seconds) + ":"
        text += milliseconds.toString() + tenthMillisecond.toString()

        return text
    }

    // Update the buttons layout based on the current state of the timer
    private fun updateButtonUI() {
        when (swatchState) {
            SwatchState.INIT -> {
                bind!!.initButtonLay.visibility = View.VISIBLE
                bind!!.runButtonLay.visibility = View.GONE
                bind!!.stopButtonLay.visibility = View.GONE
//                bind!!.countdown.text = "0"
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
}


//    private fun reset() {
//        chronometer!!.stop()
//        chronometer!!.base = SystemClock.elapsedRealtime()
//        offset = 0
//        isPlaying = false
//
//        if (init_stopwatch){
//            single_button_layout!!.visibility = View.VISIBLE
//            multi_button_layout!!.visibility = View.GONE
//
//            init_stopwatch = false
//        }
//
//        if (lap_header_active){
//            container.removeView(header_view)
//            lap_header_active = false
//            lap_count = 0
//            lastLap = 0.toLong()
//            container.removeAllViews()
//
//        }
//
//        singlestartstopbutton?.setText(R.string.start)
//        singlestartstopbutton?.setBackgroundColor(
//            ContextCompat.getColor(requireContext(), R.color.resume_green)
//        )
//
//        multissbutton?.setText(R.string.start)
//        multissbutton?.setBackgroundColor(
//            ContextCompat.getColor(requireContext(), R.color.resume_green)
//        )
//    }