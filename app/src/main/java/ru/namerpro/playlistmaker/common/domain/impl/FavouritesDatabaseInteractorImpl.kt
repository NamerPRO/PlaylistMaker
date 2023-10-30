package ru.namerpro.playlistmaker.common.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.namerpro.playlistmaker.common.data.converters.TracksDbConvertor
import ru.namerpro.playlistmaker.common.domain.api.FavouritesDatabaseInteractor
import ru.namerpro.playlistmaker.common.domain.api.FavouritesDatabaseRepository
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class FavouritesDatabaseInteractorImpl(
    private val favouritesDatabaseRepository: FavouritesDatabaseRepository
) : FavouritesDatabaseInteractor {

    override fun getFavourites(): Flow<List<TrackModel>> {
        return favouritesDatabaseRepository.getFavourites()
    }

    override suspend fun isTrackFavourite(id: Long): Boolean {
        return favouritesDatabaseRepository.isTrackFavourite(id)
    }

    override suspend fun deleteFromFavourites(
        track: TrackModel
    ) {
        return favouritesDatabaseRepository.deleteFromFavourites(track)
    }

    override suspend fun addToFavourites(
        track: TrackModel,
        favouriteAddTime: Long
    ) {
        return favouritesDatabaseRepository.addToFavourites(track, favouriteAddTime)
    }

}