package ru.namerpro.playlistmaker.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.namerpro.playlistmaker.common.data.db.dao.PlaylistDao
import ru.namerpro.playlistmaker.common.data.db.entity.PlaylistEntity
import ru.namerpro.playlistmaker.common.data.db.type_converters.Converters

@Database(version = 1, entities = [PlaylistEntity::class])
@TypeConverters(Converters::class)
abstract class PlaylistsDatabase : RoomDatabase() {

    abstract fun getPlaylistDao(): PlaylistDao

}