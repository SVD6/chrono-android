package ca.chronofit.chrono.util.components

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.NotificationManagerCompat
import ca.chronofit.chrono.util.notificationManager.StopwatchNotificationManager

class Chronometer @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : AppCompatTextView(
    context!!, attrs, defStyle
) {
    var tenMsCounter = 0

    private var running = false
    lateinit var runnable: Runnable

    private val msInSeconds = 100
    private val msInHours = 3600 * msInSeconds
    private val msInMinutes = 60 * msInSeconds

    private val notification = StopwatchNotificationManager(context!!)

    private var elapsedHrs = 0
    private var elapsedMins = 0
    private var elapsedSecs = 0
    private var elapsedMs = 0

    private var prevSec = -1

    private val template = "%02d:%02d%s%02d"
    private var timeWithHours = "00:00:00"
    private var timeWithoutHours = "00:00.00"

    private var notificationEnabled: Boolean? = null

    init {
        updateText(timeWithoutHours)
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
            val handler = Handler(Looper.getMainLooper())

            // Runnable calls itself every 10 ms
            runnable = Runnable {
                calculateElapsedTime()
                timeWithHours = template.format(elapsedHrs, elapsedMins, ":", elapsedSecs)
                timeWithoutHours = template.format(elapsedMins, elapsedSecs, ".", elapsedMs)
                val time = if (elapsedHrs == 0) timeWithoutHours else timeWithHours
                updateText(time)
                if (running) {
                    if (prevSec != elapsedSecs) {
                        notification.createRunningNotification(timeWithHours)
                        prevSec = elapsedSecs
                    }
                    tenMsCounter++
                    handler.postDelayed(runnable, 10)
                }
            }
            handler.post(runnable)
        }
    }

    fun stop() {
        running = false
        notification.createStoppedNotification(timeWithHours)
    }

    fun resume() {
        start()
    }

    fun reset() {
        running = false
        tenMsCounter = 0
        prevSec = -1
        timeWithHours = "00:00:00"
        timeWithoutHours = "00:00.00"
        updateText(timeWithoutHours)
        NotificationManagerCompat.from(context).cancel(notification.notificationId)
    }

    private fun calculateElapsedTime() {
        var remaining = tenMsCounter
        elapsedHrs = remaining / msInHours
        remaining %= msInHours
        elapsedMins = remaining / msInMinutes
        remaining %= msInMinutes
        elapsedSecs = remaining / msInSeconds
        elapsedMs = remaining % 100
    }

    fun setNotificationEnabled(setting: Boolean) {
        notificationEnabled = setting
    }
}