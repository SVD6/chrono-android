package ca.chronofit.chrono.circuit

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.media.ToneGenerator
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.ActivityCircuitTimerBinding
import ca.chronofit.chrono.databinding.DialogAlertBinding
import ca.chronofit.chrono.util.BaseActivity
import ca.chronofit.chrono.util.constants.Constants
import ca.chronofit.chrono.util.constants.Events
import ca.chronofit.chrono.util.objects.CircuitObject
import ca.chronofit.chrono.util.objects.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.GsonBuilder
import java.math.BigDecimal
import kotlin.math.roundToInt

class CircuitTimerActivity : BaseActivity() {
    private lateinit var bind: ActivityCircuitTimerBinding
    private lateinit var soundPool: SoundPool
    private lateinit var soundMap: HashMap<String, Int>
    private var tone: ToneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    private lateinit var countdown: CountDownTimer
    private var secondsLeft: Float = 0.0f
    private val celebrateTimeout = 2500L // Timeout delay
    private var getReadyTime: Int = 5
    private var audioPrompts: Boolean = true
    private var skipLastRest: Boolean = false
    private var soundEffect: String = Constants.SOUND_LONG_WHISTLE
    private var timerState: TimerState = TimerState.INIT
    private var runningState: RunningState = RunningState.INIT
    private lateinit var circuit: CircuitObject
    private var currentSet: Int = 0
    private var sets: Int = 0
    private var timeRest: Int = 0
    private var timeWork: Int = 0
    private var criticalSeconds: Int = 0

    enum class TimerState { INIT, RUNNING, PAUSED }
    enum class RunningState { READY, INIT, WORK, REST }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set Animations
        with(window) {
            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            enterTransition = Fade()
            exitTransition = Fade()
        }
        bind = DataBindingUtil.setContentView(this, R.layout.activity_circuit_timer)

        // Load Data
        initData()

        // Initialize View
        updateButtonUI()
        updateRestUI()

        // Initialize SoundPool
        initSounds()

        bind.startButton.setOnClickListener {
            FirebaseAnalytics.getInstance(this).logEvent(Events.CIRCUIT_STARTED, Bundle())
            start()
        }

        bind.pauseButton.setOnClickListener {
            FirebaseAnalytics.getInstance(this).logEvent(Events.CIRCUIT_PAUSED, Bundle())
            pause()
        }

        bind.resumeButton.setOnClickListener {
            FirebaseAnalytics.getInstance(this).logEvent(Events.CIRCUIT_RESTARTED, Bundle())
            resume()
        }

        bind.countdown.setOnClickListener {
            if (timerState == TimerState.RUNNING && runningState != RunningState.READY) {
                FirebaseAnalytics.getInstance(this).logEvent(Events.CIRCUIT_PAUSED, Bundle())
                pause()
            } else if (timerState == TimerState.PAUSED) {
                FirebaseAnalytics.getInstance(this).logEvent(Events.CIRCUIT_RESTARTED, Bundle())
                resume()
            }
        }

        bind.stopButton.setOnClickListener {
            countdown.cancel()
            timerState = TimerState.INIT
            runningState = RunningState.INIT
            updateButtonUI()
            updateRestUI()
        }

        bind.closeButton.setOnClickListener { exitTimer() }
    }

    private fun initData() {
        circuit = GsonBuilder().create()
            .fromJson(intent.getStringExtra("circuitObject"), CircuitObject::class.java)
        getReadyTime = intent.getIntExtra("readyTime", 5)
        audioPrompts = intent.getBooleanExtra("audioPrompts", true)
        skipLastRest = intent.getBooleanExtra("lastRest", false)
        soundEffect = intent.getStringExtra("soundEffect")!!
    }

    private fun initSounds() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        soundPool = SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build()
        soundMap = HashMap()

        // Load Sounds
        soundMap[soundEffect] = soundPool.load(this, getSoundFile(soundEffect), 1)
        soundMap[Constants.SOUND_COMPLETE] = soundPool.load(this, R.raw.complete, 1)
    }

    private fun playSound(sound: String) {
        //TODO: Set volume properly
        soundPool.play(soundMap[sound]!!, 1f, 1f, 0, 0, 1f)
    }

    private fun startTimer(seconds: Int, wasPaused: Boolean) {
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
                        RunningState.READY -> {
                            workout()
                        }
                        RunningState.WORK -> {
                            if (currentSet == 1 && skipLastRest) {
                                celebrate()
                            } else {
                                rest()
                            }
                        }
                        RunningState.REST -> {
                            workout()
                        }
                        else -> celebrate()
                    }
                } else {
                    celebrate()
                }
            }
        }.start()
    }

    private fun celebrate() {
        // Load celebrate layout
        bind.mainLayout.visibility = View.GONE
        bind.celebrateLayout.visibility = View.VISIBLE
        window.statusBarColor = ContextCompat.getColor(this, R.color.gradient_start)
        FirebaseAnalytics.getInstance(this).logEvent(Events.CIRCUIT_COMPLETED, Bundle())

        //Save total time and sets
        if (PreferenceManager.get<Int>(Constants.TOTAL_CIRCUITS) == null) {
            PreferenceManager.put(1, Constants.TOTAL_CIRCUITS)
        } else {
            val currSets = PreferenceManager.get<Int>(Constants.TOTAL_CIRCUITS)!! + 1
            PreferenceManager.put(currSets, Constants.TOTAL_CIRCUITS)
        }
        if (PreferenceManager.get<Int>(Constants.TOTAL_TIME) == null) {
            PreferenceManager.put(BigDecimal(0), Constants.TOTAL_CIRCUITS)
        } else {
            val thisTotal = circuit.sets!!.times(timeWork)
            val currTotal = PreferenceManager.get<Int>(Constants.TOTAL_TIME)
            PreferenceManager.put(currTotal!! + thisTotal, Constants.TOTAL_TIME)
        }

        //Play Complete Sound
        playSound(Constants.SOUND_COMPLETE)
        // Wait 2.5 seconds before showing the finish prompt
        Handler(
            Looper.getMainLooper()
        ).postDelayed(
            {
                isDone()
            }, celebrateTimeout
        )
    }

    private fun isDone() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (PreferenceManager.get<Int>(Constants.NUM_COMPLETE) != null) {
            val increment = (PreferenceManager.get<Int>(Constants.NUM_COMPLETE)!! + 1)
            PreferenceManager.put(increment, Constants.NUM_COMPLETE)
        } else {
            PreferenceManager.put(1, Constants.NUM_COMPLETE)
        }

        val builder =
            MaterialAlertDialogBuilder(this, R.style.CustomMaterialDialog).create()
        val dialogBinding = DialogAlertBinding.inflate(LayoutInflater.from(this))

        // Set Dialog Views
        dialogBinding.dialogTitle.text = getString(R.string.circuit_complete)
        dialogBinding.dialogSubtitle.text = getString(R.string.circuit_complete_subtitle)
        dialogBinding.confirm.text = getString(R.string.circuit_complete_confirm)
        dialogBinding.cancel.text = getString(R.string.circuit_complete_cancel)

        dialogBinding.confirm.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        dialogBinding.confirm.setTextColor(ContextCompat.getColor(this, R.color.white))

        // User wants to return to dashboard
        dialogBinding.confirm.setOnClickListener {
            builder.dismiss()
            setResult(Activity.RESULT_OK)
            finish()
        }

        // If the user wants to run the circuit again
        dialogBinding.cancel.setOnClickListener {
            // Reload the circuit
            builder.dismiss()
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }

        builder.setCancelable(false)
        builder.setCanceledOnTouchOutside(false)

        // Display the Dialog
        builder.setView(dialogBinding.root)
        builder.show()
    }

    private fun getReady() {
        runningState = RunningState.READY
        timerState = TimerState.RUNNING
        bind.initButtonLayout.visibility = View.GONE
        updateRestUI()
        startTimer(getReadyTime, false)
    }

    private fun workout() {
        if (audioPrompts) playSound(soundEffect)
        runningState = RunningState.WORK
        updateButtonUI()
        updateRestUI()
        startTimer(timeWork, false)
    }

    private fun rest() {
        if (audioPrompts) tone.startTone(ToneGenerator.TONE_DTMF_2, 500)
        runningState = RunningState.REST
        updateRestUI()
        startTimer(timeRest, false)
        currentSet -= 1
    }

    private fun pause() {
        countdown.cancel()
        timerState = TimerState.PAUSED
        updateButtonUI()
    }

    private fun start() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        loadTimer(circuit)
        getReady()
    }

    private fun resume() {
        startTimer(secondsLeft.toInt(), true)
        timerState = TimerState.RUNNING
        updateRestUI()
        updateButtonUI()
    }

    // Update UI for every tick, possibly need to do more in the future
    private fun updateTimerUI() {
        if (criticalSeconds != 0 && secondsLeft <= criticalSeconds && runningState == RunningState.WORK) {
            bind.mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.stop_red))
            bind.countdown.setTextColor(ContextCompat.getColor(this, R.color.white))
            bind.currentSet.setTextColor(ContextCompat.getColor(this, R.color.white))
            bind.currentState.setTextColor(ContextCompat.getColor(this, R.color.white))

            window.statusBarColor = ContextCompat.getColor(this, R.color.stop_red)
        }
        if (timeRest > 5 && runningState == RunningState.REST && secondsLeft <= 5) {
            bind.currentState.text = getString(R.string.get_ready)
        }
        bind.countdown.text = (secondsLeft).toInt().toString()
    }

    private fun updateButtonUI() {
        when (timerState) {
            TimerState.INIT -> {
                bind.initButtonLayout.visibility = View.VISIBLE
                bind.runButtonLayout.visibility = View.GONE
                bind.pauseButtonLayout.visibility = View.GONE
                bind.countdown.text = "0"
            }
            TimerState.RUNNING -> {
                bind.initButtonLayout.visibility = View.GONE
                bind.runButtonLayout.visibility = View.VISIBLE
                bind.pauseButtonLayout.visibility = View.GONE
            }
            TimerState.PAUSED -> {
                bind.initButtonLayout.visibility = View.GONE
                bind.runButtonLayout.visibility = View.GONE
                bind.pauseButtonLayout.visibility = View.VISIBLE
                bind.closeButton.visibility = View.VISIBLE

                // Just for paused we put non-button UI stuff
                if (isUsingNightModeResources()) {
                    bind.mainLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.darkBackground
                        )
                    )
                    bind.countdown.setTextColor(ContextCompat.getColor(this, R.color.darkText))
                    bind.currentSet.setTextColor(ContextCompat.getColor(this, R.color.darkText))
                    bind.currentState.setTextColor(ContextCompat.getColor(this, R.color.darkText))
                } else {
                    bind.mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    bind.countdown.setTextColor(ContextCompat.getColor(this, R.color.lightText))
                    bind.currentSet.setTextColor(ContextCompat.getColor(this, R.color.lightText))
                    bind.currentState.setTextColor(ContextCompat.getColor(this, R.color.lightText))
                }

                window.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateRestUI() {
        when (runningState) {
            RunningState.READY -> {
                bind.currentState.text = getString(R.string.get_ready)
            }
            RunningState.INIT -> {
                bind.currentSet.text = getString(R.string.empty)
                bind.currentState.text = getString(R.string.lets_go)
            }
            RunningState.WORK -> {
                bind.currentState.text = getString(R.string.workout)
                bind.currentSet.text = "Set " + (sets - currentSet.toString().toInt() + 1)
                bind.closeButton.visibility = View.INVISIBLE
                bind.mainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.beautiful_blue
                    )
                )
                bind.countdown.setTextColor(ContextCompat.getColor(this, R.color.white))
                bind.currentSet.setTextColor(ContextCompat.getColor(this, R.color.white))
                bind.currentState.setTextColor(ContextCompat.getColor(this, R.color.white))

                window.statusBarColor = ContextCompat.getColor(this, R.color.beautiful_blue)
            }
            RunningState.REST -> {
                bind.currentState.text = getString(R.string.rest)

                bind.mainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.rest_yellow
                    )
                )
                bind.countdown.setTextColor(ContextCompat.getColor(this, R.color.white))
                bind.currentSet.setTextColor(ContextCompat.getColor(this, R.color.white))
                bind.currentState.setTextColor(ContextCompat.getColor(this, R.color.white))

                window.statusBarColor = ContextCompat.getColor(this, R.color.rest_yellow)
            }
        }
    }

    private fun loadTimer(circuit: CircuitObject) {
        sets = circuit.sets!!
        currentSet = sets
        timeRest = circuit.rest!!
        timeWork = circuit.work!!

        criticalSeconds = if (timeWork > 5) {
            5
        } else {
            0
        }
    }

    private fun getSoundFile(name: String): Int {
        return when (name) {
            Constants.SOUND_LONG_WHISTLE -> R.raw.long_whistle
            Constants.SOUND_SHORT_WHISTLE -> R.raw.short_whistle
            else -> R.raw.long_whistle
        }
    }

    private fun exitTimer() {
        FirebaseAnalytics.getInstance(this).logEvent(Events.CIRCUIT_EXITED, Bundle())
        // Make sure that the timer is shut down
        if (timerState != TimerState.INIT) {
            countdown.cancel()
        }
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onBackPressed() {
        exitTimer()
        super.onBackPressed()
    }
}