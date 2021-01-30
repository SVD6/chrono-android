package ca.chronofit.chrono.util.notificationManager

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ca.chronofit.chrono.R
import ca.chronofit.chrono.stopwatch.StopwatchFrag
import ca.chronofit.chrono.util.services.NotificationIntentService

class StopwatchNotificationManager(private val context: Context) {
    val notificationId = 12
    var showNotification = false


    fun createRunningNotification(time: String) {
        val customView = RemoteViews(context.packageName, R.layout.notification_stopwatch_running)

        val stopIntent = Intent(context, NotificationIntentService::class.java)
        stopIntent.action = StopwatchFrag.STOP
        customView.setOnClickPendingIntent(
            R.id.stop_stopwatch,
            PendingIntent.getService(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        )

        val resetIntent = Intent(context, NotificationIntentService::class.java)
        resetIntent.action = StopwatchFrag.RESET
        customView.setOnClickPendingIntent(
            R.id.reset_stopwatch,
            PendingIntent.getService(context, 1, resetIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        )

        customView.setTextViewText(R.id.elapsed_time, time)

        val builder =
            NotificationCompat.Builder(
                context,
                context.getString(R.string.stopwatch_notification_channel_id)
            )
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentTitle("Stopwatch")
                .setContentText(time)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(customView)

        if (showNotification) {
            with(NotificationManagerCompat.from(context)) {
                //notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }
        }
    }

    fun createStoppedNotification(time: String) {
        val customView = RemoteViews(context.packageName, R.layout.notification_stopwatch_stopped)

        val resumeIntent = Intent(context, NotificationIntentService::class.java)
        resumeIntent.action = StopwatchFrag.RESUME
        customView.setOnClickPendingIntent(
            R.id.resume_stopwatch,
            PendingIntent.getService(context, 0, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        )

        val resetIntent = Intent(context, NotificationIntentService::class.java)
        resetIntent.action = StopwatchFrag.RESET
        customView.setOnClickPendingIntent(
            R.id.reset_stopwatch,
            PendingIntent.getService(
                context,
                notificationId,
                resetIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        customView.setTextViewText(R.id.elapsed_time, time)

        val builder =
            NotificationCompat.Builder(
                context,
                context.getString(R.string.stopwatch_notification_channel_id)
            )
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentTitle("Stopwatch")
                .setContentText(time)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(customView)

        if (showNotification) {
            with(NotificationManagerCompat.from(context)) {
                //notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }
        }
    }
}