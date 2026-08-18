package com.practicum.project.playlistmaker.medialib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.project.playlistmaker.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.project.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.project.playlistmaker.medialib.model.Playlist
import com.practicum.project.playlistmaker.medialib.model.PlaylistState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {
    private val viewModel by viewModel<PlaylistsViewModel>()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() =  _binding!!
    private lateinit var adapter: PlaylistsAdapter
    private val playlists: MutableList<Playlist> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }
        setRecyclerView()
        viewModel.playlistState.observe(viewLifecycleOwner){state ->
            when(state){
                is PlaylistState.Empty ->{
                    binding.placeholderImage.visibility = View.VISIBLE
                    binding.placeholderMessage.visibility = View.VISIBLE

                    binding.foundedPlaylist.visibility = View.GONE

                }
                is PlaylistState.Content ->{
                    binding.foundedPlaylist.visibility = View.VISIBLE

                    binding.placeholderImage.visibility = View.GONE
                    binding.placeholderMessage.visibility = View.GONE

                    adapter.updateList(state.playlist)
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerView(){

        adapter = PlaylistsAdapter(playlists) { playlist ->
            if (viewModel.clickDebounce(playlist)) {
                val action = findNavController().navigate(R.id.action_mediaLibraryFragment_to_playerFragment)
            }
        }
        binding.foundedPlaylist.layoutManager = GridLayoutManager(requireContext(),2)
        binding.foundedPlaylist.adapter = adapter
        binding.foundedPlaylist.setHasFixedSize(true)
    }
    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}