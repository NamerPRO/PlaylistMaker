package ru.namerpro.playlistmaker.main.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import ru.namerpro.playlistmaker.media.ui.activity.MediaActivity
import ru.namerpro.playlistmaker.databinding.ActivityMainBinding
import ru.namerpro.playlistmaker.main.ui.view_model.MainViewModel
import ru.namerpro.playlistmaker.search.ui.activity.SearchActivity
import ru.namerpro.playlistmaker.settings.ui.activity.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivitySearch: Button
    private lateinit var mainActivityMedia: Button
    private lateinit var mainActivitySettings: Button

    private val viewModel: MainViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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