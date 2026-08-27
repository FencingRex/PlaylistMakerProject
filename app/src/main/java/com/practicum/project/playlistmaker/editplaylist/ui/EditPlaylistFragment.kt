package com.practicum.project.playlistmaker.editplaylist.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.medialib.ui.NewPlaylistFragment
import com.practicum.project.playlistmaker.playlist.ui.PlaylistFragment.Companion.ARGS_PLAYLIST_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class EditPlaylistFragment: NewPlaylistFragment() {
    private val playlistId by lazy{ requireArguments().getLong(ARGS_PLAYLIST_KEY)}

    override val viewModel by viewModel<EditPlaylistViewModel> {
        parametersOf(playlistId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnClickListener { findNavController().popBackStack() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {findNavController().popBackStack()}

        binding.createPlaylistBtn.setOnClickListener {
            viewModel.playlistUpdate(
                name = binding.editTextName.text.toString(),
                description = binding.editTextDescription.text.toString()
            )
            findNavController().popBackStack()
        }
        viewModel.playlistLiveDataState.observe(viewLifecycleOwner) { playlist ->
            playlist?.let { value ->
                with(binding) {
                    if (!value.coverUri.isNullOrEmpty()) {
                        albumCover.setImageURI(Uri.fromFile(File(value.coverUri)))
                    }
                    editTextName.setText(value.name)
                    editTextDescription.setText(value.description)
                    toolbar.setTitle(getString(R.string.editPlaylistHeader))
                    createPlaylistBtn.setText(getString(R.string.saveChanges))
                }
            }
        }

    }
}