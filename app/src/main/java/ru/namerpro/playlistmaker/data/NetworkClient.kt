package ru.namerpro.playlistmaker.data

import ru.namerpro.playlistmaker.data.dto.Response

interface NetworkClient {

    fun doRequest(dto: Any): Response

}