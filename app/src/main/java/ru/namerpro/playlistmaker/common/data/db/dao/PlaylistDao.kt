package ru.namerpro.playlistmaker.common.data.db.dao

import androidx.room.*
import ru.namerpro.playlistmaker.common.data.db.entity.PlaylistEntity
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

@Dao
interface PlaylistDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(
        playlist: PlaylistEntity
    )

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(
        playlist: PlaylistEntity
    )

    @Query("SELECT * FROM playlists_table ORDER BY addTime DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT count(*) FROM playlists_table")
    suspend fun getPlaylistsCount(): Int

    @Query("SELECT count(*) FROM playlists_table WHERE title LIKE :playlistTitle LIMIT 1")
    suspend fun hasPlaylist(
        playlistTitle: String
    ): Int

    @Query("UPDATE playlists_table SET tracksIds = :tracksInPlaylist WHERE title = :playlistTitle")
    suspend fun updateTrackIdsInPlaylist(
        playlistTitle: String,
        tracksInPlaylist: ArrayList<Long>
    )

    @Query("UPDATE playlists_table SET coverFull = :playlistCoverFull, coverCut = :playlistCoverCut, title = :playlistTitle, description = :playlistDescription, addTime = :playlistAddTime WHERE id = :playlistId")
    suspend fun updatePlaylist(
        playlistId: Long,
        playlistTitle: String,
        playlistDescription: String?,
        playlistCoverFull: String?,
        playlistCoverCut: String?,
        playlistAddTime: Long
    )

}