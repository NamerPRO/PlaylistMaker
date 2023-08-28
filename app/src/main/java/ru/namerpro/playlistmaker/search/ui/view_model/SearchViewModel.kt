package ru.namerpro.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import ru.namerpro.playlistmaker.search.domain.api.HistoryInteractor
import ru.namerpro.playlistmaker.search.domain.api.SharedPreferencesSearchInteractor
import ru.namerpro.playlistmaker.search.domain.api.TracksInteractor
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import ru.namerpro.playlistmaker.search.ui.activity.state.SearchRenderState
import ru.namerpro.playlistmaker.universal.domain.models.SingleLiveEvent

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val preferencesSearchInteractor: SharedPreferencesSearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    companion object {
        const val TRACK_INTENT_KEY = "track_intent_key"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val renderStateLiveData = MutableLiveData<SearchRenderState>()
    fun observeRenderState(): LiveData<SearchRenderState> = renderStateLiveData

    private val searchAreaStringLiveData = SingleLiveEvent<String>()
    fun observeSearchAreaString(): LiveData<String> = searchAreaStringLiveData

    private val searchAreaFocusLiveData = SingleLiveEvent<Boolean>()
    fun observeSearchAreaFocus(): LiveData<Boolean> = searchAreaFocusLiveData

    private var isClickAllowed = true

    private val itunesCallback = object : TracksInteractor.TracksCallback {

        override fun handle(
            foundTracks: List<TrackModel>,
            responseCode: Int
        ) {
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

    val searchRunnable = Runnable {
        searchRequest()
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCleared() {
        preferencesSearchInteractor.saveTracks(
            tracks = historyInteractor.getHistory()
        )
    }

    private fun searchRequest() {
        renderStateLiveData.postValue(SearchRenderState.Loading)
        tracksInteractor.searchTracks(searchDataValue, itunesCallback)
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
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

        handler.removeCallbacks(searchRunnable)

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

        searchDebounce()
    }

    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
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