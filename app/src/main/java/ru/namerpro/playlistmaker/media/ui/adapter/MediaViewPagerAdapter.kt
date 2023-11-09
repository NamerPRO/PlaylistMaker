package ru.namerpro.playlistmaker.media.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.namerpro.playlistmaker.media.ui.fragments.favourite_tracks.FavouriteTracksFragment
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.PlaylistsFragment

class MediaViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(
        position: Int
    ): Fragment {
        return when (position) {
            0 -> FavouriteTracksFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }

}