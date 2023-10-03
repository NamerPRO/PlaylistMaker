package ru.namerpro.playlistmaker.settings.ui.view_model

import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.namerpro.playlistmaker.utils.SingleLiveEvent
import ru.namerpro.playlistmaker.settings.domain.api.SharedPreferencesSettingsInteractor
import ru.namerpro.playlistmaker.settings.domain.api.NavigationInteractor
import ru.namerpro.playlistmaker.settings.domain.models.EmailModel

class SettingsViewModel(
    private val preferencesInteractor: SharedPreferencesSettingsInteractor,
    private val navigationInteractor: NavigationInteractor
) : ViewModel() {

    var isThemeDark: Boolean = preferencesInteractor.getSwitchPosition()

    private val switchToggleLiveData = SingleLiveEvent<Boolean>()
    fun observeSwitchToggleState(): LiveData<Boolean> = switchToggleLiveData

    private val intentLiveData = SingleLiveEvent<Intent>()
    fun observeIntent(): LiveData<Intent> = intentLiveData

    fun shareApp(
        courseLink: String
    ) {
        val shareIntent = navigationInteractor.shareAppIntent(
            courseLink = courseLink
        )
        intentLiveData.postValue(shareIntent)
    }

    fun contactSupport(
        contactEmail: String,
        extraSubject: String,
        extraText: String
    ) {
        val contactSupportIntent = navigationInteractor.contactSupportIntent(
            concreteEmailModel = EmailModel(
                contactEmail = contactEmail,
                extraSubject = extraSubject,
                extraText = extraText
            )
        )
        intentLiveData.postValue(contactSupportIntent)
    }

    fun licenseAgreement(
        licenseLink: String
    ) {
        val licenseIntent = navigationInteractor.licenseAgreementIntent(
            licenseLink = licenseLink
        )
        intentLiveData.postValue(licenseIntent)
    }

    fun switchTheme(
        isEnabled: Boolean
    ) {
        isThemeDark = isEnabled
        preferencesInteractor.saveSwitchPosition(isThemeDark)
        AppCompatDelegate.setDefaultNightMode(
            if (isThemeDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        switchToggleLiveData.postValue(isThemeDark)
    }

}