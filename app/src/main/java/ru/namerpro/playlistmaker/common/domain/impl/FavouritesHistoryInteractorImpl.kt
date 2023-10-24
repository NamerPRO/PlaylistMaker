package ru.namerpro.playlistmaker.common.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.namerpro.playlistmaker.common.data.converters.TracksDbConvertor
import ru.namerpro.playlistmaker.common.domain.api.FavouritesHistoryInteractor
import ru.namerpro.playlistmaker.common.domain.api.FavouritesHistoryRepository
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class FavouritesHistoryInteractorImpl(
    private val favouritesHistoryRepository: FavouritesHistoryRepository,
    private val tracksDbConvertor: TracksDbConvertor
) : FavouritesHistoryInteractor {

    override fun getFavourites(): Flow<List<TrackModel>> {
        return favouritesHistoryRepository.getFavourites()
    }

    override suspend fun isTrackFavourite(id: Long): Boolean {
        return favouritesHistoryRepository.isTrackFavourite(id)
    }

    override suspend fun deleteFromFavourites(
        track: TrackModel
    ) {
        return favouritesHistoryRepository.deleteFromFavourites(tracksDbConvertor.map(track, 0))
    }

    override suspend fun addToFavourites(
        track: TrackModel,
        favouriteAddTime: Long
    ) {
        return favouritesHistoryRepository.addToFavourites(tracksDbConvertor.map(track, favouriteAddTime))
    }

}