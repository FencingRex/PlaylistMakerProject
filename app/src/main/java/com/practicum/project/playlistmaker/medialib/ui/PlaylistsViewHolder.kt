package com.practicum.project.playlistmaker.medialib.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.medialib.model.Playlist

class PlaylistsViewHolder(parent: ViewGroup): RecyclerView.ViewHolder (
    LayoutInflater.from(parent.context).inflate(
        R.layout.playlist_result_item,parent,false)){
    private val coverView: ImageView = itemView.findViewById(R.id.playlistCover)
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val qtyTracks: TextView = itemView.findViewById(R.id.playlistTracksQty)
    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
    fun tracksQtyText(context: Context,quantity: Int): String{
        return context.resources.getQuantityString(R.plurals.tracks_count,quantity,quantity)
    }
    fun bind (playlist: Playlist){
        playlistName.text = playlist.name
        qtyTracks.text = tracksQtyText(itemView.context, playlist.tracksQty)

        val radiusInPx = 2.dpToPx(itemView.context)

        Glide.with(itemView)
            .load(playlist.coverUri)
            .placeholder(R.drawable.ic_cover_placeholder_34)
            .fitCenter()
            .transform(CenterCrop(),RoundedCorners(radiusInPx))
            .into(coverView)
    }
}