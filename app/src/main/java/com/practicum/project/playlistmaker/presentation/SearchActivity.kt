package com.practicum.project.playlistmaker.presentation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.project.playlistmaker.Creator
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.domain.api.TracksInteractor
import com.practicum.project.playlistmaker.domain.models.Track
import com.practicum.project.playlistmaker.iTunesAPI.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var searchRequest: EditText
    private lateinit var clearBtn: ImageView
    private lateinit var refreshBtn: Button
    private var searchQuery: String = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyRecyclerView : RecyclerView
    private lateinit var placeholderErrorText: TextView
    private lateinit var placeholderErrorImage: ImageView
    private lateinit var placeholderConnectionText: TextView
    private lateinit var placeholderDownloadText: TextView
    private lateinit var placeholderConnectionImage: ImageView
    private lateinit var placeholderLayoutError: LinearLayout
    private lateinit var adapter: SearchAdapter
//    private val iTunesBaseUrl: String = "https://itunes.apple.com" //R.string.baseUrl.toString()

//    private val retrofit = Retrofit.Builder()
//        .baseUrl(iTunesBaseUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    private val iTunesSearch = retrofit.create(SearchAPI::class.java)
    private val trackList: MutableList<Track> = mutableListOf()
//    private lateinit var searchHistory: SearchHistory

    private lateinit var historyLabel: TextView
    private lateinit var clearHistoryBtn: Button
    private lateinit var historyAdapter: SearchAdapter
    private lateinit var historyLayout: LinearLayout
    private lateinit var sharedPreferences : SharedPreferences

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var progressLayout: FrameLayout
    private lateinit var progressBar: ProgressBar
    private val tracksInteractor = Creator.provideTrackInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val searchBack = findViewById<Toolbar>(R.id.toolbar)

        searchRequest = findViewById(R.id.searchInputText)
        clearBtn = findViewById(R.id.clearIcon)
        refreshBtn = findViewById(R.id.refreshButton)
        placeholderLayoutError = findViewById(R.id.layoutErrorPlaceholder)
        placeholderErrorImage = findViewById(R.id.placeholderNotFoundImage)
        placeholderErrorText = findViewById(R.id.placeholderNotFoundText)
        placeholderConnectionImage = findViewById(R.id.placeholderConnectionImage)
        placeholderConnectionText = findViewById(R.id.placeholderConnectionText)
        placeholderDownloadText = findViewById(R.id.placeholderDownloadText)
        historyLayout = findViewById(R.id.history)
        historyLabel = findViewById(R.id.historyHeader)
        clearHistoryBtn = findViewById(R.id.cleanHistory)
        progressLayout = findViewById(R.id.progress)
        progressBar = findViewById(R.id.progressBar)

        //sharedPreferences = getSharedPreferences(SearchHistory.SEARCH_HISTORY_PREF, MODE_PRIVATE)
        //searchHistory = SearchHistory(sharedPreferences)
        //sharedPreferences = getSharedPreferences()

        searchBack.setOnClickListener { finish() }

        progressLayout.visibility = View.GONE
        progressBar.visibility = View.GONE

        clearHistoryBtn.setOnClickListener {
            tracksInteractor.clearHistory()
            updateSearchHistory()
        }

        clearBtn.setOnClickListener {
            searchRequest.setText("")
            trackList.clear()
            adapter.notifyDataSetChanged()

            updateSearchHistory()
            if(tracksInteractor.isNotEmpty()) {
                historyLabel.visibility = View.VISIBLE
                historyLayout.visibility = View.VISIBLE
                clearHistoryBtn.visibility = View.VISIBLE
            }
            recyclerView.visibility = View.GONE
            placeholderLayoutError.visibility = View.GONE

            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearBtn.windowToken, 0)
        }

        searchRequest.setOnFocusChangeListener{ _, hasFocus ->
            if (hasFocus && searchRequest.text.isEmpty() && tracksInteractor.isNotEmpty()){
                updateSearchHistory()
                historyLabel.visibility = View.VISIBLE
                historyLayout.visibility = View.VISIBLE
                clearHistoryBtn.visibility = View.VISIBLE
            } else {
                historyLabel.visibility = View.GONE
                historyLayout.visibility = View.GONE
                clearHistoryBtn.visibility = View.GONE
            }
        }
        setRecyclerView()

        if (searchRequest.text.isEmpty()){
            updateSearchHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.isVisible = !s.isNullOrEmpty()
                searchDebounce()
                if (s.isNullOrEmpty()){
                    updateSearchHistory()
                    handler.removeCallbacks(searchRunnable)
                    recyclerView.visibility = View.GONE
                    placeholderLayoutError.visibility = View.GONE
                    progressLayout.visibility = View.VISIBLE
                    progressBar.visibility = View.VISIBLE
                } else{
                    historyLabel.visibility = View.GONE
                    historyLayout.visibility = View.GONE
                    clearHistoryBtn.visibility = View.GONE
                    progressLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchDebounce()
                searchQuery = s.toString()
                if (s.toString().isEmpty()){
                    trackList.clear()
                }

            }
        }
        searchRequest.addTextChangedListener(simpleTextWatcher)

        searchRequest.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (searchRequest.text.isNotEmpty()){
                        searchDebounce()
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
    private val searchRunnable = Runnable {searchTrack(searchRequest.text.toString())}

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
        recyclerView = findViewById(R.id.searchResults)
        historyRecyclerView = findViewById(R.id.searchedTracks)


        adapter = SearchAdapter(trackList) {
            if (clickDebounce()) {
                tracksInteractor.addTrackToHistory(it)

                //searchHistory.addTrackToHistory(it)

                val trackIntent =
                    Intent(this, PlayerActivity::class.java).apply { putExtra("Track", (it)) }
                startActivity(trackIntent)
            }
        }
        historyAdapter = SearchAdapter(mutableListOf()) {
            if (clickDebounce()) {
                tracksInteractor.addTrackToHistory(it)
                //searchHistory.addTrackToHistory(it)
                val trackIntent =
                    Intent(this, PlayerActivity::class.java).apply { putExtra("Track", (it)) }
                startActivity(trackIntent)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = historyAdapter

    }


    private fun searchTrack(searchValue: String){
        showProgressBar()
        tracksInteractor.searchTracks(searchValue, object:TracksInteractor.TracksConsumer{
            override fun consume(foundTracks: List<Track>){
                handler.post{
                    hideProgressBar()
                    if (foundTracks.isNotEmpty()){
                        trackList.addAll(foundTracks)
                        errorHandle(RequestState.Success)
                        adapter.updateList(trackList)
                    } else {
                        errorHandle(RequestState.NotFound)
                    }
                }
            }
            override fun onFailure() {
                handler.post {
                    hideProgressBar()
                    errorHandle(RequestState.NotConnected)
                }
            }
        })
//        iTunesSearch.search(searchValue).enqueue(object : Callback<SearchResponse> {
//            override fun onResponse (call: Call<SearchResponse>, response: Response<SearchResponse>) {
//
//                if (response.isSuccessful) {
//                    val responseValue = response.body()?.results ?: emptyList()
//                    if (responseValue.isNotEmpty()){
//                        trackList.addAll(responseValue)
//                        errorHandle(RequestState.Success)
//                    } else {
//                        errorHandle(RequestState.NotFound)
//                    }
//                } else {
//                    errorHandle(RequestState.NotConnected)
//                }
//            }
//
//            override fun onFailure(
//                call: Call<SearchResponse?>,
//                t: Throwable
//            ) {
//                hideProgressBar()
//                errorHandle(RequestState.NotConnected)
//            }
//        })
    }

    private fun updateSearchHistory(){
        val historyTrackList = tracksInteractor.getTrackFromHistory()

        if (historyTrackList.isNotEmpty() && searchRequest.text.isEmpty() && searchRequest.hasFocus()){
            historyAdapter.updateList(historyTrackList)

            recyclerView.visibility = View.GONE
            placeholderLayoutError.visibility = View.GONE
            hideProgressBar()

            historyLayout.visibility = View.VISIBLE
            clearHistoryBtn.visibility = View.VISIBLE
            historyLabel.visibility = View.VISIBLE
            historyRecyclerView.visibility = View.VISIBLE
        } else {
            historyLayout.visibility = View.GONE
            clearHistoryBtn.visibility = View.GONE
            historyLabel.visibility = View.GONE
            historyRecyclerView.visibility = View.GONE
            hideProgressBar()
        }
    }
    private fun errorHandle(status: RequestState){
        hideProgressBar()
        when(status){
            RequestState.Success ->{
                recyclerView.visibility = View.VISIBLE
                placeholderLayoutError.visibility = View.GONE
            }
            RequestState.NotFound ->{
                placeholderLayoutError.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                refreshBtn.visibility = View.GONE
                placeholderConnectionImage.visibility = View.GONE
                placeholderConnectionText.visibility = View.GONE
                placeholderDownloadText.visibility = View.GONE

                placeholderErrorImage.visibility = View.VISIBLE
                placeholderErrorText.visibility = View.VISIBLE
            }
            RequestState.NotConnected ->{
                recyclerView.visibility = View.GONE
                placeholderErrorImage.visibility = View.GONE
                placeholderErrorText.visibility = View.GONE
                placeholderLayoutError.visibility = View.VISIBLE
                placeholderConnectionImage.visibility = View.VISIBLE
                placeholderConnectionText.visibility = View.VISIBLE
                placeholderDownloadText.visibility = View.VISIBLE
                refreshBtn.visibility = View.VISIBLE
            }
            RequestState.Empty ->{
                updateSearchHistory()
            }
        }

    }
    private fun clickDebounce(): Boolean{
        val currentClick = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return currentClick
    }

    private fun searchDebounce(){
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable,SEARCH_DEBOUNCE_DELAY)
    }
    private fun showProgressBar(){
        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        recyclerView.visibility = View.GONE
        placeholderLayoutError.visibility = View.GONE
        historyLayout.visibility = View.GONE
    }
    private fun hideProgressBar(){
        progressLayout.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }
    sealed interface RequestState {
        data object Empty: RequestState
        data object Success: RequestState
        data object NotConnected: RequestState
        data object NotFound: RequestState
    }
    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}