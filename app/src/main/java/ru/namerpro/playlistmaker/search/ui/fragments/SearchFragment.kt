package ru.namerpro.playlistmaker.search.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.namerpro.playlistmaker.databinding.FragmentSearchBinding
import ru.namerpro.playlistmaker.player.ui.activity.PlayerActivity
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import ru.namerpro.playlistmaker.search.ui.fragments.state.SearchRenderState
import ru.namerpro.playlistmaker.search.ui.adapter.TrackAdapter
import ru.namerpro.playlistmaker.search.ui.view_model.SearchViewModel
import ru.namerpro.playlistmaker.common.utils.debounce

class SearchFragment : Fragment() {

    private lateinit var historyView: RecyclerView
    private lateinit var noInternetLayout: ScrollView
    private lateinit var nothingFoundLayout: FrameLayout
    private lateinit var trackView: RecyclerView
    private lateinit var searchAreaView: EditText
    private lateinit var searchHistoryElementsLayout: ConstraintLayout
    private lateinit var clearTextButtonView: ImageView
    private lateinit var clearTrackHistoryView: Button
    private lateinit var updateContentView: Button
    private lateinit var progressBarView: ProgressBar

    private lateinit var textWatcher: TextWatcher

    private lateinit var historyAdapter: TrackAdapter

    private lateinit var trackAdapter: TrackAdapter

    private val clickDebounce = debounce<Pair<TrackModel, Boolean>>(
        delayMillis = CLICK_DEBOUNCE_DELAY,
        coroutineScope = lifecycleScope,
        useLastParam = false
    ) { trackData ->
        if (trackData.second) {
            viewModel.appendHistory(trackData.first)
        }
        startAudioActivity(trackData.first)
    }

    private val viewModel: SearchViewModel by viewModel()

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        historyView = binding.trackHistory
        noInternetLayout = binding.noInternet
        nothingFoundLayout = binding.nothingFound
        trackView = binding.trackView
        searchAreaView = binding.searchArea
        searchHistoryElementsLayout = binding.searchHistory
        clearTrackHistoryView = binding.clearTrackHistory
        clearTextButtonView = binding.searchClearText
        updateContentView = binding.searchUpdateContent
        progressBarView = binding.progressBar

        initRecyclerViews()
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchAreaView.removeTextChangedListener(textWatcher)
    }

    override fun onPause() {
        super.onPause()
        viewModel.cancelSearch(progressBarView.isVisible)
        viewModel.saveTracks()
    }

    private fun initRecyclerViews() {
        trackAdapter = TrackAdapter(ArrayList()) { track ->
            clickDebounce(Pair(track, true))
        }

        trackView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter
        }

        historyAdapter = TrackAdapter(viewModel.getHistory()) { track ->
            clickDebounce(Pair(track, false))
        }

        historyView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun initListeners() {
        viewModel.observeRenderState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeSearchAreaString().observe(viewLifecycleOwner) {
            setSearchAreaViewText(it)
        }

        viewModel.observeSearchAreaFocus().observe(viewLifecycleOwner) {
            setSearchAreaViewFocus(it)
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

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(str: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(str: Editable?) {}

            override fun onTextChanged(searchText: CharSequence?, start: Int, before: Int, count: Int) {
                if (!searchText.isNullOrEmpty()) {
                    clearTextButtonView.visibility = View.VISIBLE
                }
                viewModel.searchAreaTextChangedExecutor(getSearchAreaViewText())
            }
        }
        searchAreaView.addTextChangedListener(textWatcher)

        updateContentView.setOnClickListener {
            viewModel.searchDebounce(viewModel.searchDataValue)
        }
    }

    private fun startAudioActivity(
        track: TrackModel
    ) {
        val audioIntent = Intent(requireContext(), PlayerActivity::class.java)
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

    private fun showTracks(
        tracks: List<TrackModel>
    ) {
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

    private fun showSearchArea(
        showClearButton: Boolean,
        showSearchHistory: Boolean
    ) {
        hidePlaceholders()
        clearTextButtonView.visibility = if (showClearButton) View.VISIBLE else View.GONE
        searchHistoryElementsLayout.visibility = if (showSearchHistory) View.VISIBLE else View.GONE
    }

    private fun render(
        state: SearchRenderState
    ) {
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
    private fun updateRecyclerViewComponents(
        adapter: TrackAdapter
    ) {
        adapter.notifyDataSetChanged()
    }

    private fun getSearchAreaViewText(): String {
        return searchAreaView.text?.toString().orEmpty()
    }

    private fun setSearchAreaViewText(
        text: String?
    ) {
        searchAreaView.setText(text.orEmpty())
    }

    private fun setSearchAreaViewFocus(
        isFocused: Boolean
    ) {
        if (isFocused) {
            searchAreaView.requestFocus()
        } else {
            searchAreaView.clearFocus()
        }
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 200L
    }

}