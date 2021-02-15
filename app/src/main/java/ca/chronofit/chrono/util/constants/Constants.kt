package ca.chronofit.chrono.util.constants

object Constants {
    // Fragment TAGS
    const val CIRCUIT_FRAG: String = "CircuitFrag"
    const val STOPWATCH_FRAG: String = "StopwatchFrag"
    const val SETTINGS_FRAG: String = "SettingsFrag"

    // Shared Preference Values
    const val CIRCUITS: String = "CIRCUITS"
    const val FIRST_RUN: String = "first_run"
    const val NUM_COMPLETE: String = "num_complete"

    // Settings Shared Preferences
    const val GET_READY_SETTING: String = "ready_time"
    const val AUDIO_SETTING: String = "prompts"
    const val LAST_REST_SETTING: String = "last_rest"
    const val NOTIFICATION_SETTING: String = "notifications"
    const val DARK_MODE_SETTING: String = "dark_mode"

    // Activity Request Codes
    const val DASH_TO_CREATE: Int = 10001
    const val DASH_TO_TIMER: Int = 10002
    const val DASH_TO_EDIT: Int = 10003

    // Dark Mode Options
    const val DARK_MODE: String = "On"
    const val LIGHT_MODE: String = "Off"
    const val SYSTEM_DEFAULT: String = "System Default"

    // Version Number
    const val VERSION_NUMBER: String = "Test Version 0.2.1"

    // Remote Config Keys
    const val CONFIG_REVIEW_THRESHOLD: String = "app_review_threshold"
    const val CONFIG_LATEST_VERSION: String = "latest_version_of_app"

    // Notification Id
    const val SWATCH_NOTIFICATION_ID: Int = 2416638
    const val TIMER_NOTIFICATION_ID: Int = 341032
}