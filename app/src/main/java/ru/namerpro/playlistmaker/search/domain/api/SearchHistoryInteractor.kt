package ru.namerpro.playlistmaker.search.domain.api

import ru.namerpro.playlistmaker.search.domain.model.TrackModel

interface SearchHistoryInteractor {

    fun isHistoryEmpty(): Boolean

    fun addTrack(
        track: TrackModel
    )

    fun clearHistory()

    fun getHistory(): ArrayList<TrackModel>

}