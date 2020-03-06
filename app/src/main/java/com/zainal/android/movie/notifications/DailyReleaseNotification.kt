package com.zainal.android.movie.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.zainal.android.movie.BuildConfig
import com.zainal.android.movie.R
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class DailyReleaseNotification : BroadcastReceiver() {

    companion object {
        const val RELEASE = 2
        var CHANNEL_ID = 20
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TITLE = "title"
        const val CHANNEL_NAME = "Daily Release"
        const val GROUP_KEYS_RELEASE = "group_key_release"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
    }
}
