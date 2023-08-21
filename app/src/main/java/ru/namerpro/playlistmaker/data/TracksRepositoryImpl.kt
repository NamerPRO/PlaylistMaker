package ru.namerpro.playlistmaker.data

import ru.namerpro.playlistmaker.data.dto.TracksSearchRequest
import ru.namerpro.playlistmaker.data.dto.TracksSearchResponse
import ru.namerpro.playlistmaker.domain.api.TracksRepository
import ru.namerpro.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(trackName: String): Pair<List<Track>, Int> {
        val response = networkClient.doRequest(TracksSearchRequest(trackName))
        return if (response.resultCode == 200) {
            Pair(
                (response as TracksSearchResponse).results.map {
                Track(
                    it.trackName ?: "",
                    it.artistName ?: "",
                    it.previewUrl ?: "",
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                    it.artworkUrl100 ?: "",
                    it.trackId ?: -1,
                    it.collectionName ?: "",
                    it.releaseDate ?: "",
                    it.primaryGenreName ?: "",
                    it.country ?: ""
                )},
                response.resultCode
            )
        } else {
            Pair(emptyList(), response.resultCode)
        }
    }

}