package ru.namerpro.playlistmaker.settings.domain.api

interface SharedPreferencesSettingsInteractor {

    fun saveSwitchPosition(
        check: Boolean
    )

    fun getSwitchPosition(): Boolean

}