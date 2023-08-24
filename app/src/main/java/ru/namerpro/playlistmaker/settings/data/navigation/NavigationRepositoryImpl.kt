package ru.namerpro.playlistmaker.settings.data.navigation

import android.content.Intent
import android.net.Uri
import ru.namerpro.playlistmaker.settings.domain.api.NavigationRepository
import ru.namerpro.playlistmaker.settings.domain.models.EmailModel

class NavigationRepositoryImpl : NavigationRepository {

    override fun shareAppIntent(
        courseLink: String
    ): Intent {
        return Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, courseLink)
            type = "text/plain"
        }, null)
    }

    override fun contactSupportIntent(
        concreteEmailModel: EmailModel
    ): Intent {
        return Intent().apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(concreteEmailModel.contactEmail))
            putExtra(Intent.EXTRA_SUBJECT, concreteEmailModel.extraSubject)
            putExtra(Intent.EXTRA_TEXT, concreteEmailModel.extraText)
        }
    }

    override fun licenseAgreementIntent(
        licenseLink: String
    ): Intent {
        return Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(licenseLink)
        }
    }

}