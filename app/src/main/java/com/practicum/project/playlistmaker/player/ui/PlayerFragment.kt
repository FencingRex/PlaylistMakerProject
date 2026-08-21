package com.practicum.project.playlistmaker.player.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.project.playlistmaker.R

import com.practicum.project.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.project.playlistmaker.medialib.model.AddTrackResult
import com.practicum.project.playlistmaker.medialib.model.PlaylistState

import com.practicum.project.playlistmaker.player.model.PlayerState
import com.practicum.project.playlistmaker.player.model.PlayerUiState
import com.practicum.project.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment: Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val track: Track? by lazy {
        arguments?.getParcelable(ARGS_TRACK_KEY)
    }
    private val viewModel by viewModel<PlayerViewModel>{
        parametersOf(track!!.previewUrl,track!!.trackId)
    }
    private lateinit var playlistsAdapter: PlaylistBottomSheetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnClickListener { findNavController().navigateUp() }

        fun Int.dpToPx(context: Context): Int {
            return (this * context.resources.displayMetrics.density).toInt()
        }

        binding.trackName.text = track?.trackName.toString()
        binding.artistName.text = track?.artistName.toString()
        binding.trackDurationValue.text = track?.formatedTime.toString()
        binding.albumNameValue.text = track?.collectionName.toString()
        binding.trackYearValue.text = track?.releaseDate?.take(4)
        binding.trackGenreValue.text = track?.primaryGenreName.toString()
        binding.countryValue.text = track?.country.toString()
        binding.playbackDuration?.text = "00:00"

        binding.favoriteButton.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.addTrackResultLiveData.observe(viewLifecycleOwner){ result ->
            when (result){
                is AddTrackResult.Success -> {
                    val message = "Добавлено в плейлист ${result.playlistName}"
                    Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }

                is AddTrackResult.AlreadyExists -> {
                    val message = "Трек уже добавлен в плейлист ${result.playlistName}"
                    Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                }

                AddTrackResult.PlaylistNotFound -> {
                    Toast.makeText(requireContext(),"", Toast.LENGTH_SHORT).show()
                }

                AddTrackResult.Error -> {
                    Toast.makeText(requireContext(),"", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
        playlistsAdapter = PlaylistBottomSheetAdapter{ viewModel.addTrackToPlaylist(track!!,it)}
        binding.playlistsList.adapter = playlistsAdapter

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomShet: View, slide: Float) {

            }

            override fun onStateChanged(bottomSheet: View, state: Int) {
                when (state) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }
        })
        binding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val radiusInPx = 2.dpToPx(binding.albumCover.context)

        Glide.with(binding.albumCover)
            .load(track?.coverArtwork)
            .placeholder(R.drawable.ic_album_cover_placeholder_light_312)
            .fitCenter()
            .transform(RoundedCorners(radiusInPx))
            .into(binding.albumCover)

        binding.playButton.setOnClickListener { viewModel.playbackControl() }
        viewModel.playerUIState.observe(viewLifecycleOwner) { playerUIState ->
            render(playerUIState)
        }
        viewModel.getPlaylistState().observe(viewLifecycleOwner){
            renderPlaylists(it)
        }
        binding.btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }
    }
    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(uiState: PlayerUiState) {
        when (uiState.status) {
            PlayerState.STATE_DEFAULT -> {}
            PlayerState.STATE_PREPARED -> {
                binding.playButton.isEnabled = true
                binding.playbackDuration?.text = "00:00"
                binding.playButton.setImageResource(R.drawable.ic_button_play_100)
            }
            PlayerState.STATE_PLAYING -> {
                binding.playButton.setImageResource(R.drawable.ic_button_pause_100)
                binding.playbackDuration.text = uiState.currentPosition
            }
            PlayerState.STATE_PAUSED -> {
                binding.playButton.setImageResource(R.drawable.ic_button_play_100)
                binding.playbackDuration.text = uiState.currentPosition
            }
        }
        if (uiState.isFavorite) {
            binding.favoriteButton.setImageResource(R.drawable.ic_button_favorite)
        } else
            binding.favoriteButton.setImageResource(R.drawable.ic_button_add_favorite_51)
    }

    private fun renderPlaylists(state: PlaylistState){
        when (state) {
            is PlaylistState.Content -> {
                playlistsAdapter.submitList(state.playlist)
            }
            is PlaylistState.Empty -> {
                playlistsAdapter.submitList(emptyList())
            }
        }
    }

    companion object{
        const val ARGS_TRACK_KEY = "track"
        fun createArgs(track: Track): Bundle =
            Bundle().apply {
                putParcelable(ARGS_TRACK_KEY, track)
            }
    }
}
