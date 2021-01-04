package com.example.chrono.util

import java.text.DecimalFormat

fun getTime(timeElapsed: Long): String {
    val df = DecimalFormat("00")

    val hours = (timeElapsed / (3600 * 1000))
    var remaining = (timeElapsed % (3600 * 1000))

    val minutes = remaining / (60 * 1000)
    remaining %= (60 * 1000)

    val seconds = remaining / 1000
    remaining %= 1000

    val milliseconds = timeElapsed % 1000 / 100
    remaining %= 100

    val tenthMillisecond = remaining % 10

    var text = ""

    if (hours > 0) {
        text += df.format(hours) + "."
    }

    text += df.format(minutes) + "."
    text += df.format(seconds) + "."
    text += milliseconds.toString() + tenthMillisecond.toString()

    return text
}