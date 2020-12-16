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

    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped

    private var secondsRemaining: Long = 0

    private var progressCountdown: MyProgressBar? = null
    private var countdown: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false)

        progressCountdown = bind!!.progressbar
        countdown = bind!!.countdown

        timerLay = bind!!.timerList


        bind!!.startstopbutton.setOnClickListener {
            when (timerState) {
                TimerState.Stopped -> {
                    initTimer()
                }
            }
        }

        bind!!.createTimer.setOnClickListener {
            startActivity(Intent(requireContext(), TimerCreate::class.java))
        }

//        initTimer()

//        bind!!.timerlayout.setOnClickListener {
//            when (timerState) {
//                TimerState.Paused -> {
//                    startTimer()
//                    timerState = TimerState.Running
//                }
//                TimerState.Running -> {
//                    timer.cancel()
//                    timerState = TimerState.Paused
//                }
//            }
//        }

//        bind!!.startstopbutton.setOnClickListener {
//            when (timerState) {
//                TimerState.Paused -> {
//                    timerState = TimerState.Running
//                    initTimer()
//                }
//                TimerState.Initial -> {
//                    timerState = TimerState.Running
//                    initTimer()
//                }
//                TimerState.Running -> {
//                    timer.cancel()
//                    timerState = TimerState.Paused
//                    pausedUI()
//                }
//            }
//        }

        return bind!!.root
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerLength(requireContext())
        timerLengthSeconds = (lengthInMinutes * 60L)
        progressCountdown!!.setmMaxProgress(timerLengthSeconds.toInt())
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(requireContext())
        progressCountdown!!.setmMaxProgress(timerLengthSeconds.toInt())
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()

        countdown!!.text =
            "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
//        progressCountdown!!.progress = (timerLengthSeconds - secondsRemaining).toInt()
        progressCountdown!!.setProgress((timerLengthSeconds - secondsRemaining).toInt())
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(requireContext())

        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(requireContext())
        else
            timerLengthSeconds

        if (timerState == TimerState.Running)
            startTimer()

        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TimerState.Running
//        playingUI()

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    //    override fun onResume() {
//        super.onResume()
//
//        initTimer()
//        playingUI()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        pausedUI()
//
//        if (timerState == TimerState.Running) {
//            timer.cancel()
//        } else if (timerState == TimerState.Paused) {
//
//        }
//        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, requireContext())
//        PrefUtil.setSecondsRemaining(secondsRemaining, requireContext())
//        PrefUtil.setTimerState(timerState, requireContext())
//    }
//

    //
    private fun onTimerFinished() {
        timerState = TimerState.Stopped
//        initialUI()
        setNewTimerLength()

        progressCountdown!!.setProgress(0)

        PrefUtil.setSecondsRemaining(timerLengthSeconds, requireContext())
        secondsRemaining = timerLengthSeconds

        updateCountdownUI()
    }


//

//

//
//    private fun playingUI() {
//        startstopbutton?.setText(R.string.pause)
//        startstopbutton?.setBackgroundColor(
//            ContextCompat.getColor(
//                requireContext(), R.color.stop_red
//            )
//        )
//    }
//
//    private fun initialUI() {
//        startstopbutton?.setText(R.string.start)
//        startstopbutton?.setBackgroundColor(
//            ContextCompat.getColor(
//                requireContext(), R.color.resume_green
//            )
//        )
//    }
//
//    private fun pausedUI() {
//        startstopbutton?.setText(R.string.resume)
//        startstopbutton?.setBackgroundColor(
//            ContextCompat.getColor(requireContext(), R.color.resume_green)
//        )
//    }
//
//    private fun reset() {
//        startstopbutton?.setText(R.string.start)
//        startstopbutton?.setBackgroundColor(
//            ContextCompat.getColor(requireContext(), R.color.resume_green)
//        )
//    }

}