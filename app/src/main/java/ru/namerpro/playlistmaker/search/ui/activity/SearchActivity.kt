package ru.namerpro.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import ru.namerpro.playlistmaker.creator.Creator
import ru.namerpro.playlistmaker.databinding.ActivitySearchBinding
import ru.namerpro.playlistmaker.search.data.shared_preferences.SharedPreferencesSearchRepositoryImpl
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import ru.namerpro.playlistmaker.search.ui.activity.state.SearchRenderState
import ru.namerpro.playlistmaker.search.ui.view_model.SearchViewModel
import ru.namerpro.playlistmaker.player.ui.activity.PlayerActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var historyView: RecyclerView
    private lateinit var noInternetLayout: LinearLayout
    private lateinit var nothingFoundLayout: FrameLayout
    private lateinit var trackView: RecyclerView
    private lateinit var searchAreaView: EditText
    private lateinit var searchBackButtonView: ImageView
    private lateinit var searchHistoryElementsLayout: ConstraintLayout
    private lateinit var clearTextButtonView: ImageView
    private lateinit var clearTrackHistoryView: Button
    private lateinit var updateContentView: Button
    private lateinit var progressBarView: ProgressBar

    private lateinit var historyAdapter: TrackAdapter

    private lateinit var trackAdapter: TrackAdapter

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var viewModel: SearchViewModel

    private lateinit var binding: ActivitySearchBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory(
            preferencesSearchInteractor = Creator.provideSearchSharedPreferencesInteractor(applicationContext.getSharedPreferences(
                SharedPreferencesSearchRepositoryImpl.TRACK_HISTORY_PREFERENCES,
                MODE_PRIVATE
            )),
            tracksInteractor = Creator.provideTracksInteractor()
        ))[SearchViewModel::class.java]

        historyView = binding.trackHistory
        noInternetLayout = binding.noInternet
        nothingFoundLayout = binding.nothingFound
        trackView = binding.trackView
        searchAreaView = binding.searchArea
        searchBackButtonView = binding.searchBack
        searchHistoryElementsLayout = binding.searchHistory
        clearTrackHistoryView = binding.clearTrackHistory
        clearTextButtonView = binding.searchClearText
        updateContentView = binding.searchUpdateContent
        progressBarView = binding.progressBar

        initRecyclerViews()
        initListeners()
    }

    private fun initRecyclerViews() {
        trackAdapter = TrackAdapter(ArrayList()) {
            if (viewModel.clickDebounce()) {
                viewModel.appendHistory(it)
                startAudioActivity(it)
            }
        }

        trackView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = trackAdapter
        }

        historyAdapter = TrackAdapter(viewModel.getHistory()) {
            if (viewModel.clickDebounce()) {
                startAudioActivity(it)
            }
        }

        historyView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = historyAdapter
        }
    }

    private fun initListeners() {
        viewModel.observeRenderState().observe(this) {
            render(it)
        }

        viewModel.observeSearchAreaString().observe(this) {
            setSearchAreaViewText(it)
        }

        viewModel.observeSearchAreaFocus().observe(this) {
            setSearchAreaViewFocus(it)
        }

        searchBackButtonView.setOnClickListener {
            finish()
        }

        searchAreaView.setOnFocusChangeListener { _, hasFocus ->
            viewModel.searchAreaFocusExecutor(getSearchAreaViewText(), hasFocus)
        }

        clearTrackHistoryView.setOnClickListener {
            viewModel.clearTrackHistoryExecutor()
        }

        clearTextButtonView.setOnClickListener {
            searchAreaView.onEditorAction(EditorInfo.IME_ACTION_DONE)
            viewModel.clearTextButtonExecutor()
        }

        searchAreaView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(str: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(str: Editable?) {}

            override fun onTextChanged(searchText: CharSequence?, start: Int, before: Int, count: Int) {
                clearTextButtonView.visibility = View.VISIBLE
                viewModel.searchAreaTextChangedExecutor(getSearchAreaViewText())
            }
        })

        updateContentView.setOnClickListener {
            handler.removeCallbacks(viewModel.searchRunnable)
            handler.post(viewModel.searchRunnable)
        }
    }

    private fun startAudioActivity(track: TrackModel) {
        val audioIntent = Intent(this, PlayerActivity::class.java)
        audioIntent.putExtra(SearchViewModel.TRACK_INTENT_KEY, Gson().toJson(track))
        startActivity(audioIntent)
    }

    private fun hidePlaceholders() {
        noInternetLayout.visibility = View.GONE
        nothingFoundLayout.visibility = View.GONE
        trackView.visibility = View.GONE
        historyView.visibility = View.GONE
        progressBarView.visibility = View.GONE
        searchHistoryElementsLayout.visibility = View.GONE
    }

    private fun showLoading() {
        hidePlaceholders()
        progressBarView.visibility = View.VISIBLE
    }

    private fun hideKeyboardAndFocus() {
        searchAreaView.clearFocus()
        searchAreaView.onEditorAction(EditorInfo.IME_ACTION_DONE)
    }

    private fun showNoInternet() {
        hidePlaceholders()
        noInternetLayout.visibility = View.VISIBLE
        hideKeyboardAndFocus()
    }

    private fun showNothingFound() {
        hidePlaceholders()
        nothingFoundLayout.visibility = View.VISIBLE
        hideKeyboardAndFocus()
    }

    private fun showTracks(tracks: List<TrackModel>) {
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        updateRecyclerViewComponents(trackAdapter)
        hidePlaceholders()
        trackView.visibility = View.VISIBLE
        hideKeyboardAndFocus()
    }

    private fun showSearchHistory() {
        hidePlaceholders()
        updateRecyclerViewComponents(historyAdapter)
        searchHistoryElementsLayout.visibility = View.VISIBLE
        historyView.visibility = View.VISIBLE
        clearTextButtonView.visibility = View.GONE
    }

    private fun showEmpty() {
        hidePlaceholders()
        clearTextButtonView.visibility = View.GONE
    }

    private fun showSearchArea(showClearButton: Boolean, showSearchHistory: Boolean) {
        hidePlaceholders()
        clearTextButtonView.visibility = if (showClearButton) View.VISIBLE else View.GONE
        searchHistoryElementsLayout.visibility = if (showSearchHistory) View.VISIBLE else View.GONE
    }

    private fun render(state: SearchRenderState) {
        when (state) {
            is SearchRenderState.Loading -> showLoading()
            is SearchRenderState.NoInternet -> showNoInternet()
            is SearchRenderState.NothingFound -> showNothingFound()
            is SearchRenderState.Success -> showTracks(state.tracks)
            is SearchRenderState.History -> showSearchHistory()
            is SearchRenderState.Empty -> showEmpty()
            is SearchRenderState.Search -> showSearchArea(state.showClearButton, state.showSearchHistory)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerViewComponents(adapter: TrackAdapter) {
        adapter.notifyDataSetChanged()
    }

    private fun getSearchAreaViewText(): String {
        return searchAreaView.text?.toString().orEmpty()
    }

    private fun setSearchAreaViewText(text: String?) {
        searchAreaView.setText(text.orEmpty())
    }

    private fun setSearchAreaViewFocus(isFocused: Boolean) {
        if (isFocused) {
            searchAreaView.requestFocus()
        } else {
            searchAreaView.clearFocus()
        }
    }

}