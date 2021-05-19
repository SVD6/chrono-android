package ca.chronofit.chrono.util.helpers

import android.content.res.Resources
import android.view.ViewGroup
import android.widget.FrameLayout
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

fun allocateForDeviceBars(res: Resources, view: ViewGroup) {
    val navBar = res.getIdentifier("navigation_bar_height", "dimen", "android")
    val statusBar = res.getIdentifier("status_bar_height", "dimen", "android")
    val navBarSize = res.getDimensionPixelSize(navBar)
    val statusBarSize = res.getDimensionPixelSize(statusBar)
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.setMargins(0, statusBarSize, 0, navBarSize)
    view.layoutParams = params
}