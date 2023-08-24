package ru.namerpro.playlistmaker.search.data.network

import ru.namerpro.playlistmaker.search.data.dto.TracksSearchRequest
import ru.namerpro.playlistmaker.search.data.dto.TracksSearchResponse
import ru.namerpro.playlistmaker.search.domain.api.TracksRepository
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import java.text.SimpleDateFormat
import java.util.*

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(trackName: String): Pair<List<TrackModel>, Int> {
        val response = networkClient.doRequest(TracksSearchRequest(trackName))
        return if (response.resultCode == 200) {
            Pair(
                (response as TracksSearchResponse).results.map {
                TrackModel(
                    it.trackName ?: "",
                    it.artistName,
                    it.previewUrl ?: "",
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                    it.artworkUrl100,
                    it.trackId,
                    it.collectionName ?: "",
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country
                )
                },
                response.resultCode
            )
        } else {
            Pair(emptyList(), response.resultCode)
        }
    }

}