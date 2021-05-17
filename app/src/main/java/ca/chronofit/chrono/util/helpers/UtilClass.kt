package ca.chronofit.chrono.util.helpers

import ca.chronofit.chrono.util.objects.TimeObject
import java.text.DecimalFormat

fun getTime(timeElapsed: Long): TimeObject {
    val elapsedHrs = (timeElapsed / (3600 * 1000))
    var remaining = (timeElapsed % (3600 * 1000))
    val elapsedMins = remaining / (60 * 1000)
    remaining %= (60 * 1000)
    val elapsedSecs = remaining / 1000
    remaining %= 1000
    val elapsedMs = (timeElapsed.toInt() % 1000 / 100).toLong()
    remaining %= 100
    val elapsedTenMs = remaining % 10

    val timeObject = TimeObject()
    timeObject.hours = elapsedHrs
    timeObject.minutes = elapsedMins
    timeObject.seconds = elapsedSecs
    timeObject.milliseconds = elapsedMs
    timeObject.tenMilliseconds = elapsedTenMs

    return timeObject
}

fun formatTime(time: TimeObject, separator: String): String {
    val df = DecimalFormat("00")
    var text = ""
    if (time.hours > 0) {
        text += df.format(time.hours) + separator
    }

    text += df.format(time.minutes) + separator
    text += df.format(time.seconds) + "."
    text += time.milliseconds.toString() + time.tenMilliseconds.toString()
    return text
}