package com.practicum.project.playlistmaker.player.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.project.playlistmaker.R

import com.practicum.project.playlistmaker.databinding.FragmentPlayerBinding

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
        parametersOf(track!!.previewUrl)
    }

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
    }
    companion object{
        const val ARGS_TRACK_KEY = "track"
        fun createArgs(track: Track): Bundle =
            Bundle().apply {
                putParcelable(ARGS_TRACK_KEY, track)
            }
    }
}
