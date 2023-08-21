package ru.namerpro.playlistmaker.search.ui.activity.state

import ru.namerpro.playlistmaker.search.domain.model.TrackModel

sealed interface SearchRenderState {

    object Loading: SearchRenderState

    object NoInternet: SearchRenderState

    object NothingFound: SearchRenderState

    data class Success(
        val tracks: List<TrackModel>
    ) : SearchRenderState

    object History : SearchRenderState

    object Empty: SearchRenderState

    data class Search(
        val showClearButton: Boolean,
        val showSearchHistory: Boolean
    ) : SearchRenderState

}