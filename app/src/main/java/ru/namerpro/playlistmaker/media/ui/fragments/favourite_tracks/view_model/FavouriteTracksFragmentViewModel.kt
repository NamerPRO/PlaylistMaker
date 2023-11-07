package ru.namerpro.playlistmaker.media.ui.fragments.favourite_tracks.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.namerpro.playlistmaker.common.domain.api.FavouritesDatabaseInteractor
import ru.namerpro.playlistmaker.media.ui.fragments.favourite_tracks.state.FavouritesState

class FavouriteTracksFragmentViewModel(
    private val favouritesDatabaseInteractor: FavouritesDatabaseInteractor
) : ViewModel() {

    private val favouritesLiveData = MutableLiveData<FavouritesState>()
    fun observeFavouritesLiveData(): LiveData<FavouritesState> = favouritesLiveData

    fun fillFavourites() {
        viewModelScope.launch {
            favouritesDatabaseInteractor
                .getFavourites()
                .collect { favourites ->
                    if (favourites.isEmpty()) {
                        favouritesLiveData.postValue(FavouritesState.Empty)
                    } else {
                        favouritesLiveData.postValue(FavouritesState.Filled(favourites))
                    }
                }
        }
    }

}