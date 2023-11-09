package ru.namerpro.playlistmaker.root.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.databinding.ActivityRootBinding
import ru.namerpro.playlistmaker.root.ui.view_model.RootViewModel

class RootActivity : AppCompatActivity() {

    private var binding: ActivityRootBinding? = null

    private val viewModel: RootViewModel by viewModel()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel.observeTheme().observe(this) {
            AppCompatDelegate.setDefaultNightMode(
                if (it) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playlistFragment,
                R.id.createPlaylistFragment,
                R.id.editPlaylistFragment,
                R.id.playerFragment -> {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    binding?.bottomNavigationViewTopBorder?.isVisible = false
                    binding?.bottomNavigationView?.isVisible = false
                }
                else -> {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                    binding?.bottomNavigationViewTopBorder?.isVisible = true
                    binding?.bottomNavigationView?.isVisible = true
                }
            }
        }

        binding?.bottomNavigationView?.setupWithNavController(navController)
    }

}