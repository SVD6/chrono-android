package com.example.chrono.util.components

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.text.DecimalFormat

class MyChronometer @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : AppCompatTextView(
    context!!, attrs, defStyle
) {

    private val tickWhat = 2
    private var mBase: Long = 0
    private var mVisible = false
    private var mStarted = false
    private var mRunning = false
    private var onChronometerTickListener: OnChronometerTickListener? = null
    private var timeElapsed: Long = 0

    var base: Long
        get() = mBase
        set(base) {
            mBase = base
            dispatchChronometerTick()
            updateText(SystemClock.elapsedRealtime())
        }

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler(
        Looper.getMainLooper()
    ) {
        override fun handleMessage(m: Message) {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime())
                dispatchChronometerTick()
                sendMessageDelayed(
                    Message.obtain(this, tickWhat),
                    10
                )
            }
        }
    }

    private fun init() {
        mBase = SystemClock.elapsedRealtime()
        updateText(mBase)
    }

    fun start() {
        mStarted = true
        updateRunning()
    }

    fun stop() {
        mStarted = false
        updateRunning()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mVisible = false
        updateRunning()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        mVisible = visibility == VISIBLE
        updateRunning()
    }

    @Synchronized
    private fun updateText(now: Long) {
        timeElapsed = now - mBase
        val df = DecimalFormat("00")
        val hours = (timeElapsed / (3600 * 1000)).toInt()
        var remaining = (timeElapsed % (3600 * 1000)).toInt()
        val minutes = remaining / (60 * 1000)
        remaining %= (60 * 1000)
        val seconds = remaining / 1000
        remaining %= 1000
        val milliseconds = timeElapsed.toInt() % 1000 / 100
        remaining %= 100
        val tenthMillisecond = remaining % 10
        var text = ""
        if (hours > 0) {
            text += df.format(hours.toLong()) + ":"
        }
        text += df.format(minutes.toLong()) + ":"
        text += df.format(seconds.toLong()) + ":"
        text += milliseconds.toString() + tenthMillisecond.toString()
        setText(text)
    }

    private fun updateRunning() {
        val running = mVisible && mStarted
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime())
                dispatchChronometerTick()
                mHandler.sendMessageDelayed(
                    Message.obtain(
                        mHandler,
                        tickWhat
                    ), 10
                )
            } else {
                mHandler.removeMessages(tickWhat)
            }
            mRunning = running
        }
    }

    fun dispatchChronometerTick() {
        if (onChronometerTickListener != null) {
            onChronometerTickListener!!.onChronometerTick(this)
        }
    }

    interface OnChronometerTickListener {
        fun onChronometerTick(chronometer: MyChronometer?)
    }

    init {
        init()
    }
}