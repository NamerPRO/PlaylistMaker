package ru.namerpro.playlistmaker.common.data.converters

import ru.namerpro.playlistmaker.common.data.db.entity.TrackInPlaylistEntity
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import java.text.SimpleDateFormat
import java.util.*

class TrackInPlaylistDbConvertor {

    fun map(
        track: TrackModel,
        trackAddTime: Long
    ): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.previewUrl,
            SimpleDateFormat("mm:ss", Locale.getDefault()).parse(track.trackTimeInFormat)!!.time,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            trackAddTime
        )
    }

    fun map(
        track: TrackInPlaylistEntity
    ): TrackModel {
        return TrackModel(
            track.trackName ?: "",
            track.artistName,
            track.previewUrl ?: "",
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis),
            track.artworkUrl100,
            track.trackId,
            track.collectionName ?: "",
            track.releaseDate,
            track.primaryGenreName,
            track.country
        )
    }

}