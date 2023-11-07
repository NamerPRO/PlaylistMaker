package ru.namerpro.playlistmaker.media.domain.models

data class PlaylistModel(
    val title: String,
    val description: String?,
    val cover: String?,
    val time: Long,
    val tracksIds: ArrayList<Long>
)