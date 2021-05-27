package ca.chronofit.chrono.util.services

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import ca.chronofit.chrono.stopwatch.StopwatchFrag

class NotificationIntentService
    : IntentService("notificationIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        if (intent!!.action == StopwatchFrag.STOP || intent.action == StopwatchFrag.RESUME || intent.action == StopwatchFrag.RESET) {
            val stopHandler = Handler(Looper.getMainLooper())
            stopHandler.post {
                val stopIntent = Intent()
                stopIntent.action = intent.action
                sendBroadcast(stopIntent)
            }
        }
    }
}