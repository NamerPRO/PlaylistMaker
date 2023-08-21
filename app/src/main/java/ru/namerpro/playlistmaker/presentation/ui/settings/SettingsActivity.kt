package ru.namerpro.playlistmaker.presentation.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import ru.namerpro.playlistmaker.App
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.SETTINGS_THEME_PREFERENCES
import ru.namerpro.playlistmaker.SWITCH_THEME_KEY

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingsBack = findViewById<ImageView>(R.id.settings_back)
        settingsBack.setOnClickListener {
            finish()
        }

        val settingsDayNightToggle = findViewById<SwitchCompat>(R.id.toggle_day_night_mode)
        settingsDayNightToggle.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
        settingsDayNightToggle.isChecked = (applicationContext as App).darkTheme

        val shareAppButton = findViewById<TextView>(R.id.settings_share_app_button)
        shareAppButton.setOnClickListener {
            val shareIntent = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_course_link))
                type = "text/plain"
            }, null)
            startActivity(shareIntent)
        }

        val contactSupportButton = findViewById<TextView>(R.id.settings_contact_support_button)
        contactSupportButton.setOnClickListener {
            val contactIntent = Intent().apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.settings_contact_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_extra_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_extra_text))
            }
            startActivity(contactIntent)
        }

        val licenseAgreementButton = findViewById<TextView>(R.id.settings_license_agreement)
        licenseAgreementButton.setOnClickListener {
            val licenseIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.license_agreement))
            }
            startActivity(licenseIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPrefs = getSharedPreferences(SETTINGS_THEME_PREFERENCES, MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean(
                SWITCH_THEME_KEY, (applicationContext as App)
            .darkTheme).apply()
    }
}