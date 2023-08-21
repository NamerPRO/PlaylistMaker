package ru.namerpro.playlistmaker.search.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.namerpro.playlistmaker.search.data.dto.Response
import ru.namerpro.playlistmaker.search.data.dto.TracksSearchRequest
import java.io.IOException

class RetrofitNetworkClient : NetworkClient {

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesServiceApi::class.java)

    override fun doRequest(dto: Any): Response {
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