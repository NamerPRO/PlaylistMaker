package ru.namerpro.playlistmaker.domain.models

data class Track(
    val trackName: String,
    val artistName: String,
    val previewUrl: String,
    val trackTimeInFormat: String,
    val artworkUrl100: String,
    val trackId: Long,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
)
