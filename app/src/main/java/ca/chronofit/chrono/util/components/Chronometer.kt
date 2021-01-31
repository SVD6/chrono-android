package ca.chronofit.chrono.util.components

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.NotificationManagerCompat
import ca.chronofit.chrono.util.helpers.SwatchNotifManager
import ca.chronofit.chrono.util.helpers.formatTime
import ca.chronofit.chrono.util.helpers.getTime
// Tutorial followed: https://www.geeksforgeeks.org/how-to-create-a-stopwatch-app-using-android-studio/
class Chronometer @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : AppCompatTextView(
    context!!, attrs, defStyle
) {
    var tenMsCounter = 0

    private val swPeriod = 10.toLong() // sw period in milliseconds

    private var running = false
    lateinit var runnable: Runnable

    private val notification = SwatchNotifManager(context!!)

    private var prevSec = -1

    private var defaultTime = "00:00.00"

    private var notificationEnabled: Boolean? = null
    private var notificationTime = ""

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
        if(visibility == VISIBLE){
            NotificationManagerCompat.from(context).cancel(notification.notificationId)
        }
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
                val elapsed = getTime(tenMsCounter)
                val time = formatTime(elapsed, ":")
                notificationTime = time.dropLast(3)
                updateText(time)
                if (running) {
                    if (prevSec != elapsed.seconds) {
                        notification.createRunningNotification(notificationTime, true)
                        prevSec = elapsed.seconds
                    }
                    tenMsCounter++
                    handler.postDelayed(runnable, swPeriod)
                }
            }
            handler.post(runnable)
        }
    }

    fun stop() {
        running = false
        notification.createRunningNotification(notificationTime, false)
    }

    fun resume() {
        start()
    }

    fun reset() {
        running = false
        tenMsCounter = 0
        prevSec = -1
        updateText(defaultTime)
        NotificationManagerCompat.from(context).cancel(notification.notificationId)
    }

    fun setNotificationEnabled(setting: Boolean) {
        notificationEnabled = setting
    }

    companion object {
        const val MS_IN_SECONDS = 100
        const val MS_IN_HOURS = 3600 * MS_IN_SECONDS
        const val MS_IN_MINUTES = 60 * MS_IN_SECONDS
    }
}