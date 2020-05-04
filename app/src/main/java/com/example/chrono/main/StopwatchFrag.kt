package com.example.chrono.main

import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.chrono.R
import com.example.chrono.databinding.FragmentStopwatchBinding

class StopwatchFrag : Fragment() {
    var bind: FragmentStopwatchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_stopwatch, container, false)
        val chronometer = bind!!.chronometer
        var offset = 0

        val SSbutton = bind!!.startstopbutton
        SSbutton?.setOnClickListener(object : View.OnClickListener {
            internal var isPlaying = false


            override fun onClick(v: View?) {
                if(!isPlaying) {
                    chronometer.base = SystemClock.elapsedRealtime()-offset
                    chronometer.start()
                    isPlaying = true

                } else {
                    chronometer.stop()
                    offset = (SystemClock.elapsedRealtime() - chronometer.base).toInt()
                    isPlaying = false
                }
                SSbutton.setText(if(isPlaying) R.string.stop else R.string.start)
            }
        })

        val Rbutton = bind!!.resetbutton
        Rbutton?.setOnClickListener(object: View.OnClickListener{

            override fun onClick(v: View?) {
                chronometer.base = SystemClock.elapsedRealtime()
                offset = 0
            }
        })

        return bind!!.root
    }
}