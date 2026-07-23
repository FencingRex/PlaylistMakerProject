package com.practicum.project.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.project.playlistmaker.creator.Creator
import com.practicum.project.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.project.playlistmaker.search.domain.TracksInteractor
import com.practicum.project.playlistmaker.search.domain.models.Track
import com.practicum.project.playlistmaker.player.ui.PlayerActivity

class SearchActivity : AppCompatActivity() {
    private var searchQuery: String = ""
    private lateinit var adapter: SearchAdapter
    private val trackList: MutableList<Track> = mutableListOf()
    private lateinit var historyAdapter: SearchAdapter
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding
    private val tracksInteractor = Creator.provideTrackInteractor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]

        binding.toolbar.setOnClickListener { finish() }

        binding.progress.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        binding.cleanHistory.setOnClickListener {
            tracksInteractor.clearHistory()
            updateSearchHistory()
        }

        binding.clearIcon.setOnClickListener {
            binding.searchInputText.setText("")
            trackList.clear()
            adapter.notifyDataSetChanged()

            updateSearchHistory()
            if(tracksInteractor.isNotEmpty()) {
                binding.historyHeader.visibility = View.VISIBLE
                binding.history.visibility = View.VISIBLE
                binding.cleanHistory.visibility = View.VISIBLE
            }
            binding.searchResults.visibility = View.GONE
            binding.layoutErrorPlaceholder.visibility = View.GONE

            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearIcon.windowToken, 0)
        }

        binding.searchInputText.setOnFocusChangeListener{ _, hasFocus ->
            if (hasFocus && binding.searchInputText.text.isEmpty() && tracksInteractor.isNotEmpty()){
                updateSearchHistory()
                binding.historyHeader.visibility = View.VISIBLE
                binding.history.visibility = View.VISIBLE
                binding.cleanHistory.visibility = View.VISIBLE
            } else {
                binding.historyHeader.visibility = View.GONE
                binding.history.visibility = View.GONE
                binding.cleanHistory.visibility = View.GONE
            }
        }
        setRecyclerView()

        if (binding.searchInputText.text.isEmpty()){
            updateSearchHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.isVisible = !s.isNullOrEmpty()
                searchDebounce()
                if (s.isNullOrEmpty()){
                    updateSearchHistory()
                    handler.removeCallbacks(searchRunnable)
                    binding.searchResults.visibility = View.GONE
                    binding.layoutErrorPlaceholder.visibility = View.GONE
                    binding.progress.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                } else{
                    binding.historyHeader.visibility = View.GONE
                    binding.history.visibility = View.GONE
                    binding.cleanHistory.visibility = View.GONE
                    binding.progress.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
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
        binding.searchInputText.addTextChangedListener(simpleTextWatcher)

        binding.searchInputText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (binding.searchInputText.text.isNotEmpty()){
                        searchDebounce()
                        searchTrack(binding.searchInputText.text.toString())
                        setRecyclerView()
                    }
                }
                false
            }
        binding.refreshButton.setOnClickListener {
            searchTrack(binding.searchInputText.text.toString())
        }

    }
    private val searchRunnable = Runnable {searchTrack(binding.searchInputText.text.toString())}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("savedSearchReq", binding.searchInputText.getText().toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString("savedSearchReq", "")
        binding.searchInputText.setText(searchQuery)
    }
    private fun setRecyclerView(){

        adapter = SearchAdapter(trackList) {
            if (clickDebounce()) {
                tracksInteractor.addTrackToHistory(it)
                val trackIntent =
                    Intent(this, PlayerActivity::class.java).apply { putExtra("Track", (it)) }
                startActivity(trackIntent)
            }
        }
        historyAdapter = SearchAdapter(mutableListOf()) {
            if (clickDebounce()) {
                tracksInteractor.addTrackToHistory(it)
                val trackIntent =
                    Intent(this, PlayerActivity::class.java).apply { putExtra("Track", (it)) }
                startActivity(trackIntent)
            }
        }
        binding.searchResults.layoutManager = LinearLayoutManager(this)
        binding.searchResults.adapter = adapter
        binding.searchResults.setHasFixedSize(true)

        binding.searchedTracks.layoutManager = LinearLayoutManager(this)
        binding.searchedTracks.adapter = historyAdapter
    }
    private fun searchTrack(searchValue: String){
        showProgressBar()
        tracksInteractor.searchTracks(searchValue, object: TracksInteractor.TracksConsumer{
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
    }
    private fun updateSearchHistory(){
        val historyTrackList = tracksInteractor.getTrackFromHistory()

        if (historyTrackList.isNotEmpty() && binding.searchInputText.text.isEmpty() && binding.searchInputText.hasFocus()){
            historyAdapter.updateList(historyTrackList)

            binding.searchResults.visibility = View.GONE
            binding.layoutErrorPlaceholder.visibility = View.GONE
            hideProgressBar()

            binding.history.visibility = View.VISIBLE
            binding.cleanHistory.visibility = View.VISIBLE
            binding.history.visibility = View.VISIBLE
            binding.searchedTracks.visibility = View.VISIBLE
        } else {
            binding.history.visibility = View.GONE
            binding.cleanHistory.visibility = View.GONE
            binding.history.visibility = View.GONE
            binding.searchedTracks.visibility = View.GONE
            hideProgressBar()
        }
    }
    private fun errorHandle(status: RequestState){
        if (isFinishing || isDestroyed) return
        hideProgressBar()
        when(status){
            RequestState.Success ->{
                binding.searchResults.visibility = View.VISIBLE
                binding.layoutErrorPlaceholder.visibility = View.GONE
            }
            RequestState.NotFound ->{
                binding.layoutErrorPlaceholder.visibility = View.VISIBLE
                binding.searchResults.visibility = View.GONE
                binding.refreshButton.visibility = View.GONE
                binding.placeholderConnectionImage.visibility = View.GONE
                binding.placeholderConnectionText.visibility = View.GONE
                binding.placeholderDownloadText.visibility = View.GONE

                binding.placeholderNotFoundText.visibility = View.VISIBLE
                binding.placeholderNotFoundImage.visibility = View.VISIBLE
            }
            RequestState.NotConnected ->{
                binding.searchResults.visibility = View.GONE
                binding.placeholderNotFoundImage.visibility = View.GONE
                binding.placeholderNotFoundText.visibility = View.GONE
                binding.layoutErrorPlaceholder.visibility = View.VISIBLE
                binding.placeholderConnectionImage.visibility = View.VISIBLE
                binding.placeholderConnectionText.visibility = View.VISIBLE
                binding.placeholderDownloadText.visibility = View.VISIBLE
                binding.refreshButton.visibility = View.VISIBLE
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
        binding.progress.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE

        binding.searchResults.visibility = View.GONE
        binding.layoutErrorPlaceholder.visibility = View.GONE
        binding.history.visibility = View.GONE
    }
    private fun hideProgressBar(){
        binding.progress.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
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