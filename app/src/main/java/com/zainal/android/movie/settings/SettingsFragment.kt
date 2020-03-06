package com.zainal.android.movie.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.zainal.android.movie.R
import com.zainal.android.movie.notifications.DailyReminderNotification

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val dailyReminderNotification = DailyReminderNotification()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val changeLanguage = findPreference<Preference>("change_language")
        changeLanguage?.intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when(key) {
            "daily_reminder" -> {
                when(sharedPreferences?.getBoolean(key, false)){
                    true -> dailyReminderNotification.setDailyReminderAlarm(context)
                    false -> dailyReminderNotification.cancelDailyReminderAlarm(context)
                }
            }
        }
    }
}
