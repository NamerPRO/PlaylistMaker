package ru.namerpro.playlistmaker.search.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.namerpro.playlistmaker.common.data.db.PlaylistApplicationDatabase
import ru.namerpro.playlistmaker.search.data.dto.TracksSearchRequest
import ru.namerpro.playlistmaker.search.data.dto.TracksSearchResponse
import ru.namerpro.playlistmaker.search.domain.api.TracksRepository
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import java.text.SimpleDateFormat
import java.util.*

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val playlistApplicationDatabase: PlaylistApplicationDatabase
) : TracksRepository {

    override fun searchTracks(
        trackName: String
    ): Flow<Pair<List<TrackModel>, Int>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(trackName))
        if (response.resultCode == 200) {
            emit(Pair(
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
            ))
        } else {
            emit(Pair(emptyList(), response.resultCode))
        }
    }

}