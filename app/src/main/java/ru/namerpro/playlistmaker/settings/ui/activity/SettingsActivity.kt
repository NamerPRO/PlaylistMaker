package ru.namerpro.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.databinding.ActivitySettingsBinding
import ru.namerpro.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsBackView: ImageView
    private lateinit var settingsDayNightToggleView: SwitchCompat
    private lateinit var shareAppButtonView: TextView
    private lateinit var contactSupportButtonView: TextView
    private lateinit var licenseAgreementButtonView: TextView

    private val viewModel: SettingsViewModel by viewModel()

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsBackView = binding.settingsBack
        settingsDayNightToggleView = binding.toggleDayNightMode
        shareAppButtonView = binding.settingsShareAppButton
        contactSupportButtonView = binding.settingsContactSupportButton
        licenseAgreementButtonView = binding.settingsLicenseAgreement

        settingsBackView.setOnClickListener {
            finish()
        }

        settingsDayNightToggleView.setOnCheckedChangeListener { _, isEnabled ->
            viewModel.switchTheme(isEnabled)
        }
        settingsDayNightToggleView.isChecked = viewModel.isThemeDark

        viewModel.observeSwitchToggleState().observe(this) {
            settingsDayNightToggleView.isChecked = it
        }

        viewModel.observeIntent().observe(this) {
            startActivity(it)
        }

        shareAppButtonView.setOnClickListener {
            viewModel.shareApp(getString(R.string.share_course_link))
        }

        contactSupportButtonView.setOnClickListener {
            viewModel.contactSupport(
                contactEmail = getString(R.string.settings_contact_email),
                extraSubject = getString(R.string.settings_extra_subject),
                extraText = getString(R.string.settings_extra_text)
            )
        }

        licenseAgreementButtonView.setOnClickListener {
            viewModel.licenseAgreement(getString(R.string.license_agreement))
        }
    }

}