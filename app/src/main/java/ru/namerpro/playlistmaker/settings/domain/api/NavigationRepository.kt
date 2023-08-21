package ru.namerpro.playlistmaker.settings.domain.api

import android.content.Intent
import ru.namerpro.playlistmaker.settings.domain.models.EmailModel

interface NavigationRepository {

    fun shareAppIntent(courseLink: String): Intent

    fun contactSupportIntent(concreteEmailModel: EmailModel): Intent

    fun licenseAgreementIntent(licenseLink: String): Intent

}