package com.practicum.project.playlistmaker.player.ui

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.databinding.PlaylistBottomViewSheetBinding
import com.practicum.project.playlistmaker.medialib.model.Playlist

class PlaylistBottomSheetViewHolder(private val binding: PlaylistBottomViewSheetBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val radiusInPx = 2.dpToPx(itemView.context)
    fun tracksQtyText(context: Context,quantity: Int): String{
        return context.resources.getQuantityString(R.plurals.tracks_count,quantity,quantity)
    }
    fun bind(item: Playlist) {
        binding.playlistName.text = item.name
        binding.tracksQty.text = tracksQtyText(itemView.context, item.tracksQty)

        Glide.with(itemView)
            .load(item.coverUri)
            .transform(CenterCrop(), RoundedCorners(radiusInPx))
            .placeholder(R.drawable.ic_cover_placeholder_34)
            .into(binding.ivCover)
    }
    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}