package ru.namerpro.playlistmaker.common.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.namerpro.playlistmaker.common.data.converters.TracksDbConvertor
import ru.namerpro.playlistmaker.common.data.db.FavouriteTracksDatabase
import ru.namerpro.playlistmaker.common.data.db.entity.TrackEntity
import ru.namerpro.playlistmaker.common.domain.api.FavouritesDatabaseRepository
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class FavouritesDatabaseRepositoryImpl(
    private val favouriteTracksDatabase: FavouriteTracksDatabase,
    private val tracksDbConvertor: TracksDbConvertor
) : FavouritesDatabaseRepository {

    override fun getFavourites(): Flow<List<TrackModel>> = flow {
        val tracks = favouriteTracksDatabase.getTrackDao().getTracks()
        emit(convertFromTracksEntity(tracks))
    }

    override suspend fun isTrackFavourite(
        id: Long
    ): Boolean {
        val ids = favouriteTracksDatabase.getTrackDao().getIds()
        return ids.contains(id)
    }

    override suspend fun deleteFromFavourites(
        track: TrackModel
    ) {
        favouriteTracksDatabase.getTrackDao().deleteTrack(tracksDbConvertor.map(track, 0))
    }

    override suspend fun addToFavourites(
        track: TrackModel,
        favouriteAddTime: Long
    ) {
        favouriteTracksDatabase.getTrackDao().insertTrack(tracksDbConvertor.map(track, favouriteAddTime))
    }

    private fun convertFromTracksEntity(
        tracks: List<TrackEntity>
    ): List<TrackModel> {
        return tracks.map { track -> tracksDbConvertor.map(track) }
    }

}