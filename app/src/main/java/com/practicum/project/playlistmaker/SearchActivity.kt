package com.practicum.project.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.content.Context
import android.os.PersistableBundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class SearchActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private var searchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchBack = findViewById<Button>(R.id.searchBack)
        val searchRequest = findViewById<EditText>(R.id.searchInputText)
        val clearBtn = findViewById<ImageView>(R.id.clearIcon)

        searchBack.setOnClickListener { finish() }

        clearBtn.setOnClickListener {
            searchRequest.setText("")
        }

        val simpleTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
               //empty
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.visibility = clearButtonVisibility(s)
            }
            override fun afterTextChanged(s: Editable?) {
                searchQuery = s.toString()
            }
        }
        searchRequest.addTextChangedListener(simpleTextWatcher)
    }
    private fun clearButtonVisibility(s: CharSequence?): Int{
        return if (s.isNullOrEmpty()){
            View.GONE
        }else{
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("savedSearchReq",editText.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString("savedSearchReq","")
        editText.setText(searchQuery)
    }
}
