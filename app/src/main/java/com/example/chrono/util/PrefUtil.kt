package com.example.chrono.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.chrono.main.TimerFrag

class PrefUtil {
    companion object {

        private const val TIMER_LENGTH_ID = "com.example.chrono.timer_length"
        private const val TIMER_STATE_ID = "com.example.chrono.timer_state"
        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID =
            "com.example.chrono.previous_timer_length_seconds"
        private const val SECONDS_REMAINING_ID = "com.example.chrono.seconds_remaining"
        private const val ALARM_SET_TIME_ID = "com.example.chrono.backgrounded_time"

        fun getTimerLength(context: Context): Int {
            // placeholder
            return 1
        }

        fun getPreviousTimerLengthSeconds(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        fun getTimerState(context: Context): TimerFrag.TimerState {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerFrag.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerFrag.TimerState, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

        fun getSecondsRemaining(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        fun getAlarmSetTime(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}