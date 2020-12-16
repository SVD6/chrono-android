package com.example.chrono.main.stopwatch

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.chrono.R
import com.example.chrono.databinding.FragmentStopwatchBinding
import com.example.chrono.util.components.MyChronometer
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_stopwatch.*
import kotlinx.android.synthetic.main.lap_row.view.*
import java.text.DecimalFormat

class StopwatchFrag : Fragment() {
    var bind: FragmentStopwatchBinding? = null
    var isPlaying: Boolean = false
    var init_stopwatch = false // Clock has not been initialized

    var chronometer: MyChronometer? = null
    var singlestartstopbutton: MaterialButton? = null
    var multissbutton: MaterialButton? = null
    var resetlapbutton: MaterialButton? = null
    var offset: Int = 0

    var single_button_layout: LinearLayout? = null
    var multi_button_layout: LinearLayout? = null

    var lap_header_active = false
    var header_view: View? = null
    var lap_count = 0
    var lastLap = 0.toLong()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_stopwatch, container, false)
        header_view = LayoutInflater.from(requireContext()).inflate(R.layout.lap_header, null)

        chronometer = bind!!.chronometer

        singlestartstopbutton = bind!!.singlestartstopbutton
        multissbutton = bind!!.multissbutton
        resetlapbutton = bind!!.resetlapbutton

        single_button_layout = bind!!.singlebutton
        multi_button_layout = bind!!.multibuttons

        singlestartstopbutton?.setOnClickListener {
            if (!isPlaying) {
                playingUI()
            } else {
                pausedUI()
            }
        }
        multissbutton?.setOnClickListener {
            if (!isPlaying) {
                playingUI()
            } else {
                pausedUI()
            }
        }

        bind!!.resetlapbutton.setOnClickListener {
            if(!isPlaying){
                //The stopwatch is not running, therefore user clicked the reset button
                reset()
            } else {
                //The stopwatch is running, therefore the button is a lap button
                lap()
            }
        }

        chronometer!!.setOnClickListener {
            if (!isPlaying) {
                playingUI()
            } else {
                pausedUI()
                isPlaying = false
            }
        }

        chronometer!!.setOnLongClickListener {
            reset()
            return@setOnLongClickListener true
        }

        return bind!!.root
    }

    private fun playingUI() {
        if (!init_stopwatch) { //The stopwatch has not been initialized and this is the first instance of it starting.
                single_button_layout!!.visibility = View.GONE
                multi_button_layout!!.visibility = View.VISIBLE

            init_stopwatch =  true//Init is done
        }

        chronometer!!.base = SystemClock.elapsedRealtime() - offset
        chronometer!!.start()
        isPlaying = true

        singlestartstopbutton?.setText(R.string.stop)
        singlestartstopbutton?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(), R.color.stop_red
            )
        )

        multissbutton?.setText(R.string.stop)
        multissbutton?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(), R.color.stop_red
            )
        )

        resetlapbutton?.text = "Lap"
        resetlapbutton?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.lap_blue)
        )
    }

    private fun pausedUI() {
        chronometer!!.stop()
        offset = (SystemClock.elapsedRealtime() - chronometer!!.base).toInt()
        isPlaying = false

        singlestartstopbutton?.setText(R.string.resume)
        singlestartstopbutton?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.resume_green)
        )

        multissbutton?.setText(R.string.resume)
        multissbutton?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.resume_green)
        )

        resetlapbutton?.text = "Reset"
        resetlapbutton?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.reset_grey)
        )
    }

    private fun reset() {
        chronometer!!.stop()
        chronometer!!.base = SystemClock.elapsedRealtime()
        offset = 0
        isPlaying = false

        if (init_stopwatch){
            single_button_layout!!.visibility = View.VISIBLE
            multi_button_layout!!.visibility = View.GONE

            init_stopwatch = false
        }

        if (lap_header_active){
            container.removeView(header_view)
            lap_header_active = false
            lap_count = 0
            lastLap = 0.toLong()
            container.removeAllViews()

        }

        singlestartstopbutton?.setText(R.string.start)
        singlestartstopbutton?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.resume_green)
        )

        multissbutton?.setText(R.string.start)
        multissbutton?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.resume_green)
        )
    }

    private fun lap() {
        if(!lap_header_active){
            //we need to add in the lap table header
            container.addView(header_view)
            lap_header_active = true
        }

        //track lap numbers
        lap_count += 1
        var lap_view = LayoutInflater.from(requireContext()).inflate(R.layout.lap_row, null)

        //get lap numbers
        lap_view.lapNum.text = lap_count.toString()

        var timeNow = SystemClock.elapsedRealtime() - chronometer!!.base

        // get overall time that the current lap finished at.
        var overall_time = getTime(timeNow)

        // get lap time for current lap
        var lapTimeDiff = timeNow - lastLap
        lastLap = lapTimeDiff
        var lapTime= getTime(lapTimeDiff)

        //set text views
        lap_view.lapTimes.text = lapTime
        lap_view.overallTime.text = overall_time
        container.addView(lap_view)
    }

    private fun getTime(timeElapsed: Long) : String{

        val df = DecimalFormat("00")

        val hours = (timeElapsed / (3600 * 1000))
        var remaining = (timeElapsed % (3600 * 1000))

        val minutes = remaining / (60 * 1000)
        remaining = remaining % (60 * 1000)

        val seconds = remaining / 1000
        remaining = remaining % 1000

        val milliseconds = timeElapsed % 1000 / 100
        remaining = remaining % 100

        val tenthmillisecond = remaining % 10

        var text = ""

        if (hours > 0) {
            text += df.format(hours.toLong()) + ":"
        }

        text += df.format(minutes.toLong()) + ":"
        text += df.format(seconds.toLong()) + ":"
        text += milliseconds.toString() + tenthmillisecond.toString()

        return text
    }
}