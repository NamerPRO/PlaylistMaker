package ru.namerpro.playlistmaker.search.domain.impl

import ru.namerpro.playlistmaker.search.domain.api.SearchHistoryInteractor
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class SearchHistoryInteractorImpl(
    private val trackHistory: ArrayList<TrackModel>
) : SearchHistoryInteractor {

    override fun isHistoryEmpty(): Boolean {
        return trackHistory.isEmpty()
    }

    override fun addTrack(
        track: TrackModel
    ) {
        val elementToDelete = trackHistory.find { it.trackId == track.trackId }
        if (elementToDelete != null) {
            trackHistory.remove(elementToDelete)
        } else if (trackHistory.size >= maximumElementsInHistory) {
            trackHistory.removeLast()
        }
        trackHistory.add(0, track)
    }

    override fun clearHistory() {
        trackHistory.clear()
    }

    override fun getHistory(): ArrayList<TrackModel> {
        return trackHistory
    }

    companion object {
        const val maximumElementsInHistory = 10
    }

}