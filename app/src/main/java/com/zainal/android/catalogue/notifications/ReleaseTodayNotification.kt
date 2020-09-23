package com.zainal.android.catalogue.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.zainal.android.catalogue.BuildConfig
import com.zainal.android.catalogue.R
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ReleaseTodayNotification : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 1200
        var CHANNEL_ID = 1300
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TITLE = "title"
        const val CHANNEL_NAME = "Daily Release"
        const val GROUP_KEYS_RELEASE = "group_key_release"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val title: String? = context.getString(R.string.daily_release)
        var message: String?

        val date = Date()
        val formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = formatDate.format(date)

        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=${BuildConfig.MovieDB_ApiKey}&primary_release_date.gte=$currentDate&primary_release_date.lte=$currentDate"
        client.get(url, object : AsyncHttpResponseHandler(){
            @SuppressLint("DefaultLocale")
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val resultData = responseObject.getJSONArray("results")
                    for (i in 0 until resultData.length()){
                        val data = resultData.getJSONObject(i)
                        CHANNEL_ID += i
                        val movieTitle = data.getString("title")
                        message = movieTitle.toUpperCase()

                        setNotificationRelease(context, title, message, CHANNEL_ID)
                    }
                }catch (e: Exception) {
                    Log.d("Exception: ", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure: ", error?.message.toString())
            }
        })
    }

    fun setNotificationRelease(context: Context, title: String?, message: String?, notificationId: Int){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, notificationId.toString())
            .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setGroup(GROUP_KEYS_RELEASE)
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationId.toString(), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            with(channel) {
                enableVibration(true)
                setShowBadge(true)
                canShowBadge()
                vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
                lockscreenVisibility = 1
            }
            notificationBuilder.setChannelId(notificationId.toString())
            notificationManager.createNotificationChannel(channel)
        }
        val notification = notificationBuilder.build()
        notificationManager.notify(notificationId, notification)

        Log.d("INFO -> CHANNEL ID : ", notificationId.toString())
    }

    fun setReleaseTodayAlarm(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            timeInMillis = System.currentTimeMillis()
        }

        val title: String? = context.getString(R.string.daily_release_notification)
        val message: String? = context.getString(R.string.daily_release_notification_message)
        val intent = Intent(context, ReleaseTodayNotification::class.java)
        intent.putExtra(EXTRA_TITLE, title)
        intent.putExtra(EXTRA_MESSAGE, message)
        val pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelReleaseTodayAlarm(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseTodayNotification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
}
