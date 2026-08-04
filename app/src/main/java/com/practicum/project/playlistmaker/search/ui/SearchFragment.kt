package com.practicum.project.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.project.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.project.playlistmaker.player.ui.PlayerFragment
import android.view.inputmethod.EditorInfo
import com.practicum.project.playlistmaker.search.domain.models.Track
import com.practicum.project.playlistmaker.search.model.RequestState
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import com.practicum.project.playlistmaker.R

class SearchFragment : Fragment() {
    private var searchQuery: String = EMPTY_TEXT
    private lateinit var adapter: SearchAdapter
    private val trackList: MutableList<Track> = mutableListOf()
    private lateinit var historyAdapter: SearchAdapter
    private val viewModel by viewModel<SearchViewModel>()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.getString(SEARCH_KEY)?.let {
            binding.searchInputText.setText(it)
            searchQuery = it
        }

        viewModel.requestState.observe(viewLifecycleOwner){state ->
            when (state) {
                is RequestState.Loading -> showProgressBar()
                is RequestState.Success -> {
                    hideProgressBar()
                    trackList.clear()
                    trackList.addAll(state.tracks)
                    adapter.notifyDataSetChanged()
                    binding.searchResults.visibility = View.VISIBLE
                    binding.layoutErrorPlaceholder.visibility = View.GONE
                }
                is RequestState.NotFound -> {
                    hideProgressBar()
                    showNotFoundPlaceholder()
                }
                is RequestState.NotConnected -> {
                    hideProgressBar()
                    showNotConnectedPlaceholder()
                }
                is RequestState.Empty -> {
                    hideProgressBar()
                    updateSearchHistory()
                }
            }
        }

        binding.progress.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        binding.cleanHistory.setOnClickListener {
            viewModel.clearHistory()
            updateSearchHistory()
        }

        binding.clearIcon.setOnClickListener {
            binding.searchInputText.setText("")
            trackList.clear()
            adapter.notifyDataSetChanged()

            hideProgressBar()

            updateSearchHistory()

            if(viewModel.getHistory().isNotEmpty()) {
                binding.historyHeader.visibility = View.VISIBLE
                binding.history.visibility = View.VISIBLE
                binding.cleanHistory.visibility = View.VISIBLE
            }
            binding.searchResults.visibility = View.GONE
            binding.layoutErrorPlaceholder.visibility = View.GONE

            val inputMethodManager =
               requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.clearIcon.windowToken, 0)
        }

        binding.searchInputText.setOnFocusChangeListener{ _, hasFocus ->
            if (hasFocus && binding.searchInputText.text.isEmpty() && viewModel.getHistory().isNotEmpty()){
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

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.isVisible = !s.isNullOrEmpty()
                if (s.isNullOrEmpty()){
                    updateSearchHistory()
                    binding.searchResults.visibility = View.GONE
                    binding.layoutErrorPlaceholder.visibility = View.GONE
                    binding.progress.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                } else{
                    binding.historyHeader.visibility = View.GONE
                    binding.history.visibility = View.GONE
                    binding.cleanHistory.visibility = View.GONE
                    binding.progress.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    viewModel.searchDebounce(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
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
                        searchTrack(binding.searchInputText.text.toString())
                    }
                }
                false
            }
        binding.refreshButton.setOnClickListener {
            searchTrack(binding.searchInputText.text.toString())
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding?.let{outState.putString(SEARCH_KEY, binding.searchInputText.getText().toString())}
    }
    private fun setRecyclerView(){

        adapter = SearchAdapter(trackList) { track ->
            if (viewModel.clickDebounce(track)) {
                val action = findNavController().navigate(R.id.action_searchFragment_to_playerFragment, PlayerFragment.createArgs(track))
            }
        }

        historyAdapter = SearchAdapter(mutableListOf()) { track ->
            if (viewModel.clickDebounce(track)) {
                val action = findNavController().navigate(R.id.action_searchFragment_to_playerFragment, PlayerFragment.createArgs(track))
            }
        }
        binding.searchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.searchResults.adapter = adapter
        binding.searchResults.setHasFixedSize(true)

        binding.searchedTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.searchedTracks.adapter = historyAdapter
    }
    private fun searchTrack(searchValue: String){
        viewModel.searchTrack(searchValue)
    }
    private fun updateSearchHistory(){
        val historyTrackList = viewModel.getHistory()

        if (historyTrackList.isNotEmpty() && binding.searchInputText.text.isEmpty() && binding.searchInputText.hasFocus()){
            historyAdapter.updateList(historyTrackList)

            binding.searchResults.visibility = View.GONE
            binding.layoutErrorPlaceholder.visibility = View.GONE
            hideProgressBar()

            binding.historyHeader.visibility = View.VISIBLE
            binding.cleanHistory.visibility = View.VISIBLE
            binding.history.visibility = View.VISIBLE
            binding.searchedTracks.visibility = View.VISIBLE
        } else {
            binding.historyHeader.visibility = View.GONE
            binding.cleanHistory.visibility = View.GONE
            binding.history.visibility = View.GONE
            binding.searchedTracks.visibility = View.GONE
            hideProgressBar()
        }
    }
    private fun showNotFoundPlaceholder() {
        binding.layoutErrorPlaceholder.visibility = View.VISIBLE
        binding.searchResults.visibility = View.GONE
        binding.refreshButton.visibility = View.GONE
        binding.placeholderConnectionImage.visibility = View.GONE
        binding.placeholderConnectionText.visibility = View.GONE
        binding.placeholderDownloadText.visibility = View.GONE
        binding.placeholderNotFoundText.visibility = View.VISIBLE
        binding.placeholderNotFoundImage.visibility = View.VISIBLE
    }
    private fun showNotConnectedPlaceholder() {
        binding.layoutErrorPlaceholder.visibility = View.VISIBLE
        binding.searchResults.visibility = View.GONE
        binding.placeholderNotFoundImage.visibility = View.GONE
        binding.placeholderNotFoundText.visibility = View.GONE
        binding.placeholderConnectionImage.visibility = View.VISIBLE
        binding.placeholderConnectionText.visibility = View.VISIBLE
        binding.placeholderDownloadText.visibility = View.VISIBLE
        binding.refreshButton.visibility = View.VISIBLE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object{
        private const val SEARCH_KEY = "search_key"
        private const val EMPTY_TEXT = ""
    }
}