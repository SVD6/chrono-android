package ca.chronofit.chrono.circuit

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.res.TypedArray
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.media.ToneGenerator
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Fade
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.ActivityCircuitTimerBinding
import ca.chronofit.chrono.util.BaseActivity
import ca.chronofit.chrono.util.constants.Constants
import ca.chronofit.chrono.util.constants.Events
import ca.chronofit.chrono.util.objects.CircuitObject
import ca.chronofit.chrono.util.objects.PreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.math.BigDecimal
import kotlin.concurrent.thread
import kotlin.math.roundToInt
import kotlin.random.Random

class CircuitTimerActivity : BaseActivity() {
    private lateinit var bind: ActivityCircuitTimerBinding
    private lateinit var countdown: CountDownTimer
    private lateinit var soundPool: SoundPool
    private lateinit var soundMap: HashMap<String, Int>
    private lateinit var circuit: CircuitObject
    private var tone: ToneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    private var secondsLeft: Float = 0.0f
    private var getReadyTime: Int = 5
    private var audioPrompts: Boolean = true
    private var skipLastRest: Boolean = false
    private var soundEffect: String = Constants.SOUND_LONG_WHISTLE
    private var timerState: TimerState = TimerState.INIT
    private var runningState: RunningState = RunningState.INIT
    private var currentSet: Int = 0
    private var sets: Int = 0
    private var timeRest: Int = 0
    private var timeWork: Int = 0
    private var criticalSeconds: Int = 0
    private var fact: String? = null

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

        setupView()
        initData()
        initSounds()
        getFact()
    }

    private fun setupView() {
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

        bind.celebrateAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                println("debug: animation started")
                bind.celebrateAnimation.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                println("debug: animation ended")
                bind.celebrateAnimation.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
                println("debug: animation cancelled")
            }

            override fun onAnimationRepeat(animation: Animator?) {
                println("debug: animation repeated")
            }
        })

        updateButtonUI()
        updateRestUI()
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
//                            workout()
                            complete()
                        }
                        RunningState.WORK -> {
                            if (currentSet == 1 && skipLastRest) {
                                complete()
                            } else {
//                                rest()
                                complete()
                            }
                        }
                        RunningState.REST -> {
//                            workout()
                            complete()
                        }
                        else -> complete()
                    }
                } else {
                    complete()
                }
            }
        }.start()
    }

    private fun complete() {
        window.statusBarColor = Color.TRANSPARENT
        bind.mainLayout.visibility = View.GONE
        bind.celebrateLayout.visibility = View.VISIBLE

        FirebaseAnalytics.getInstance(this).logEvent(Events.CIRCUIT_COMPLETED, Bundle())
        playSound(Constants.SOUND_COMPLETE)

        bind.finishButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        bind.restartButton.setOnClickListener {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }

        bind.fact.text = fact
        val thisTotal = circuit.sets!!.times(timeWork)
        when {
            thisTotal <= 10 -> {
                bind.quote.text = getString(R.string.quote_weak)
            }
            thisTotal in 11..29 -> {
                bind.quote.text = getString(R.string.quote_medium)
            }
            else -> {
                bind.quote.text = getString(R.string.quote_strong)
            }
        }

        val animations: TypedArray = resources.obtainTypedArray(R.array.chrono_cat_files)
        val selectedAnimation = animations.getString(Random.nextInt(1, 5) - 1)

        bind.factAnimation!!.setAnimation(selectedAnimation)
        bind.factAnimation!!.repeatCount = ValueAnimator.INFINITE
        bind.factAnimation!!.playAnimation()

        if (PreferenceManager.get<Int>(Constants.TOTAL_CIRCUITS) == null) {
            PreferenceManager.put(1, Constants.TOTAL_CIRCUITS)
        } else {
            val currSets = PreferenceManager.get<Int>(Constants.TOTAL_CIRCUITS)!! + 1
            PreferenceManager.put(currSets, Constants.TOTAL_CIRCUITS)
        }
        if (PreferenceManager.get<Int>(Constants.TOTAL_TIME) == null) {
            PreferenceManager.put(BigDecimal(0), Constants.TOTAL_CIRCUITS)
        } else {
            val currTotal = PreferenceManager.get<Int>(Constants.TOTAL_TIME)
            PreferenceManager.put(currTotal!! + thisTotal, Constants.TOTAL_TIME)
        }

        animations.recycle()
    }

    private fun getFact() {
        val client = OkHttpClient()

        thread {
            val request = Request.Builder()
                .url(getString(R.string.fact_api))
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseObj = JSONObject(response.body!!.string())
                    println("debug: $responseObj")
                    fact = responseObj.getString("text")
                } else {
                    throw java.io.IOException("Unexpected code $response")
                }
            }
        }
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