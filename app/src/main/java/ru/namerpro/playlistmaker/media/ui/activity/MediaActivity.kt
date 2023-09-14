package ru.namerpro.playlistmaker.media.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.databinding.ActivityMediaBinding
import ru.namerpro.playlistmaker.media.ui.adapter.MediaViewPagerAdapter

class MediaActivity : AppCompatActivity() {

    private lateinit var tabMediator: TabLayoutMediator

    private lateinit var binding: ActivityMediaBinding

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.mediaTablayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.media_favourite_tracks)
                1 -> tab.text = getString(R.string.media_playlists)
            }
        }

        tabMediator.attach()

        binding.mediaBack.setOnClickListener {
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}
