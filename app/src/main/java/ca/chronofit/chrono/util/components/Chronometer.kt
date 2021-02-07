package ca.chronofit.chrono.util.components

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.NotificationManagerCompat
import ca.chronofit.chrono.util.helpers.formatTime
import ca.chronofit.chrono.util.helpers.getTime
import ca.chronofit.chrono.util.helpers.SwatchNotifManager

class Chronometer @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : AppCompatTextView(
    context!!, attrs, defStyle
) {

    private val swPeriod = 10.toLong() // sw period in milliseconds

    private var running = false
    lateinit var runnable: Runnable

    private val notification = SwatchNotifManager(context!!)

    private var prevSec = -1

    private var defaultTime = "00:00.00"

    private var notificationEnabled: Boolean? = null
    private var notificationTime = ""

    var startedTime = 0L
    var stopTime = 0L
    var elapsed = 0L
    var delay = 0L



    init {
        updateText(defaultTime)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        notification.showNotification = true && (notificationEnabled == true)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        notification.showNotification = (visibility != VISIBLE) && (notificationEnabled == true)
    }

    private fun updateText(time: String) {
        text = time
    }

    fun start() {
        if (!running) {
            running = true
            startedTime = SystemClock.elapsedRealtime()
            val handler = Handler(Looper.getMainLooper())

            // Runnable calls itself every 10 ms
            runnable = Runnable {
                if (running){
                    elapsed = SystemClock.elapsedRealtime() - startedTime - delay
                }
                val elapsedTime = getTime(elapsed)
                val time = formatTime(elapsedTime, ":")
                notificationTime = time.dropLast(3)
                updateText(time)
                if (running) {
                    if (prevSec != elapsedTime.seconds) {
                        notification.createRunningNotification(notificationTime)
                        prevSec = elapsedTime.seconds
                    }
                }
                handler.postDelayed(runnable, swPeriod)
            }
            handler.post(runnable)
        }
    }


    fun stop() {
        stopTime = SystemClock.elapsedRealtime()
        running = false
        notification.createStoppedNotification(notificationTime)
    }

    fun resume() {
        delay = stopTime - SystemClock.elapsedRealtime()
        running = true
    }

    fun reset() {
        running = false
        elapsed = 0
        stopTime = 0
        prevSec = -1
        updateText(defaultTime)
        NotificationManagerCompat.from(context).cancel(notification.notificationId)
    }

    fun setNotificationEnabled(setting: Boolean) {
        notificationEnabled = setting
    }
    companion object{
        const val MS_IN_SECONDS = 1000
        const val MS_IN_HOURS = 3600 * MS_IN_SECONDS
        const val MS_IN_MINUTES = 60 * MS_IN_SECONDS
    }
}