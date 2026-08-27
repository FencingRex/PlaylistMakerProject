package com.practicum.project.playlistmaker.playlist.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.project.playlistmaker.medialib.model.Playlist
import com.practicum.project.playlistmaker.player.ui.PlayerFragment
import com.practicum.project.playlistmaker.playlist.model.PlaylistEditState
import com.practicum.project.playlistmaker.search.domain.models.Track
import com.practicum.project.playlistmaker.search.ui.SearchAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class PlaylistFragment : Fragment(){

   private var _binding: FragmentPlaylistBinding? = null
   private val binding get() = _binding!!
   private lateinit var  trackAdapter: SearchAdapter

   private val playlist: Playlist? by lazy{
      arguments?.getParcelable(ARGS_PLAYLIST_KEY)
   }
   private val viewModel by viewModel<PlaylistViewModel>{parametersOf(playlist!!.id)}

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      _binding = FragmentPlaylistBinding.inflate(inflater,container,false)
      return binding.root

   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      val bottomSheetBehavior = BottomSheetBehavior.from(binding.editPlaylistBottomSheet).apply {
         state = BottomSheetBehavior.STATE_HIDDEN
      }
      binding.btnMore.setOnClickListener {
         bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
      }
      binding.arrowBack.setOnClickListener { findNavController().navigateUp() }

      binding.playlistName.text = playlist?.name
      binding.playlistDescription.text = playlist?.description
      if (!playlist?.coverUri.isNullOrEmpty()){
         binding.playlistCover.setImageURI(Uri.fromFile(File(playlist?.coverUri)))
         Log.d("filepath","${playlist?.coverUri}")
      }
      trackAdapter = SearchAdapter(ArrayList(),
         onClickListener = {track -> onClickEvent(track)},
         onLongClickListener = {track -> onLongClickEvent(track)} )

      binding.playlistTracks.adapter = trackAdapter
      viewModel.playlistLiveData.observe(viewLifecycleOwner){
         render(it)
      }
      bottomSheetBehavior.addBottomSheetCallback( object : BottomSheetBehavior.BottomSheetCallback(){
         override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
               BottomSheetBehavior.STATE_HIDDEN -> {
                  binding.overlay.isVisible = false
               }

               else -> {
                  binding.overlay.isVisible = true
               }
            }
         }
         override fun onSlide(bottomSheet: View, slideOffset: Float) {
         }
      })
      viewModel.getPlaylistData()

      binding.deletePlaylist.setOnClickListener {
         deletePlaylist()
      }
      binding.btnShare.setOnClickListener {
         sharePlaylist()
      }
      binding.sharePlaylist.setOnClickListener {
         sharePlaylist()
      }
      binding.editPlaylist.setOnClickListener {
         editPlaylist()
      }
      
   }
   private fun editPlaylist(){
      val playlistId = playlist!!.id
      val bundle = Bundle().apply {
         putLong(PlaylistFragment.ARGS_PLAYLIST_KEY, playlistId)
      }
      val action = findNavController().navigate(R.id.action_playlistFragment_to_editPlaylistFragment,bundle)
   }
   private fun deletePlaylist(){
      AlertDialog.Builder(requireContext())
         .setMessage(getString(R.string.deletePlaylistConfirm))
         .setPositiveButton(getString(R.string.yes)) { dialog, which ->
            viewModel.deletePlaylist()
            findNavController().popBackStack()
         }
         .setNegativeButton(getString(R.string.no), null)
         .show()
   }
   private fun sharePlaylist(){
      viewModel.playlistLiveData.value?.let { state ->
         if (state.tracks.isNotEmpty()) {
            sharingMessagePlaylist(state)
         } else {
            Toast.makeText(
               requireContext(),
               getString(R.string.shareEmptyPlaylist),
               Toast.LENGTH_SHORT
            ).show()
         }
      }
   }
   private fun sharingMessagePlaylist(state: PlaylistEditState) {
      val tracksCount = tracksQtyText(requireContext(),state.tracksCount)

      val message = buildString {
         appendLine(state.name)
         if (!state.description.isNullOrBlank()) appendLine(state.description)
         appendLine(tracksCount)
         appendLine()
         state.tracks.forEachIndexed { index, tracks ->
            appendLine("${index + 1}. ${tracks.artistName} - ${tracks.trackName} - ${tracks.formatedTime}.")
         }
      }
      viewModel.sharePlaylist(message, getString(R.string.sharePlaylist))
   }
   private fun onClickEvent(track: Track) {
      val bundle = Bundle().apply {
         putParcelable(PlayerFragment.ARGS_TRACK_KEY, track)
      }
      if (viewModel.clickDebounce(track)){
         val action = findNavController().navigate(R.id.action_playlistFragment_to_playerFragment,bundle)
      }
   }
   private fun onLongClickEvent(track: Track) {
      MaterialAlertDialogBuilder(requireContext(),R.style.DeleteTrackConfirm)
         .setMessage(R.string.deleteTrack)
         .setNegativeButton(R.string.no, null)
         .setPositiveButton(R.string.yes) { dialog, which ->
            viewModel.deleteTrack(track.trackId)
         }
         .show()
   }

   fun tracksQtyText(context: Context, quantity: Int): String{
      return context.resources.getQuantityString(R.plurals.tracks_count,quantity,quantity)
   }

   fun tracksDurationText(context: Context, quantity: Int): String{
      return context.resources.getQuantityString(R.plurals.track_duration,quantity,quantity)
   }
   private fun render(state: PlaylistEditState) {

      val tracksCount = tracksQtyText(requireContext(),state.tracksCount)
      val tracksDuration = tracksDurationText(requireContext(), state.totalDurationTracks.toIntOrNull() ?: 0)
      
      with(binding) {
         playlistName.text = state.name
         playlistView.playlistName.text = state.name

         if (state.description.isNullOrBlank()) {
            playlistDescription.isVisible = false
         } else {
            playlistDescription.text = state.description
            playlistDescription.isVisible = true
         }

         totalTracksCount.text = tracksCount
         playlistView.tracksQty.text = tracksCount

         totalDuration.text = tracksDuration

         if (state.image.isNullOrBlank()) {
            binding.playlistCover.scaleType = ImageView.ScaleType.CENTER_INSIDE
         } else {
            binding.playlistCover.scaleType = ImageView.ScaleType.CENTER_CROP
         }

         Glide.with(this@PlaylistFragment)
            .load(state.image)
            .placeholder(R.drawable.ic_album_cover_placeholder_light_312)
            .into(playlistCover)

         Glide.with(this@PlaylistFragment)
            .load(state.image)
            .placeholder(R.drawable.ic_cover_placeholder_34)
            .into(playlistView.ivCover)

         trackAdapter.updateList(ArrayList(state.tracks))
         if (state.tracks.isNullOrEmpty()) {
            emptyPlaylist.isVisible = true
            playlistTracks.isVisible = false
         } else {
            emptyPlaylist.isVisible = false
            playlistTracks.isVisible = true
         }
      }
   }
   companion object{
      const val ARGS_PLAYLIST_KEY = "playlist"
      fun createArgs(playlist: Playlist): Bundle =
         Bundle().apply {
            putParcelable(ARGS_PLAYLIST_KEY, playlist)
         }
   }
}