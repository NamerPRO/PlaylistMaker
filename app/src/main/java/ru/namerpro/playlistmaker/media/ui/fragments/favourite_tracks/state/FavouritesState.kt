package ru.namerpro.playlistmaker.media.ui.fragments.favourite_tracks.state

import ru.namerpro.playlistmaker.search.domain.model.TrackModel

sealed interface FavouritesState {

    object Empty : FavouritesState

    data class Filled(
        val favouriteTracksList: List<TrackModel>
    ) : FavouritesState

}