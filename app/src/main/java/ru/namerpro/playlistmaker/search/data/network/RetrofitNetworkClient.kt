package ru.namerpro.playlistmaker.search.data.network

import ru.namerpro.playlistmaker.search.data.dto.Response
import ru.namerpro.playlistmaker.search.data.dto.TracksSearchRequest
import java.io.IOException

class RetrofitNetworkClient(
    private val itunesService: ItunesServiceApi
) : NetworkClient {

    override fun doRequest(
        dto: Any
    ): Response {
        return if (dto is TracksSearchRequest) {
            try {
                val response = itunesService.searchTracks(dto.trackName).execute()
                val body = response.body() ?: Response()
                body.apply { resultCode = response.code() }
            } catch (ce: IOException) {
                Response().apply { resultCode = 503 }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }

}