package ru.namerpro.playlistmaker.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.namerpro.playlistmaker.common.data.db.dao.TrackInPlaylistDao
import ru.namerpro.playlistmaker.common.data.db.entity.TrackInPlaylistEntity

@Database(version = 1, entities = [TrackInPlaylistEntity::class])
abstract class TracksInPlaylistDatabase : RoomDatabase() {

    abstract fun getTracksInPlaylistDao(): TrackInPlaylistDao

}