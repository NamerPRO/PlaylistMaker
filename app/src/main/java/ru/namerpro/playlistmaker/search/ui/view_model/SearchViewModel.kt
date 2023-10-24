package ru.namerpro.playlistmaker.search.ui.view_model

import androidx.lifecycle.*
import ru.namerpro.playlistmaker.search.domain.api.HistoryInteractor
import ru.namerpro.playlistmaker.search.domain.api.SharedPreferencesSearchInteractor
import ru.namerpro.playlistmaker.search.domain.api.TracksInteractor
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import ru.namerpro.playlistmaker.search.ui.fragments.state.SearchRenderState
import ru.namerpro.playlistmaker.utils.SingleLiveEvent
import ru.namerpro.playlistmaker.utils.debounce

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val preferencesSearchInteractor: SharedPreferencesSearchInteractor,
    private val historyInteractor: HistoryInteractor
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

    val searchDebounce = debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { searchDataValue ->
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

    var searchDataValue = ""

    init {
        historyInteractor.getHistory().apply {
            clear()
            addAll(preferencesSearchInteractor.loadTracks())
        }
    }

    fun saveTracks() {
        preferencesSearchInteractor.saveTracks(
            tracks = historyInteractor.getHistory()
        )
    }

    fun clearTextButtonExecutor() {
        searchAreaStringLiveData.postValue("")
        renderStateLiveData.postValue(SearchRenderState.History)
        searchAreaFocusLiveData.postValue(false)
    }

    fun clearTrackHistoryExecutor() {
        historyInteractor.clearHistory()
        renderStateLiveData.postValue(SearchRenderState.Empty)
    }

    fun searchAreaFocusExecutor(
        searchViewText: String,
        hasFocus: Boolean
    ) {
        if (searchViewText.isNotEmpty()) {
            return
        }

        searchDataValue = ""

        if (hasFocus && !historyInteractor.isHistoryEmpty()) {
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
                showSearchHistory = searchDataValue.isEmpty() && !historyInteractor.isHistoryEmpty()
            )
        )

        searchDebounce(searchDataValue)
    }

    fun getHistory() : ArrayList<TrackModel> {
        return historyInteractor.getHistory()
    }

    fun appendHistory(
        track: TrackModel
    ) {
        historyInteractor.addTrack(track)
    }

}