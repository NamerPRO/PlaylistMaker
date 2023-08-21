package ru.namerpro.playlistmaker.settings.domain.impl

import ru.namerpro.playlistmaker.settings.domain.api.SharedPreferencesSettingsInteractor
import ru.namerpro.playlistmaker.settings.domain.api.SharedPreferencesSettingsRepository

class SharedPreferencesSettingsInteractorImpl(
    private val sharedPreferencesSettingsRepository: SharedPreferencesSettingsRepository
) : SharedPreferencesSettingsInteractor {

    override fun saveSwitchPosition(check: Boolean) {
        sharedPreferencesSettingsRepository.saveSwitchPosition(check)
    }

    override fun getSwitchPosition(): Boolean {
        return sharedPreferencesSettingsRepository.getSwitchPosition()
    }

}