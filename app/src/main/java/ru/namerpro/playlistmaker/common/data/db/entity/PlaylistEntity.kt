package ru.namerpro.playlistmaker.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val cover: String?,
    val title: String,
    val description: String?,
    val addTime: Long,
    val tracksIds: ArrayList<Long>
)
