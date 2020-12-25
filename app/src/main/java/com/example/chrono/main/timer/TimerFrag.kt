package com.example.chrono.main.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.chrono.R
import com.example.chrono.databinding.FragmentTimerBinding
import kotlin.math.roundToInt


class TimerFrag : Fragment() {
    var bind: FragmentTimerBinding? = null

    enum class TimerState { RUNNING, STOPPED, PAUSED }

    enum class RunningState { INITIAL, WORK, REST }

    private lateinit var countdown: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false)

//        bind!!.createTimer.setOnClickListener {
//            startActivity(Intent(requireContext(), TimerCreate::class.java))
//        }

        countdown = object : CountDownTimer(20000, 1000) {
            var secondsLeft = 0.toFloat()

            override fun onTick(p0: Long) {
                if ((p0.toFloat().roundToInt() / (1000).toFloat()) != secondsLeft) {
                    secondsLeft = (p0.toFloat() / (1000).toFloat()).roundToInt().toFloat()
                    bind!!.countdown.text = (secondsLeft).toInt().toString()
                }
            }

            override fun onFinish() {
                Toast.makeText(context, "Timer completed.", Toast.LENGTH_LONG).show()
            }
        }

        bind!!.startstopbutton.setOnClickListener {
            countdown.start()
        }

        return bind!!.root
    }

    // Update UI for every tick
    fun updateUI() {

    }

    // Create a countdown timer based on parameters
    fun createTimer() {

    }
}