package com.practicum.project.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.content.Context
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlin.toString
import com.practicum.project.playlistmaker.SearchAdapter
import com.practicum.project.playlistmaker.iTunesAPI.SearchAPI
import com.practicum.project.playlistmaker.iTunesAPI.SearchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var searchRequest: EditText
    private lateinit var clearBtn: ImageView
    private lateinit var refreshBtn: Button
    private var searchQuery: String = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeholderErrorText: TextView
    private lateinit var placeholderErrorImage: ImageView
    private lateinit var placeholderConnectionText: TextView
    private lateinit var placeholderDownloadText: TextView
    private lateinit var placeholderConnectionImage: ImageView
    private lateinit var placeholderLayoutError: LinearLayout


    private lateinit var adapter: SearchAdapter
    private val iTunesBaseUrl: String = "https://itunes.apple.com" //R.string.baseUrl.toString()

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesSearch = retrofit.create(SearchAPI::class.java)
    private val trackList = ArrayList<Track>()

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
        refreshBtn = findViewById(R.id.refreshButton)


        placeholderLayoutError = findViewById(R.id.layoutErrorPlaceholder)
        placeholderErrorImage = findViewById(R.id.placeholderNotFoundImage)
        placeholderErrorText = findViewById(R.id.placeholderNotFoundText)
        placeholderConnectionImage = findViewById(R.id.placeholderConnectionImage)
        placeholderConnectionText = findViewById(R.id.placeholderConnectionText)
        placeholderDownloadText = findViewById(R.id.placeholderDownloadText)
        searchBack.setOnClickListener { finish() }

        clearBtn.setOnClickListener {
            searchRequest.setText("")
            trackList.clear()
            adapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearBtn.windowToken, 0)
            trackList.clear()
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
                clearBtn.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                searchQuery = s.toString()
                if (searchRequest.text.isEmpty()){
                    trackList.clear()
                }
            }
        }
        searchRequest.addTextChangedListener(simpleTextWatcher)

        searchRequest.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (searchRequest.text.isNotEmpty()){
                        searchTrack(searchRequest.text.toString())
                        setRecyclerView()
                    }
                }
                false
            }
        refreshBtn.setOnClickListener {
            searchTrack(searchRequest.text.toString())
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
        Log.d("recyclerView","run recyclerView")
        recyclerView = findViewById(R.id.searchResults)

        adapter = SearchAdapter(trackList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
    }

    private fun searchTrack(searchValue: String){
        iTunesSearch.search(searchValue).enqueue(object : Callback<SearchResponse> {
            override fun onResponse (call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.code() == 200) {
                    if(response.body()?.resultCount!! > 0){
                        trackList.addAll(response.body()?.results!!)
                        errorHandle("")
                    } else {
                        errorHandle("not_found")
                    }
                } else {
                    errorHandle("connection_error")
                }
            }

            override fun onFailure(
                call: Call<SearchResponse?>,
                t: Throwable
            ) {
                errorHandle("connection_error")
            }
        })
    }

    private fun errorHandle(status: String){
        recyclerView.visibility = View.GONE

        trackList.clear()
        adapter.notifyDataSetChanged()

        when(status){
            "not_found" ->{
                Log.d("show result","nothing")
                placeholderLayoutError.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                refreshBtn.visibility = View.GONE
                placeholderConnectionImage.visibility = View.GONE
                placeholderConnectionText.visibility = View.GONE
                placeholderDownloadText.visibility = View.GONE

                placeholderErrorImage.visibility = View.VISIBLE
                placeholderErrorText.visibility =View.VISIBLE
            }
            "connection_error" ->{
                Log.d("show result","connection error")
                recyclerView.visibility = View.GONE
                placeholderErrorImage.visibility = View.GONE
                placeholderErrorText.visibility =View.GONE
                placeholderLayoutError.visibility = View.VISIBLE
                placeholderConnectionImage.visibility = View.VISIBLE
                placeholderConnectionText.visibility = View.VISIBLE
                placeholderDownloadText.visibility = View.VISIBLE
                refreshBtn.visibility = View.VISIBLE
            }
            else -> {
                Log.d("show result","result")
                recyclerView.visibility = View.VISIBLE
                placeholderLayoutError.visibility = View.GONE
            }

        }

    }
}
