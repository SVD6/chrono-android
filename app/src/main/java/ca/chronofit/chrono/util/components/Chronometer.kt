package ca.chronofit.chrono.util.components

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.RemoteViews
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ca.chronofit.chrono.R
import ca.chronofit.chrono.main.stopwatch.StopwatchFrag
import ca.chronofit.chrono.util.services.NotificationIntentService
import java.text.DecimalFormat

class Chronometer @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : AppCompatTextView(
    context!!, attrs, defStyle
) {
    private val tickWhat = 2
    private var mBase: Long = 0
    private var mVisible = true
    private var mStarted = false
    private var mRunning = false
    private var onChronometerTickListener: OnChronometerTickListener? = null
    private var timeElapsed: Long = 0
    private var showNotification = false
    private var notificationTime = "00:00"
    private val notificationId = 12

    private var lastSecond = -1

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
        createStoppedNotification(notificationTime)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        showNotification = true
        updateRunning()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        showNotification = visibility != VISIBLE
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
        text += df.format(seconds.toLong())
        if (lastSecond != seconds) {
            notificationTime = text
            lastSecond = seconds
            if (mStarted) {
                createRunningNotification(notificationTime)
            } else {
                NotificationManagerCompat.from(context).cancel(notificationId) // Reset is pressed
            }
        }
        text += ":$milliseconds"
        text += tenthMillisecond.toString()
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
        fun onChronometerTick(chronometer: Chronometer?)
    }

    init {
        init()
    }

    private fun createRunningNotification(time: String) {
        val customView = RemoteViews(context.packageName, R.layout.notification_stopwatch_running)

        val stopIntent = Intent(context, NotificationIntentService::class.java)
        stopIntent.action = StopwatchFrag.STOP
        customView.setOnClickPendingIntent(
            R.id.stop_stopwatch,
            PendingIntent.getService(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        )

        val resetIntent = Intent(context, NotificationIntentService::class.java)
        resetIntent.action = StopwatchFrag.RESET
        customView.setOnClickPendingIntent(
            R.id.reset_stopwatch,
            PendingIntent.getService(context, 1, resetIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        )

        customView.setTextViewText(R.id.elapsed_time, time)

        val builder =
            NotificationCompat.Builder(
                context,
                context.getString(R.string.stopwatch_notification_channel_id)
            )
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentTitle("Stopwatch")
                .setContentText(time)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(customView)

        if (showNotification) {
            with(NotificationManagerCompat.from(context)) {
                //notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }
        }
    }

    private fun createStoppedNotification(time: String) {
        val customView = RemoteViews(context.packageName, R.layout.notification_stopwatch_stopped)

        val resumeIntent = Intent(context, NotificationIntentService::class.java)
        resumeIntent.action = StopwatchFrag.RESUME
        customView.setOnClickPendingIntent(
            R.id.resume_stopwatch,
            PendingIntent.getService(context, 0, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        )

        val resetIntent = Intent(context, NotificationIntentService::class.java)
        resetIntent.action = StopwatchFrag.RESET
        customView.setOnClickPendingIntent(
            R.id.reset_stopwatch,
            PendingIntent.getService(context, notificationId, resetIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        )

        customView.setTextViewText(R.id.elapsed_time, time)

        val builder =
            NotificationCompat.Builder(
                context,
                context.getString(R.string.stopwatch_notification_channel_id)
            )
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentTitle("Stopwatch")
                .setContentText(time)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(customView)

        if (showNotification) {
            with(NotificationManagerCompat.from(context)) {
                //notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }
        }
    }
}