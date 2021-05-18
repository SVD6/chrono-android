package ca.chronofit.chrono.util.constants

object Events {
    // Circuit Create Related Events
    const val CREATE_STARTED = "circuit_create"
    const val CREATE_COMPLETE = "circuit_added"

    // Circuit Activity Related Events
    const val CIRCUIT_STARTED = "circuit_started"
    const val CIRCUIT_PAUSED = "circuit_paused"
    const val CIRCUIT_COMPLETED = "circuit_completed"
    const val CIRCUIT_RESTARTED = "circuit_restarted"
    const val CIRCUIT_EXITED = "circuit_exited"

    // Stopwatch Related Events
    const val STOPWATCH_STARTED = "stopwatch_started"
    const val STOPWATCH_STOPPED = "stopwatch_stopped"
    const val STOPWATCH_LAPPED = "stopwatch_lapped"

    // Other
    const val USER_REVIEWED = "user_reviewed"
    const val EASTER_EGG_FOUND = "easter_egg_found"
}