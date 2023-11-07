package ru.namerpro.playlistmaker.media.domain.models

data class PlaylistModel(
    val id: Long,
    val title: String,
    val description: String?,
    val coverFull: String?,
    val coverCut: String?,
    val time: Long,
    val tracksIds: ArrayList<Long>
)