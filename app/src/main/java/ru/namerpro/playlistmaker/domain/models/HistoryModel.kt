package ru.namerpro.playlistmaker.domain.models

class HistoryModel {

    companion object {
        val trackHistory = mutableListOf<Track>()
        const val maximumElementsInHistory = 10
    }

    fun isHistoryEmpty() : Boolean {
        return trackHistory.isEmpty()
    }

    fun addTrack(track: Track) {
        val elementToDelete = trackHistory.find { it.trackId == track.trackId }
        if (elementToDelete != null) {
            trackHistory.remove(elementToDelete)
        } else if (trackHistory.size == maximumElementsInHistory) {
            trackHistory.removeLast()
        }
        trackHistory.add(0, track)
    }

    fun clearHistory() {
        trackHistory.clear()
    }

}