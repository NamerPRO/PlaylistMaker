package ru.namerpro.playlistmaker.main.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import ru.namerpro.playlistmaker.media.ui.activity.MediaActivity
import ru.namerpro.playlistmaker.creator.Creator
import ru.namerpro.playlistmaker.databinding.ActivityMainBinding
import ru.namerpro.playlistmaker.main.ui.view_model.MainViewModel
import ru.namerpro.playlistmaker.search.ui.activity.SearchActivity
import ru.namerpro.playlistmaker.settings.data.shared_preferences.SharedPreferencesSettingsRepositoryImpl
import ru.namerpro.playlistmaker.settings.ui.activity.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivitySearch: Button
    private lateinit var mainActivityMedia: Button
    private lateinit var mainActivitySettings: Button

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModel.getViewModelFactory(
            preferencesRepository = Creator.provideSettingsSharedPreferencesInteractor(applicationContext.getSharedPreferences(
                SharedPreferencesSettingsRepositoryImpl.SWITCH_POSITION_PREFERENCES,
                MODE_PRIVATE
            ))
        ))[MainViewModel::class.java]

        mainActivitySearch = binding.mainActivitySearch
        mainActivityMedia = binding.mainActivityMedia
        mainActivitySettings = binding.mainActivitySettings

        viewModel.observeTheme().observe(this) {
            AppCompatDelegate.setDefaultNightMode(
                if (it) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }

        mainActivitySearch.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SearchActivity::class.java
                )
            )
        }

        mainActivityMedia.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MediaActivity::class.java
                )
            )
        }

        mainActivitySettings.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        }
    }

}