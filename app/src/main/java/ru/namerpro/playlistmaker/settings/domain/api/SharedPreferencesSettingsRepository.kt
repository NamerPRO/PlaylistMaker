package ru.namerpro.playlistmaker.settings.domain.api

interface SharedPreferencesSettingsRepository {

    fun saveSwitchPosition(
        check: Boolean
    )

    fun getSwitchPosition(): Boolean

}