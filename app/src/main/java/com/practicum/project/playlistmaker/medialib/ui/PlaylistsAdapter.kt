package com.practicum.project.playlistmaker.medialib.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.project.playlistmaker.medialib.model.Playlist

class PlaylistsAdapter (
    private var foundedPlaylists: List<Playlist>,
    private val onClickListener: (Playlist) -> Unit):
    RecyclerView.Adapter<PlaylistsViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(parent)
    }


    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val playlist = foundedPlaylists[position]
        holder.bind(foundedPlaylists[position])

        holder.itemView.setOnClickListener {
            onClickListener(playlist)
        }
    }

    fun updateList(playlist: List<Playlist>){
        this.foundedPlaylists = playlist
        notifyDataSetChanged()
    }
    override fun getItemCount() = foundedPlaylists.size

}