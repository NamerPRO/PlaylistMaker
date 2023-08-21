package ru.namerpro.playlistmaker.data.dto

data class TracksDto(
    val trackName: String?,
    val artistName: String,
    val previewUrl: String?,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: Long,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
)