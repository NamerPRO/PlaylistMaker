package ru.namerpro.playlistmaker.common.data.db.dao

import androidx.room.*
import ru.namerpro.playlistmaker.common.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(
        track: TrackEntity
    )

    @Query("SELECT * FROM tracks_table ORDER BY favouriteAddTime DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(
        trackEntity: TrackEntity
    )

    @Query("SELECT id FROM tracks_table")
    suspend fun getIds(): List<Long>

}