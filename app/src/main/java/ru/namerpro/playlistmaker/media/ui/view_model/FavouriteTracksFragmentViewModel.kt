package ru.namerpro.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.namerpro.playlistmaker.common.domain.api.FavouritesHistoryInteractor
import ru.namerpro.playlistmaker.media.ui.fragments.state.FavouritesState

class FavouriteTracksFragmentViewModel(
    private val favouritesHistoryInteractor: FavouritesHistoryInteractor
) : ViewModel() {

    private val favouritesLiveData = MutableLiveData<FavouritesState>()
    fun observeFavouritesLiveData(): LiveData<FavouritesState> = favouritesLiveData

    fun fillFavourites() {
        viewModelScope.launch {
            favouritesHistoryInteractor
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