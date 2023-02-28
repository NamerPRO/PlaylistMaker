package ru.namerpro.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_DATA = "SEARCH_DATA"
        var SEARCH_DATA_VALUE = ""
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_DATA, SEARCH_DATA_VALUE)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        SEARCH_DATA_VALUE = savedInstanceState.getString(SEARCH_DATA,"")
        findViewById<EditText>(R.id.search_area).setText(SEARCH_DATA_VALUE)
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
                SEARCH_DATA_VALUE = if (str.isNullOrEmpty()) "" else str.toString()
                clearTextButton.visibility = if (str.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })
    }
}