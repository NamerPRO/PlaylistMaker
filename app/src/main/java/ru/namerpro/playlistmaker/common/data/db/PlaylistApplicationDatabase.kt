package ru.namerpro.playlistmaker.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.namerpro.playlistmaker.common.data.db.dao.TrackDao
import ru.namerpro.playlistmaker.common.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class PlaylistApplicationDatabase : RoomDatabase() {

    abstract fun getTrackDao(): TrackDao

}