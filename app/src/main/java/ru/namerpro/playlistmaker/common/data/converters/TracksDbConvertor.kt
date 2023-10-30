package ru.namerpro.playlistmaker.common.data.converters

import ru.namerpro.playlistmaker.common.data.db.entity.TrackEntity
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import java.text.SimpleDateFormat
import java.util.*

class TracksDbConvertor {

    fun map(
        track: TrackModel,
        trackAddTime: Long
    ): TrackEntity {
        return TrackEntity(
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
        track: TrackEntity
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