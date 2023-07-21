package ru.namerpro.playlistmaker.presentation.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Runnable
import ru.namerpro.playlistmaker.Creator
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.data.sharedprefs.SharedPreferencesInteractorImpl.Companion.TRACK_HISTORY_PREFERENCES
import ru.namerpro.playlistmaker.domain.api.TracksInteractor
import ru.namerpro.playlistmaker.domain.models.HistoryModel
import ru.namerpro.playlistmaker.domain.models.Track
import ru.namerpro.playlistmaker.presentation.presenters.SharedPreferencesListener
import ru.namerpro.playlistmaker.presentation.ui.tracks.TrackActivity
import ru.namerpro.playlistmaker.presentation.ui.tracks.TrackAdapter

class SearchActivity : AppCompatActivity(), SharedPreferencesListener {

    private val history = HistoryModel()
    private val preferencesInteractor = Creator.provideSharedPreferencesInteractor(getSharedPreferences(TRACK_HISTORY_PREFERENCES, MODE_PRIVATE),this)

    private val trackList = mutableListOf<Track>()

    // SharedPreference listeners
    override fun onPreferencesLoad(json: String) {
        val possibleTrackHistory = Gson().fromJson<MutableList<Track>>(json, object : TypeToken<MutableList<Track>>() {}.type)
        if (possibleTrackHistory != null) {
            HistoryModel.trackHistory.addAll(possibleTrackHistory)
        }
    }

    override fun onPreferencesSave(): String {
        HistoryModel.trackHistory.clear()
        return Gson().toJson(HistoryModel.trackHistory)
    }
    // ===

    private val trackAdapter = TrackAdapter(trackList)

    private val historyAdapter = TrackAdapter(HistoryModel.trackHistory)
    private lateinit var historyView: RecyclerView

    private lateinit var noInternet: LinearLayout
    private lateinit var nothingFound: FrameLayout
    private lateinit var trackView: RecyclerView

    private var isClickAllowed = true

    var searchDataValue = ""

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var searchRunnable: Runnable
    private lateinit var searchArea: EditText

    companion object {
        const val TRACK_INTENT_KEY = "track_intent_key"
        const val SEARCH_DATA = "SEARCH_DATA"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_DATA, searchDataValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchDataValue = savedInstanceState.getString(SEARCH_DATA,"")
        findViewById<EditText>(R.id.search_area).setText(searchDataValue)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchBackButton = findViewById<ImageView>(R.id.search_back)
        searchBackButton.setOnClickListener {
            finish()
        }

        searchArea = findViewById(R.id.search_area)

        trackView = findViewById(R.id.track_view)
        trackView.layoutManager = LinearLayoutManager(this)

        trackView.adapter = trackAdapter

        historyView = findViewById(R.id.track_history)
        historyView.layoutManager = LinearLayoutManager(this)
        historyView.adapter = historyAdapter

        historyAdapter.notifyDataSetChanged()

        trackAdapter.itemClickListener = { _, track ->
            if (clickDebounce()) {
                history.addTrack(track)
                historyAdapter.notifyDataSetChanged()

                val audioIntent = Intent(this, TrackActivity::class.java)
                audioIntent.putExtra(TRACK_INTENT_KEY, Gson().toJson(track))
                startActivity(audioIntent)
            }
        }

        val searchHistoryElements = findViewById<ConstraintLayout>(R.id.search_history)
        searchArea.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryElements.visibility = if (hasFocus && searchArea.text.isNullOrEmpty() && !history.isHistoryEmpty()) View.VISIBLE else View.GONE
        }

        val clearTrackHistory = findViewById<Button>(R.id.clear_track_history)
        clearTrackHistory.setOnClickListener {
            history.clearHistory()
            historyAdapter.notifyDataSetChanged()
            searchHistoryElements.visibility = View.GONE
        }

        historyAdapter.itemClickListener = { _, track ->
            if (clickDebounce()) {
                val audioIntent = Intent(this, TrackActivity::class.java)
                audioIntent.putExtra(TRACK_INTENT_KEY, Gson().toJson(track))
                startActivity(audioIntent)
            }
        }

        val clearTextButton = findViewById<ImageView>(R.id.search_clear_text)
        clearTextButton.setOnClickListener {
            searchArea.setText("")
            searchArea.onEditorAction(EditorInfo.IME_ACTION_DONE)
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            trackView.visibility = View.GONE
            searchArea.clearFocus()
        }

        searchArea.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(str: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(str: Editable?) {}

            override fun onTextChanged(str: CharSequence?, start: Int, before: Int, count: Int) {
                searchDataValue = if (str.isNullOrEmpty()) "" else str.toString()
                clearTextButton.visibility = if (str.isNullOrEmpty()) View.GONE else View.VISIBLE
                hideAllPlaceholders()
                searchHistoryElements.visibility = if (searchArea.hasFocus() && str.isNullOrEmpty() && !history.isHistoryEmpty()) View.VISIBLE else View.GONE
            }
        })

        nothingFound = findViewById(R.id.nothing_found)
        noInternet = findViewById(R.id.nointernet)

        searchArea.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchDebounce()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        val itunesCallback = object : TracksInteractor.TracksCallback {

            override fun handle(foundTracks: List<Track>, responseCode: Int) {
                runOnUiThread {
                    findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                    findViewById<LinearLayout>(R.id.noLoadingScreen).visibility = View.VISIBLE
                    trackList.clear()
                    if (responseCode == 200) {
                        if (foundTracks.isNotEmpty()) {
                            trackList.addAll(foundTracks)
                            showContentOrPlaceholder(ContentType.SUCCESS)
                        } else {
                            showContentOrPlaceholder(ContentType.NOTHING_FOUND)
                        }
                    } else {
                        showContentOrPlaceholder(ContentType.NO_INTERNET)
                    }
                }
            }

        }

        searchRunnable = Runnable { searchRequest(itunesCallback) }

        val updateContent = findViewById<Button>(R.id.search_update_content)
        updateContent.setOnClickListener {
            handler.removeCallbacks(searchRunnable)
            handler.post(searchRunnable)
        }

    }

    private fun searchRequest(itunesCallback: TracksInteractor.TracksCallback) {
        if (searchArea.text.isNotEmpty()) {
            if (searchArea.hasFocus()) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
            findViewById<LinearLayout>(R.id.noLoadingScreen).visibility = View.GONE
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            Creator.provideTracksInteractor().searchTracks(searchArea.text.toString(), itunesCallback)
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onDestroy() {
        super.onDestroy()
        preferencesInteractor.saveTracks()
    }

    enum class ContentType {
        NO_INTERNET,
        NOTHING_FOUND,
        SUCCESS
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContentOrPlaceholder(type: ContentType) {
        hideAllPlaceholders()
        when (type) {
            ContentType.NO_INTERNET -> noInternet.visibility = View.VISIBLE
            ContentType.NOTHING_FOUND -> nothingFound.visibility = View.VISIBLE
            ContentType.SUCCESS -> trackView.visibility = View.VISIBLE
        }
        trackAdapter.notifyDataSetChanged()
    }

    private fun hideAllPlaceholders() {
        noInternet.visibility = View.GONE
        nothingFound.visibility = View.GONE
        trackView.visibility = View.GONE
    }

}