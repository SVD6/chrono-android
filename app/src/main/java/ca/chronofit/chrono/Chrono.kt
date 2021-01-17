package ca.chronofit.chrono

import android.app.Application

class Chrono: Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO
        // EMPTY FOR NOW BUT WILL BE NEEDED VERY SHORTLY

    }

    companion object {

        // Settings
        var notifications: Boolean = false
        var darkMode: Boolean = false
        var audioPrompts: Boolean = true
        var readytime: String = "5"

    }
}