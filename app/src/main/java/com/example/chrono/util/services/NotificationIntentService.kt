package com.example.chrono.util.services

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.chrono.main.stopwatch.StopwatchFrag


class NotificationIntentService
    : IntentService("notificationIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        when (intent!!.action) {
            StopwatchFrag.STOP -> {
                val leftHandler = Handler(Looper.getMainLooper())
                leftHandler.post(Runnable {
                    var stopIntent = Intent()
                    stopIntent.action = intent!!.action
                    sendBroadcast(stopIntent)
                })

            }
            "right" -> {
                val rightHandler = Handler(Looper.getMainLooper())
                rightHandler.post(Runnable {
                    Toast.makeText(
                        baseContext,
                        "You clicked the right button",
                        Toast.LENGTH_LONG
                    ).show()
                })
            }
        }
    }
}