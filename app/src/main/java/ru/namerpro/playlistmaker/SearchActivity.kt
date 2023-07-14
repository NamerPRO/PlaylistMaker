package ru.namerpro.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonSerializer
import kotlinx.coroutines.Runnable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TRACK_INTENT_KEY = "track_intent_key"

class SearchActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesServiceApi::class.java)

    private val trackList = mutableListOf<Track>()
    private val trackAdapter = TrackAdapter(trackList)

    private val historyAdapter = TrackAdapter(SearchHistory.trackHistory)
    private lateinit var historyView: RecyclerView
    private lateinit var historyManager: SearchHistory

    private lateinit var noInternet: LinearLayout
    private lateinit var nothingFound: FrameLayout
    private lateinit var trackView: RecyclerView

    private var isClickAllowed = true

    var searchDataValue = ""

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var searchRunnable : Runnable
    private lateinit var searchArea: EditText

    companion object {
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

        val historyPrefs = getSharedPreferences(TRACK_HISTORY_PREFERENCES, MODE_PRIVATE)
        historyManager = SearchHistory(historyPrefs)
        historyAdapter.notifyDataSetChanged()

        trackAdapter.itemClickListener = { _, track ->
            if (clickDebounce()) {
                historyManager.addTrack(track)
                historyAdapter.notifyDataSetChanged()

                val audioIntent = Intent(this, AudioActivity::class.java)
                audioIntent.putExtra(TRACK_INTENT_KEY, Gson().toJson(track))
                startActivity(audioIntent)
            }
        }

        val searchHistoryElements = findViewById<ConstraintLayout>(R.id.search_history)
        searchArea.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryElements.visibility = if (hasFocus && searchArea.text.isNullOrEmpty() && !historyManager.isHistoryEmpty()) View.VISIBLE else View.GONE
        }

        val clearTrackHistory = findViewById<Button>(R.id.clear_track_history)
        clearTrackHistory.setOnClickListener {
            historyManager.clearHistory()
            historyAdapter.notifyDataSetChanged()
            searchHistoryElements.visibility = View.GONE
        }

        historyAdapter.itemClickListener = { _, track ->
            if (clickDebounce()) {
                val audioIntent = Intent(this, AudioActivity::class.java)
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
                searchHistoryElements.visibility = if (searchArea.hasFocus() && str.isNullOrEmpty() && !historyManager.isHistoryEmpty()) View.VISIBLE else View.GONE
            }
        })

        nothingFound = findViewById(R.id.nothing_found)
        noInternet = findViewById(R.id.nointernet)

        searchArea.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchDebounce()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        val itunesCallback = object : Callback<TrackResponse> {
            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                findViewById<LinearLayout>(R.id.noLoadingScreen).visibility = View.VISIBLE
                trackList.clear()
                if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        trackList.addAll(response.body()?.results!!)
                        showContentOrPlaceholder(ContentType.SUCCESS)
                    }
                    if (trackList.isEmpty()) {
                        showContentOrPlaceholder(ContentType.NOTHING_FOUND)
                    }
                } else {
                    showContentOrPlaceholder(ContentType.NO_INTERNET)
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                findViewById<LinearLayout>(R.id.noLoadingScreen).visibility = View.VISIBLE
                trackList.clear()
                showContentOrPlaceholder(ContentType.NO_INTERNET)
            }
        }

        searchRunnable = Runnable { searchRequest(itunesCallback) }

        val updateContent = findViewById<Button>(R.id.search_update_content)
        updateContent.setOnClickListener {
            itunesService.search(searchArea.text.toString()).enqueue(itunesCallback)
        }

    }

    private fun searchRequest(itunesCallback: Callback<TrackResponse>) {
        if (searchArea.text.isNotEmpty()) {
            findViewById<LinearLayout>(R.id.noLoadingScreen).visibility = View.GONE
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            itunesService.search(searchArea.text.toString()).enqueue(itunesCallback)
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
        historyManager.saveTracks()
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