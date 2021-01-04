package com.example.chrono.util

import android.content.Context
import java.text.DecimalFormat

fun getDrawableByString(context: Context, identifier: String): Int {
    return context.resources.getIdentifier(identifier, "drawable", context.packageName)
}

fun getIconName(position: Int): String {
    when (position) {
        0 -> return "ic_stopwatch"
        1 -> return "ic_abs"
        2 -> return "ic_arm"
        3 -> return "ic_bottle"
        4 -> return "ic_boxer"
        5 -> return "ic_dumbbell"
        6 -> return "ic_gym"
        7 -> return "ic_gym_bag"
        8 -> return "ic_gym_2"
        9 -> return "ic_gym_3"
        10 -> return "ic_gym_4"
        11 -> return "ic_gymnast"
        12 -> return "ic_jump_rope"
        13 -> return "ic_mat"
        14 -> return "ic_punching_ball"
        15 -> return "ic_resistance"
        16 -> return "ic_resistance_1"
        17 -> return "ic_treadmill"
        18 -> return "ic_workout"
        19 -> return "ic_workout_3"
    }
    return "-1"
}

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

//fun getIconPosition(name: String): Int {
//    when (name) {
//        "ic_abs" -> return 0
//        "ic_arm" -> return 1
//        "ic_bottle" -> return 2
//        "ic_boxer" -> return 3
//        "ic_dumbbell" -> return 4
//        "ic_gym" -> return 5
//        "ic_gym_bag" -> return 6
//        "ic_gym_2" -> return 7
//        "ic_gym_3" -> return 8
//        "ic_gym_4" -> return 9
//        "ic_gymnast" -> return 10
//        "ic_jump_rope" -> return 11
//        "ic_mat" -> return 12
//        "ic_punching_ball" -> return 13
//        "ic_resistance" -> return 14
//        "ic_resistance_1" -> return 15
//        "ic_treadmill" -> return 16
//        "ic_workout" -> return 17
//        "ic_workout_3" -> return 18
//    }
//    return -1
//}