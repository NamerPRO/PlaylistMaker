package ru.namerpro.playlistmaker.common.domain.api

import kotlinx.coroutines.flow.Flow
import ru.namerpro.playlistmaker.common.data.db.entity.TrackEntity
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

interface FavouritesHistoryRepository {

    fun getFavourites(): Flow<List<TrackModel>>

    suspend fun isTrackFavourite(
        id: Long
    ): Boolean

    suspend fun deleteFromFavourites(
        trackEntity: TrackEntity
    )

    suspend fun addToFavourites(
        trackEntity: TrackEntity
    )

}