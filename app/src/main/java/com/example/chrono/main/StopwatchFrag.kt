package com.example.chrono.main

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.chrono.R
import com.example.chrono.databinding.FragmentStopwatchBinding
import com.google.android.material.button.MaterialButton

class StopwatchFrag : Fragment() {
    var bind: FragmentStopwatchBinding? = null
    var isPlaying: Boolean = false

    var chronometer: Chronometer? = null
    var startstopbutton: MaterialButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_stopwatch, container, false)

        chronometer = bind!!.chronometer
        isPlaying = false
        var offset = 0

        startstopbutton = bind!!.startstopbutton

        startstopbutton?.setOnClickListener {
            if (!isPlaying) {
                chronometer!!.base = SystemClock.elapsedRealtime() - offset
                chronometer!!.start()
                playingUI()
                isPlaying = true

            } else {
                chronometer!!.stop()
                offset = (SystemClock.elapsedRealtime() - chronometer!!.base).toInt()
                pausedUI()
                isPlaying = false
            }
        }

        bind!!.resetbutton.setOnClickListener {
            chronometer!!.base = SystemClock.elapsedRealtime()
            offset = 0
            resetUI()
        }

        return bind!!.root
    }

    private fun playingUI() {
        startstopbutton?.setText(R.string.stop)
        startstopbutton?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(), R.color.stop_red
            )
        )
    }

    private fun pausedUI() {
        startstopbutton?.setText(R.string.resume)
        startstopbutton?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.resume_green)
        )
    }

    private fun resetUI() {
        startstopbutton?.setText(R.string.start)
        startstopbutton?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.resume_green)
        )
    }
}