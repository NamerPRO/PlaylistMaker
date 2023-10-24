package ru.namerpro.playlistmaker.search.data.network

import ru.namerpro.playlistmaker.search.data.dto.Response

interface NetworkClient {

    suspend fun doRequest(
        dto: Any
    ): Response

}