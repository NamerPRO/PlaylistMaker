package ru.namerpro.playlistmaker.search.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.namerpro.playlistmaker.search.data.dto.TracksSearchResponse

interface ItunesServiceApi {

    @GET("/search?entry=song")
    fun searchTracks(
        @Query("term") text: String
    ): Call<TracksSearchResponse>

}