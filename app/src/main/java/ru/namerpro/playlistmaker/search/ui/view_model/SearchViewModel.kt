package ru.namerpro.playlistmaker.search.ui.view_model

import androidx.lifecycle.*
import ru.namerpro.playlistmaker.search.domain.api.SearchHistoryInteractor
import ru.namerpro.playlistmaker.search.domain.api.SearchHistorySaveInteractor
import ru.namerpro.playlistmaker.search.domain.api.TracksInteractor
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import ru.namerpro.playlistmaker.search.ui.fragments.state.SearchRenderState
import ru.namerpro.playlistmaker.common.SingleLiveEvent
import ru.namerpro.playlistmaker.common.utils.debounce

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val preferencesSearchInteractor: SearchHistorySaveInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    companion object {
        const val TRACK_INTENT_KEY = "track_intent_key"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val renderStateLiveData = MutableLiveData<SearchRenderState>()
    fun observeRenderState(): LiveData<SearchRenderState> = renderStateLiveData

    private val searchAreaStringLiveData = SingleLiveEvent<String>()
    fun observeSearchAreaString(): LiveData<String> = searchAreaStringLiveData

    private val searchAreaFocusLiveData = SingleLiveEvent<Boolean>()
    fun observeSearchAreaFocus(): LiveData<Boolean> = searchAreaFocusLiveData

    val searchDebounce = debounce<String?>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { searchDataValue ->
        if (searchDataValue != null) {
            renderStateLiveData.postValue(SearchRenderState.Loading)
            tracksInteractor.searchTracks(searchDataValue).collect { response ->
                val foundTracks = response.first
                val responseCode = response.second

                if (responseCode == 200) {
                    if (foundTracks.isNotEmpty()) {
                        renderStateLiveData.postValue(
                            SearchRenderState.Success(
                                tracks = foundTracks
                            )
                        )
                    } else {
                        renderStateLiveData.postValue(SearchRenderState.NothingFound)
                    }
                } else {
                    renderStateLiveData.postValue(SearchRenderState.NoInternet)
                }
            }
        }
    }

    var searchDataValue = ""

    init {
        searchHistoryInteractor.getHistory().apply {
            clear()
            addAll(preferencesSearchInteractor.loadTracks())
        }
    }

    fun saveTracks() {
        preferencesSearchInteractor.saveTracks(
            tracks = searchHistoryInteractor.getHistory()
        )
    }

    fun clearTextButtonExecutor() {
        searchAreaStringLiveData.postValue("")
        renderStateLiveData.postValue(SearchRenderState.History)
        searchAreaFocusLiveData.postValue(false)
    }

    fun clearTrackHistoryExecutor() {
        searchHistoryInteractor.clearHistory()
        renderStateLiveData.postValue(SearchRenderState.Empty)
    }

    fun cancelSearch(isLoadingState: Boolean = false) {
        searchDebounce(null)
        if (isLoadingState) {
            renderStateLiveData.postValue(SearchRenderState.Empty)
        }
    }

    fun searchAreaFocusExecutor(
        searchViewText: String,
        hasFocus: Boolean
    ) {
        if (searchViewText.isNotEmpty()) {
            return
        }

        cancelSearch()

        searchDataValue = ""

        if (hasFocus && !searchHistoryInteractor.isHistoryEmpty()) {
            renderStateLiveData.postValue(SearchRenderState.History)
        } else {
            renderStateLiveData.postValue(SearchRenderState.Empty)
        }
    }

    fun searchAreaTextChangedExecutor(
        searchViewText: String
    ) {
        if (searchDataValue == searchViewText) {
            return
        }

        searchDataValue = searchViewText

        if (searchDataValue.isEmpty()) {
            searchAreaFocusExecutor("", true)
            return
        }

        renderStateLiveData.postValue(
            SearchRenderState.Search(
                showClearButton = searchDataValue.isNotEmpty(),
                showSearchHistory = searchDataValue.isEmpty() && !searchHistoryInteractor.isHistoryEmpty()
            )
        )

        searchDebounce(searchDataValue)
    }

    fun getHistory() : ArrayList<TrackModel> {
        return searchHistoryInteractor.getHistory()
    }

    fun appendHistory(
        track: TrackModel
    ) {
        searchHistoryInteractor.addTrack(track)
    }

}