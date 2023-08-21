package ru.namerpro.playlistmaker.settings.data.shared_preferences

import android.content.SharedPreferences
import android.content.res.Configuration
import ru.namerpro.playlistmaker.settings.domain.api.SharedPreferencesSettingsRepository

class SharedPreferencesSettingsRepositoryImpl(
    private val sharedPrefs: SharedPreferences
) : SharedPreferencesSettingsRepository {

    companion object {
        const val SWITCH_POSITION_PREFERENCES = "switch_position_preferences"
        private const val SWITCH_THEME_KEY = "switch_theme_key"
    }

    override fun saveSwitchPosition(
        check: Boolean
    ) {
        sharedPrefs.edit()
            .putBoolean(
                SWITCH_THEME_KEY,
                check
            )
            .apply()
    }

    override fun getSwitchPosition(): Boolean {
        return sharedPrefs.getBoolean(
            SWITCH_THEME_KEY,
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        )
    }

}