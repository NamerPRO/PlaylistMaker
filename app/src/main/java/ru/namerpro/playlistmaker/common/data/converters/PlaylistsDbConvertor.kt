package ru.namerpro.playlistmaker.common.data.converters

import ru.namerpro.playlistmaker.common.data.db.entity.PlaylistEntity
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel

class PlaylistsDbConvertor {

    fun map(
        playlist: PlaylistModel
    ): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.coverFull,
            playlist.coverCut,
            playlist.title,
            playlist.description,
            playlist.time,
            playlist.tracksIds
        )
    }

    fun map(
        playlist: PlaylistEntity
    ): PlaylistModel {
        return PlaylistModel(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.coverFull,
            playlist.coverCut,
            playlist.addTime,
            playlist.tracksIds
        )
    }

}