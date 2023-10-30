package ru.namerpro.playlistmaker.common.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.namerpro.playlistmaker.common.data.db.entity.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToTrackInPlaylistStorage(
        track: TrackInPlaylistEntity
    )

    @Query("SELECT count(*) FROM tracks_in_playlist_table WHERE id = :trackId LIMIT 1")
    suspend fun isInTrackInPlaylistStorage(
        trackId: Long
    ): Int

}