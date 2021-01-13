package com.example.chrono.util.services

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import ca.chronofit.chrono.main.stopwatch.StopwatchFrag



class NotificationIntentService
    : IntentService("notificationIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        when (intent!!.action) {
            StopwatchFrag.STOP -> {
                val stopHandler = Handler(Looper.getMainLooper())
                stopHandler.post(Runnable {
                    var stopIntent = Intent()
                    stopIntent.action = intent!!.action
                    sendBroadcast(stopIntent)
                })

            }
            StopwatchFrag.RESET -> {
                var resetHandler = Handler(Looper.getMainLooper())

                resetHandler.post(Runnable
                {
                    var resetIntent = Intent()
                    resetIntent.action = intent!!.action
                    sendBroadcast(resetIntent)
                }
                )
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