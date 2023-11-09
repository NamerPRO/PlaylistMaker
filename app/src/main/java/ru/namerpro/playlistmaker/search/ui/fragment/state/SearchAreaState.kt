package ru.namerpro.playlistmaker.search.ui.fragment.state

sealed interface SearchAreaState {

    data class Text(
        val text: String
    ) : SearchAreaState

    data class Focus(
        val isFocused: Boolean
    ) : SearchAreaState

}