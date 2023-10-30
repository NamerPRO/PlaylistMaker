package ru.namerpro.playlistmaker.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_table")
data class TrackEntity(

    @PrimaryKey
    val id: Long,
    val trackName: String?,
    val artistName: String,
    val previewUrl: String?,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: Long,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val favouriteAddTime: Long

)