package ru.namerpro.playlistmaker.settings.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.databinding.FragmentSettingsBinding
import ru.namerpro.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsFragment : Fragment() {

    private lateinit var settingsDayNightToggleView: SwitchCompat
    private lateinit var shareAppButtonView: TextView
    private lateinit var contactSupportButtonView: TextView
    private lateinit var licenseAgreementButtonView: TextView

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        settingsDayNightToggleView = binding.toggleDayNightMode
        shareAppButtonView = binding.settingsShareAppButton
        contactSupportButtonView = binding.settingsContactSupportButton
        licenseAgreementButtonView = binding.settingsLicenseAgreement

        settingsDayNightToggleView.setOnCheckedChangeListener { _, isEnabled ->
            viewModel.switchTheme(isEnabled)
        }
        settingsDayNightToggleView.isChecked = viewModel.isThemeDark

        viewModel.observeSwitchToggleState().observe(viewLifecycleOwner) {
            settingsDayNightToggleView.isChecked = it
        }

        viewModel.observeIntent().observe(viewLifecycleOwner) {
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