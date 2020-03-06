package com.zainal.android.movie.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zainal.android.movie.R
import com.zainal.android.movie.util.setNotificationReminder
import java.util.*

class DailyReminderNotification : BroadcastReceiver() {

    companion object {
        const val REMINDER = 1
        const val CHANNEL_ID = 10
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TITLE = "title"
        const val CHANNEL_NAME = "Daily Reminder"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val title = intent.getStringExtra(EXTRA_TITLE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        setNotificationReminder(context, title, message, CHANNEL_ID, CHANNEL_NAME)
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
        val pendingIntent = PendingIntent.getBroadcast(context, REMINDER, intent,0)

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
        val pendingIntent = PendingIntent.getBroadcast(context, REMINDER, intent,0)
        alarmManager.cancel(pendingIntent)
    }
}
