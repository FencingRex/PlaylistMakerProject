package com.practicum.project.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.content.Context
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class SearchViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_search_result_item,parent,false)) {
    private val coverView: ImageView = itemView.findViewById(R.id.cover)
    private val trackView: TextView = itemView.findViewById(R.id.trackName)
    private val artistView: TextView = itemView.findViewById(R.id.artistName)
    private val trackTimeView: TextView = itemView.findViewById(R.id.trackTime)
    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
    fun bind (track: Track){
        trackView.text = track.trackName
        artistView.text = track.artistName
        trackTimeView.text = formatedTime(track.trackTimeMillis)

        val radiusInPx = 2.dpToPx(itemView.context)

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_cover_placeholder_34)
            .fitCenter()
            .transform(RoundedCorners(radiusInPx))
            .into(coverView)
    }
    private fun formatedTime(timeLong: Long): String{
        return (SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeLong))
    }
}
