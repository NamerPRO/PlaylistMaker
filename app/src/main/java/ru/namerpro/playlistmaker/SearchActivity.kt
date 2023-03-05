package ru.namerpro.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private lateinit var savedRequest: String
    private val itunesService = retrofit.create(ItunesServiceApi::class.java)

    var searchDataValue = ""

    companion object {
        const val SEARCH_DATA = "SEARCH_DATA"
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

        val searchArea = findViewById<EditText>(R.id.search_area)

        val trackView = findViewById<RecyclerView>(R.id.track_view)
        trackView.layoutManager = LinearLayoutManager(this)

        val trackList = mutableListOf<Track>()

        val trackAdapter = TrackAdapter(trackList)
        trackView.adapter = trackAdapter

        val clearTextButton = findViewById<ImageView>(R.id.search_clear_text)
        clearTextButton.setOnClickListener {
            searchArea.setText("")
            searchArea.onEditorAction(EditorInfo.IME_ACTION_DONE)
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            trackView.visibility = View.GONE
        }

        searchArea.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(str: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(str: Editable?) {}

            override fun onTextChanged(str: CharSequence?, start: Int, before: Int, count: Int) {
                searchDataValue = if (str.isNullOrEmpty()) "" else str.toString()
                clearTextButton.visibility = if (str.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })

        val nothingFound = findViewById<FrameLayout>(R.id.nothing_found)
        val noInternet = findViewById<LinearLayout>(R.id.nointernet)

        val itunesCallback = object : Callback<TrackResponse> {

            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                if (response.code() == 200) {
                    noInternet.visibility = View.GONE
                    nothingFound.visibility = View.GONE
                    trackView.visibility = View.GONE
                    trackList.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        trackList.addAll(response.body()?.results!!)
                        trackView.visibility = View.VISIBLE
                        trackAdapter.notifyDataSetChanged()
                    }
                    if (trackList.isEmpty()) {
                        nothingFound.visibility = View.VISIBLE
                    }
                } else {
//                                Toast.makeText(applicationContext, "Failed to download content :(", Toast.LENGTH_LONG).show()
                    noInternet.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                trackView.visibility = View.GONE
                nothingFound.visibility = View.GONE
                noInternet.visibility = View.VISIBLE
            }

        }

        val updateContent = findViewById<Button>(R.id.search_update_content)
        updateContent.setOnClickListener {
            itunesService.search(savedRequest).enqueue(itunesCallback)
        }

        searchArea.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchArea.text.isNotEmpty()) {
                    savedRequest = searchArea.text.toString()
                    itunesService.search(searchArea.text.toString()).enqueue(itunesCallback)
                }
                true
            }
            false
        }

    }
}