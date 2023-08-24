package ru.namerpro.playlistmaker.main.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.namerpro.playlistmaker.settings.domain.api.SharedPreferencesSettingsInteractor
import ru.namerpro.playlistmaker.universal.domain.models.SingleLiveEvent

class MainViewModel(
    private val preferencesRepository: SharedPreferencesSettingsInteractor
) : ViewModel() {

    companion object {
        fun getViewModelFactory(
            preferencesRepository: SharedPreferencesSettingsInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(
                    preferencesRepository = preferencesRepository
                )
            }
        }
    }

    private val themeLiveData = SingleLiveEvent<Boolean>()
    fun observeTheme(): LiveData<Boolean> = themeLiveData

    init {
        themeLiveData.postValue(preferencesRepository.getSwitchPosition())
    }

}