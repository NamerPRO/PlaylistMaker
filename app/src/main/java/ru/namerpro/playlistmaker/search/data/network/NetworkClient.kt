package ru.namerpro.playlistmaker.search.data.network

import ru.namerpro.playlistmaker.search.data.dto.Response

interface NetworkClient {

    fun doRequest(
        dto: Any
    ): Response

}