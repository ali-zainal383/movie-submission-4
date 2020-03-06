package com.zainal.android.movie.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.zainal.android.movie.R
import java.util.*

class DailyReminderNotification : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 1000
        const val CHANNEL_ID = "1100"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TITLE = "title"
        const val CHANNEL_NAME = "Daily Reminder"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val title = intent.getStringExtra(EXTRA_TITLE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        setNotificationReminder(context, title, message, NOTIFICATION_ID)
    }

    private fun setNotificationReminder(context: Context, title: String?, message: String?, notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            with(channel) {
                enableVibration(true)
                setShowBadge(true)
                canShowBadge()
                vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 100)
                lockscreenVisibility = 1
            }
            notificationBuilder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = notificationBuilder.build()
        notificationManager.notify(notificationId, notification)
    }

    fun setDailyReminderAlarm(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 7)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            timeInMillis = System.currentTimeMillis()
        }

        val title : String? = context.getString(R.string.daily_reminder_notification)
        val message: String? = context.getString(R.string.daily_reminder_notification_message)
        val intent = Intent(context, DailyReminderNotification::class.java)
        intent.putExtra(EXTRA_TITLE, title)
        intent.putExtra(EXTRA_MESSAGE, message)
        val pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent,0)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelDailyReminderAlarm(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminderNotification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent,0)
        alarmManager.cancel(pendingIntent)
    }
}
