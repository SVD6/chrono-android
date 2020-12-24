package com.example.chrono.main.timer

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.chrono.R
import com.example.chrono.databinding.FragmentTimerBinding
import com.example.chrono.util.PrefUtil
import com.example.chrono.util.components.MyProgressBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import java.lang.RuntimeException

class TimerFrag : Fragment() {
    var bind: FragmentTimerBinding? = null

    enum class TimerState {
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private lateinit var timerLay: LinearLayout

    private var timerMilliSeconds: Long = 0
    private var timerState = TimerState.Stopped


    private var countdown: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false)

        countdown = bind!!.countdown

        timerLay = bind!!.timerList


        bind!!.startstopbutton.setOnClickListener {
            when (timerState) {
                TimerState.Stopped -> {
                    startTimer(6000)
                }
                TimerState.Running -> {
                    // Test
                }
            }
        }

        bind!!.createTimer.setOnClickListener {
            startActivity(Intent(requireContext(), TimerCreate::class.java))
        }

        return bind!!.root
    }

    private fun startTimer(time_seconds: Long) {
        timer = object : CountDownTimer(time_seconds, 1000) {
            override fun onFinish() {
                TODO("Not yet implemented")
            }

            override fun onTick(p0: Long) {
                timerMilliSeconds = p0
            }
        }
    }

    private fun updateTimerUI() {
        val minute = (timerMilliSeconds / 1000) / 60
        val seconds = (timerMilliSeconds / 1000) % 60

        countdown!!.text = "$ "
    }

}