package ru.namerpro.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchBackButton = findViewById<ImageView>(R.id.search_back)
        searchBackButton.setOnClickListener {
            finish()
        }

        val searchArea = findViewById<EditText>(R.id.search_area)

        val clearTextButton = findViewById<ImageView>(R.id.search_clear_text)
        clearTextButton.setOnClickListener {
            searchArea.setText("")
            searchArea.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }

        searchArea.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(str: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(str: Editable?) {}

            override fun onTextChanged(str: CharSequence?, start: Int, before: Int, count: Int) {
                searchDataValue = if (str.isNullOrEmpty()) "" else str.toString()
                clearTextButton.visibility = if (str.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })

        val trackView = findViewById<RecyclerView>(R.id.track_view)
        trackView.layoutManager = LinearLayoutManager(this)

        val trackList : List<Track> = listOf(Track("Smells Like Teen Spirit", "Nirvana", "5:01", "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
                                            Track("Billie Jean", "Michael Jackson", "4:35", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
                                            Track("Stayin' Alive", "Bee Gees", "4:10", "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
                                            Track("Whole Lotta Love", "Led Zeppelin", "5:33", "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
                                            Track("Sweet Child O'Mine", "Guns N' Roses", "5:03", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"))

        val trackAdapter = TrackAdapter(trackList)
        trackView.adapter = trackAdapter

    }
}