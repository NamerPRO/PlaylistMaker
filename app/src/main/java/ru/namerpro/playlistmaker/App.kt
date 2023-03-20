package ru.namerpro.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

const val SETTINGS_THEME_PREFERENCES = "settings_theme_preferences"
const val SWITCH_THEME_KEY = "switch_theme_key"

class App : Application() {

    var darkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(SETTINGS_THEME_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(SWITCH_THEME_KEY,
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)

        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}