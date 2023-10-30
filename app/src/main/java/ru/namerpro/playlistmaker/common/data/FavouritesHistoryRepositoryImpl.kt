package ru.namerpro.playlistmaker.common.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.namerpro.playlistmaker.common.data.converters.TracksDbConvertor
import ru.namerpro.playlistmaker.common.data.db.PlaylistApplicationDatabase
import ru.namerpro.playlistmaker.common.data.db.entity.TrackEntity
import ru.namerpro.playlistmaker.common.domain.api.FavouritesHistoryRepository
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class FavouritesHistoryRepositoryImpl(
    private val playlistApplicationDatabase: PlaylistApplicationDatabase,
    private val tracksDbConvertor: TracksDbConvertor
) : FavouritesHistoryRepository {

    override fun getFavourites(): Flow<List<TrackModel>> = flow {
        val tracks = playlistApplicationDatabase.getTrackDao().getTracks()
        emit(convertFromTracksEntity(tracks))
    }

    override suspend fun isTrackFavourite(id: Long): Boolean {
        val ids = playlistApplicationDatabase.getTrackDao().getIds()
        return ids.contains(id)
    }

    override suspend fun deleteFromFavourites(trackEntity: TrackEntity) {
        playlistApplicationDatabase.getTrackDao().deleteTrack(trackEntity)
    }

    override suspend fun addToFavourites(trackEntity: TrackEntity) {
        playlistApplicationDatabase.getTrackDao().insertTrack(trackEntity)
    }

    private fun convertFromTracksEntity(
        tracks: List<TrackEntity>
    ): List<TrackModel> {
        return tracks.map { track -> tracksDbConvertor.map(track) }
    }

}