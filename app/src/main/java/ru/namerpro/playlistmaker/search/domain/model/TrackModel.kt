package ru.namerpro.playlistmaker.search.domain.model

data class TrackModel(
    val trackName: String,
    val artistName: String,
    val previewUrl: String,
    val trackTimeInFormat: String,
    val artworkUrl100: String,
    var trackId: Long,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
)
