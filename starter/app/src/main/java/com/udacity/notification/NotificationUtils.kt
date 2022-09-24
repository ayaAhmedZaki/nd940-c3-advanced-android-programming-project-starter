package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import com.udacity.MainActivity.Companion.STATUS


private val NOTIFICATION_ID = 0
private val CHANNEL_ID = "NOTIFICATION_ID"

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(
    messageBody: String,
    applicationContext: Context,
    status: String,
    title: String
) {
//    // Create an Intent for the activity you want to start
//    val resultIntent = Intent(applicationContext, DetailActivity::class.java)
//    // Create the TaskStackBuilder
//    val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(applicationContext).run {
//        // Add the intent, which inflates the back stack
//        addNextIntentWithParentStack(resultIntent)
//        // Get the PendingIntent containing the entire back stack
//        getPendingIntent(0,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//    }

    Log.d("NOTIFICATION__NOTIFY" , "$status")

    val notifyIntent = Intent(applicationContext, DetailActivity::class.java).apply {
        action = Intent.ACTION_MAIN
        putExtra("FileStatus" , status)
        putExtra("FileTitle" , title)
        addCategory(Intent.CATEGORY_LAUNCHER)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK

    }
    val notifyPendingIntent = PendingIntent.getActivity(
        applicationContext, 0, notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

//    val contentIntent = Intent(applicationContext, MainActivity::class.java)
//    val contentPendingIntent = PendingIntent.getActivity(
//        applicationContext,
//        NOTIFICATION_ID,
//        contentIntent,
//        PendingIntent.FLAG_UPDATE_CURRENT
//    )
    // TODO: Step 2.0 add style
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.download
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        .bigLargeIcon(null)

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    )
        .setSmallIcon(R.drawable.download)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentText(messageBody)
        .setContentIntent(notifyPendingIntent)
        .setAutoCancel(true)// so that when the user taps on the notification,
        .setStyle(bigPicStyle)
        .setLargeIcon(eggImage)
        .addAction(
            R.drawable.download,
            applicationContext.getString(R.string.notification_button),
            notifyPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}
