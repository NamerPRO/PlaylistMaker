package ru.namerpro.playlistmaker.search.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.namerpro.playlistmaker.search.data.dto.Response
import ru.namerpro.playlistmaker.search.data.dto.TracksSearchRequest
import java.io.IOException

class RetrofitNetworkClient(
    private val itunesService: ItunesServiceApi
) : NetworkClient {

    override suspend fun doRequest(
        dto: Any
    ): Response {
        return withContext(Dispatchers.IO) {
            if (dto is TracksSearchRequest) {
                try {
                    val response = itunesService.searchTracks(dto.trackName)
                    response.apply { resultCode = 200 }
                } catch (e: Throwable) {
                    Response().apply { resultCode = 503 }
                }
            } else {
                Response().apply { resultCode = 400 }
            }
        }
    }

}