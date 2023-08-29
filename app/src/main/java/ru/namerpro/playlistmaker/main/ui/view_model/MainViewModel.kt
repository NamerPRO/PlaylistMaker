package ru.namerpro.playlistmaker.main.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.namerpro.playlistmaker.settings.domain.api.SharedPreferencesSettingsInteractor
import ru.namerpro.playlistmaker.universal.domain.models.SingleLiveEvent

class MainViewModel(
    preferencesInteractor: SharedPreferencesSettingsInteractor
) : ViewModel() {

    private val themeLiveData = SingleLiveEvent<Boolean>()
    fun observeTheme(): LiveData<Boolean> = themeLiveData

    init {
        themeLiveData.postValue(preferencesInteractor.getSwitchPosition())
    }

}