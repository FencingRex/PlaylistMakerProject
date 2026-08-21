package com.practicum.project.playlistmaker.player.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.practicum.project.playlistmaker.databinding.PlaylistBottomViewSheetBinding
import com.practicum.project.playlistmaker.medialib.model.Playlist


class PlaylistBottomSheetAdapter(private val clickListener: PlaylistClickListener) :
    ListAdapter<Playlist, PlaylistBottomSheetViewHolder>(
        object : DiffUtil.ItemCallback<Playlist>() {
            override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                return oldItem == newItem
            }

        }) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistBottomSheetViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistBottomSheetViewHolder(
            PlaylistBottomViewSheetBinding.inflate(
                layoutInspector, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaylistBottomSheetViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickListener.onPlaylistClick(getItem(position))
        }
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}