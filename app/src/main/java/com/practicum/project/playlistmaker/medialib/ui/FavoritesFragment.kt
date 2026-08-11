package com.practicum.project.playlistmaker.medialib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.project.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.project.playlistmaker.search.ui.SearchAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.medialib.model.FavoritesState
import com.practicum.project.playlistmaker.player.ui.PlayerFragment
import com.practicum.project.playlistmaker.search.domain.models.Track

class FavoritesFragment: Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FavoritesViewModel>()
    private lateinit var favoritesAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerVew()

        viewModel.favoritesState.observe(viewLifecycleOwner){state ->
            when(state) {
                is FavoritesState.Empty -> showEmpty()
                is FavoritesState.Content -> {
                    showFavoritesList()
                    favoritesAdapter.updateList(state.tracks)
                }
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerVew(){
        favoritesAdapter = SearchAdapter(mutableListOf()) { track ->
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }
        binding.favoritesTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesTracks.adapter = favoritesAdapter
    }

    private fun showEmpty(){
        binding.placeholderContainer.isVisible = true
        binding.placeholderImage.isVisible = true
        binding.placeholderMessage.isVisible = true
        binding.favoritesTracks.isVisible = false
    }

    private fun showFavoritesList(){
        binding.favoritesTracks.isVisible = true
        binding.placeholderContainer.isVisible = false
        binding.placeholderImage.isVisible = false
        binding.placeholderMessage.isVisible = false
    }
}