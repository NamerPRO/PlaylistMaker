package ru.namerpro.playlistmaker.domain.api

import ru.namerpro.playlistmaker.domain.models.Track

interface TracksRepository {

    fun searchTracks(trackName: String): Pair<List<Track>, Int>

}