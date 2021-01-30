package ca.chronofit.chrono.util.helpers

import android.app.Activity
import android.content.Context
import ca.chronofit.chrono.util.components.Chronometer.Companion.MS_IN_HOURS
import ca.chronofit.chrono.util.components.Chronometer.Companion.MS_IN_MINUTES
import ca.chronofit.chrono.util.components.Chronometer.Companion.MS_IN_SECONDS
import ca.chronofit.chrono.util.objects.TimeObject
import java.text.DecimalFormat

fun getTime(timeElapsed: Int): TimeObject {


    var remaining = timeElapsed

    val elapsedHrs = remaining / MS_IN_HOURS
    remaining %= MS_IN_HOURS
    val elapsedMins = remaining / MS_IN_MINUTES
    remaining %= MS_IN_MINUTES
    val elapsedSecs = remaining / MS_IN_SECONDS
    val elapsedMs = remaining % 100

    var timeObject = TimeObject()

    timeObject.hours = elapsedHrs
    timeObject.minutes = elapsedMins
    timeObject.seconds = elapsedSecs
    timeObject.milliseconds = elapsedMs

    return timeObject
}

fun formatTime(time: TimeObject, separator: String): String {
    val df = DecimalFormat("00")
    var text = ""
    if (time.hours > 0) {
        text += df.format(time.hours) + separator
    }

    text += df.format(time.minutes) + separator
    text += df.format(time.seconds) + "." // milliseconds will always be separated by a period
    text += df.format(time.milliseconds)

    return text
}

fun isContextValid(context: Context?): Boolean {
    if (context == null) {
        return false
    }
    if (context is Activity) {
        val activity = context as Activity?
        return !activity!!.isDestroyed && !activity.isFinishing
    }
    return true
}