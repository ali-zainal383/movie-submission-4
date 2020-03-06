package com.zainal.android.movie.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.zainal.android.movie.R

fun setNotificationReminder(context: Context, title: String?, message: String?, channelId: Int, channelName: String?) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(context, channelId.toString())
        .setSmallIcon(R.drawable.ic_notifications_white_24dp)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setColor(ContextCompat.getColor(context, android.R.color.transparent))
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        .setSound(alarmSound)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId.toString(), channelName, NotificationManager.IMPORTANCE_DEFAULT
        )
        with(channel) {
            enableVibration(true)
            setShowBadge(true)
            canShowBadge()
            vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 100)
            lockscreenVisibility = 1
        }
        notificationBuilder.setChannelId(channelId.toString())
        notificationManager.createNotificationChannel(channel)
    }

    val notification = notificationBuilder.build()
    notificationManager.notify(channelId, notification)
}

fun setNotificationRelease(context: Context, title: String?, message: String?, channelId: Int, channelName: String?, group: String?){
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(context, channelId.toString())
        .setSmallIcon(R.drawable.ic_notifications_white_24dp)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setColor(ContextCompat.getColor(context, android.R.color.transparent))
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        .setSound(alarmSound)
        .setGroup(group)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId.toString(), channelName, NotificationManager.IMPORTANCE_DEFAULT
        )
        with(channel) {
            enableVibration(true)
            setShowBadge(true)
            canShowBadge()
            vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            lockscreenVisibility = 1
        }
        notificationBuilder.setChannelId(channelId.toString())
        notificationManager.createNotificationChannel(channel)
    }
    val notification = notificationBuilder.build()
    notificationManager.notify(channelId, notification)
}