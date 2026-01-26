package com.practicum.project.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.content.Context
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.toString
import com.practicum.project.playlistmaker.Mock
import com.practicum.project.playlistmaker.SearchAdapter

class SearchActivity : AppCompatActivity() {
    private lateinit var searchRequest: EditText
    private lateinit var clearBtn: ImageView
    private var searchQuery: String = ""

    //private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter

    private val mockData = Mock.getMockTracks()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val searchBack = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

            searchRequest = findViewById(R.id.searchInputText)
            clearBtn = findViewById(R.id.clearIcon)

            searchBack.setOnClickListener { finish() }

            clearBtn.setOnClickListener {
                searchRequest.setText("")
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(clearBtn.windowToken, 0)
            }
        setRecyclerView()
            val simpleTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //empty
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    //clearBtn.visibility = // clearButtonVisibility(s)
                    clearBtn.isVisible = !s.isNullOrEmpty()
                }

                override fun afterTextChanged(s: Editable?) {
                    searchQuery = s.toString()
                }
            }
            searchRequest.addTextChangedListener(simpleTextWatcher)
        }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("savedSearchReq", searchRequest.getText().toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString("savedSearchReq", "")
        searchRequest.setText(searchQuery)
    }
    private fun setRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.searchResults)

        adapter = SearchAdapter(mockData)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
    }
}
