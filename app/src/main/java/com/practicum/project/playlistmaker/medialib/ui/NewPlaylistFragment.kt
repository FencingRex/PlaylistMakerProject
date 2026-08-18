package com.practicum.project.playlistmaker.medialib.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.activity.OnBackPressedCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.databinding.NewPlaylistFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import kotlin.getValue

class NewPlaylistFragment: Fragment() {
    private val viewModel by viewModel<PlaylistsViewModel>()
    private var _binding: NewPlaylistFragmentBinding? = null
    private val binding get() =  _binding!!
    private var playlistName: String = ""
    private var playlistDescription: String = ""
    private var coverSelected: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = NewPlaylistFragmentBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnClickListener { handleBackNavigation() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackNavigation()
            }
        })

        viewModel.savedCoverPath.observe(viewLifecycleOwner){ path ->
            path?.let {
                binding.albumCover.setImageURI(Uri.fromFile(File(it)))
            }
        }

        val simpleTextWatcherName = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.createPlaylistBtn.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                playlistName = s?.toString() ?: ""
            }
        }

        val simpleTextWatcherDescription = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                playlistDescription = s?.toString() ?: ""
            }
        }

        simpleTextWatcherName.let { binding.editTextName.addTextChangedListener(it) }
        simpleTextWatcherDescription.let { binding.editTextDescription.addTextChangedListener(it) }


        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.albumCover.setImageURI(uri)
                    viewModel.saveCover(uri)
                    coverSelected = true
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        binding.albumCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        viewModel
        }

        binding.createPlaylistBtn.setOnClickListener {
            viewModel.createPlaylist(playlistName,playlistDescription)
            findNavController().navigateUp()
            showToastMessage("Плейлист ${playlistName} создан")
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun handleBackNavigation() {
        if (hasUnsavedChanges()) {
            confirmDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun hasUnsavedChanges(): Boolean {
        return playlistName.isNotEmpty() || playlistDescription.isNotEmpty() || coverSelected
    }

    private fun confirmDialog() {
        MaterialAlertDialogBuilder(requireContext(),R.style.confirmMessage)
            .setTitle(R.string.confirmMessageHeader)
            .setMessage(R.string.confirmMessage)
            .setPositiveButton(R.string.confirmButton) { dialog, which ->
                findNavController().navigateUp()
            }
            .setNeutralButton(R.string.confirmCancel) { dialog, which ->

            }
            .show()
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}