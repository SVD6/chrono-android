package ca.chronofit.chrono.util.components

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.NotificationManagerCompat
import ca.chronofit.chrono.util.constants.Constants
import ca.chronofit.chrono.util.helpers.SwatchNotificationManager
import ca.chronofit.chrono.util.helpers.formatTime
import ca.chronofit.chrono.util.helpers.getTime

class Chronometer @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : AppCompatTextView(
    context!!, attrs, defStyle
) {
    private val notification = SwatchNotificationManager(context!!)
    private var notificationEnabled: Boolean? = null
    private var notificationTime = ""
    private val swPeriod = 10.toLong()
    private var prevSec = -1L
    private var defaultTime = "00:00.00"
    private var running = false
    private lateinit var runnable: Runnable
    private var startedTime = 0L
    private var stopTime = 0L
    var elapsedTime = 0L
    private var delayTime = 0L

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
        if (visibility == VISIBLE) {
            closeNotification()
        }
    }

    private fun updateText(time: String) {
        text = time
    }

    fun start() {
        if (!running) {
            running = true
            startedTime = SystemClock.elapsedRealtime()
            val handler = Handler(Looper.getMainLooper())

            runnable = Runnable {
                if (running) {
                    elapsedTime =
                        SystemClock.elapsedRealtime() - startedTime - delayTime // Time elapsed = current time - time sw was started - time spent in pause
                    val elapsedTime = getTime(elapsedTime)
                    val time = formatTime(elapsedTime, ":")
                    notificationTime = time.dropLast(3)
                    updateText(time)
                    if (prevSec != elapsedTime.seconds) {
                        notification.createRunningNotification(notificationTime, true)
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
        notification.createRunningNotification(notificationTime, false)
    }

    fun resume() {
        delayTime += SystemClock.elapsedRealtime() - stopTime
        running = true
    }

    fun reset() {
        running = false
        startedTime = 0
        elapsedTime = 0
        stopTime = 0
        delayTime = 0
        prevSec = -1
        updateText(defaultTime)
        closeNotification()
    }

    fun setNotificationEnabled(setting: Boolean) {
        notificationEnabled = setting
    }

    private fun closeNotification() {
        NotificationManagerCompat.from(context).cancel(Constants.SWATCH_NOTIFICATION_ID)
    }
}