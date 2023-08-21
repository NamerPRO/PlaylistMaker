package ru.namerpro.playlistmaker.settings.domain.impl

import android.content.Intent
import ru.namerpro.playlistmaker.settings.domain.api.NavigationInteractor
import ru.namerpro.playlistmaker.settings.domain.api.NavigationRepository
import ru.namerpro.playlistmaker.settings.domain.models.EmailModel

class NavigationInteractorImpl(
    private val navigationRepository: NavigationRepository
) : NavigationInteractor {

    override fun shareAppIntent(courseLink: String): Intent {
        return navigationRepository.shareAppIntent(courseLink)
    }

    override fun contactSupportIntent(concreteEmailModel: EmailModel): Intent {
        return navigationRepository.contactSupportIntent(concreteEmailModel)
    }

    override fun licenseAgreementIntent(licenseLink: String): Intent {
        return navigationRepository.licenseAgreementIntent(licenseLink)
    }

}