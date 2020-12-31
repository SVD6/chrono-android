package com.example.chrono.main.circuit

import android.app.Activity
import android.media.AsyncPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.chrono.R
import com.example.chrono.databinding.ActivityCircuitTimerBinding
import com.example.chrono.util.objects.CircuitObject
import com.google.gson.GsonBuilder
import kotlin.math.roundToInt

class CircuitTimerActivity : AppCompatActivity() {
    private var bind: ActivityCircuitTimerBinding? = null

    enum class TimerState { INIT, RUNNING, PAUSED }
    enum class RunningState { INIT, WORK, REST }

    private lateinit var countdown: CountDownTimer
    private var timerState: TimerState = TimerState.INIT
    private lateinit var runningState: RunningState

    private var secondsLeft: Float = 0.0f
    private var currentSet: Int = 0

    private var sets: Int = 0
    private var timeRest: Int = 0
    private var timeWork: Int = 0

//    private lateinit var audioPlayer: AsyncPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit_timer)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_circuit_timer)

        loadTimer(
            GsonBuilder().create()
                .fromJson(intent.getStringExtra("circuitObject"), CircuitObject::class.java)
        )

        // Initialize stuff perhaps

        bind!!.startbutton.setOnClickListener {
            workout()
            timerState = TimerState.RUNNING
            updateButtonUI()
        }

        bind!!.pausebutton.setOnClickListener {
            countdown.cancel()
            timerState = TimerState.PAUSED
            updateButtonUI()
        }

        bind!!.resumebutton.setOnClickListener {
            createTimer(secondsLeft.toInt(), true)
            timerState = TimerState.RUNNING
            updateButtonUI()
        }

        bind!!.closeButton.setOnClickListener {
            // Make sure that the timer is shut down
            countdown.cancel()
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun createTimer(seconds: Int, wasPaused: Boolean) {
        var time = seconds.toFloat() * 1000 + 250
        if (wasPaused) {
            time = secondsLeft * 1000 + 250
        }
        countdown = object : CountDownTimer(time.toLong(), 250) {
            override fun onTick(p0: Long) {
                if ((p0.toFloat().roundToInt() / 1000.0f) != secondsLeft) {
                    secondsLeft = (p0.toFloat() / 1000.0f).roundToInt().toFloat()
                    updateTimerUI()
                }
            }

            override fun onFinish() {
                if (currentSet != 0) {
                    when (runningState) {
                        RunningState.INIT -> {
                            workout()
                        }
                        RunningState.WORK -> {
                            rest()
                        }
                        RunningState.REST -> {
                            workout()
                        }
                    }
                } else {
                    isDone()
                }
            }
        }.start()
    }

    private fun isDone() {
        Toast.makeText(this, "It's done", Toast.LENGTH_SHORT).show()
        timerState = TimerState.INIT
        runningState = RunningState.INIT
        updateButtonUI()
    }

    // Update UI for every tick, possibly need to do more in the future
    fun updateTimerUI() {
        bind!!.countdown.text = (secondsLeft).toInt().toString()
    }

    private fun updateButtonUI() {
        when (timerState) {
            TimerState.INIT -> {
                bind!!.initButtonLay.visibility = View.VISIBLE
                bind!!.runButtonLay.visibility = View.GONE
                bind!!.pauseButtonLay.visibility = View.GONE
                bind!!.countdown.text = "0"
            }
            TimerState.RUNNING -> {
                bind!!.initButtonLay.visibility = View.GONE
                bind!!.runButtonLay.visibility = View.VISIBLE
                bind!!.pauseButtonLay.visibility = View.GONE
            }
            TimerState.PAUSED -> {
                bind!!.initButtonLay.visibility = View.GONE
                bind!!.runButtonLay.visibility = View.GONE
                bind!!.pauseButtonLay.visibility = View.VISIBLE
            }
        }
    }

    private fun loadTimer(circuit: CircuitObject) {
        sets = circuit.sets!!
        currentSet = sets
        timeRest = circuit.rest!!
        timeWork = circuit.work!!

        bind!!.name.text = circuit.name
    }

    private fun workout() {
        runningState = RunningState.WORK
        bind!!.state.text = "Workout"
        bind!!.set.text = "Set Number " + currentSet.toString()
        createTimer(timeWork, false)
    }

    private fun rest() {
        runningState = RunningState.REST
        bind!!.state.text = "Rest"
        bind!!.set.text = "Set Number " + currentSet.toString()
        createTimer(timeRest, false)
        currentSet -= 1
    }
}