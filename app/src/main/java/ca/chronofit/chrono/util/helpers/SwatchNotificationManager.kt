package ca.chronofit.chrono.util.helpers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getColor
import ca.chronofit.chrono.R
import ca.chronofit.chrono.stopwatch.StopwatchFrag
import ca.chronofit.chrono.util.constants.Constants
import ca.chronofit.chrono.util.services.NotificationIntentService

class SwatchNotificationManager(private val context: Context) {
    var showNotification = false

    fun createRunningNotification(time: String, isRunning: Boolean) {
        val stopIntent = Intent(context, NotificationIntentService::class.java)
        stopIntent.action = StopwatchFrag.STOP

        val openAppIntent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)!!
            .setPackage(null)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        val openAppPendingIntent = PendingIntent.getActivity(context, 3, openAppIntent, 0)

        val resetIntent = Intent(context, NotificationIntentService::class.java)
        resetIntent.action = StopwatchFrag.RESET

        val resumeIntent = Intent(context, NotificationIntentService::class.java)
        resumeIntent.action = StopwatchFrag.RESUME

        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.stopwatch_notification_channel_id)
        )
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setContentTitle("Stopwatch")
            .setContentText(time)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(openAppPendingIntent)
            .setShowWhen(false)
            .setColorized(true)
            .setColor(getColor(context,R.color.colorAccent))
            .setSound(null)

        // addAction requires an icon to support older devices, but isn't actually shown in newer ones
        if (isRunning) {
            builder.addAction(
                R.drawable.ic_pause,
                "Stop",
                PendingIntent.getService(
                    context,
                    0,
                    stopIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        } else {
            builder.addAction(
                R.drawable.ic_resume_arrow,
                "Resume",
                PendingIntent.getService(
                    context,
                    0,
                    resumeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            builder.setStyle(
                NotificationCompat.BigTextStyle().setSummaryText("Paused")
            )
        }

        builder.addAction(
            R.drawable.ic_stop,
            "Reset",
            PendingIntent.getService(
                context,
                1,
                resetIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        if (showNotification) {
            with(NotificationManagerCompat.from(context)) {
                //notificationId is a unique int for each notification that you must define
                notify(Constants.SWATCH_NOTIFICATION_ID, builder.build())
            }
        }
    }
}