package com.spotifysearch

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spotifysearch.databinding.TopTrackItemBinding
import com.spotifysearch.toptracks.Track
import com.squareup.picasso.Picasso

class TopTrackAdapter(val list:List<Track>):RecyclerView.Adapter<TopTrackAdapter.ViewHolder>() {
    class ViewHolder(binding: TopTrackItemBinding):RecyclerView.ViewHolder(binding.root){
        val tvAlbum = binding.tvAlbum
        val tvName = binding.tvName
        val tvLink = binding.tvLink
        val ivAlbumCover = binding.ivAlbumcover
        val id = binding.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TopTrackItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.id.text = (position+1).toString()
        Picasso.get().load(list[position].album.images[0].url).into(holder.ivAlbumCover)
        holder.tvAlbum.text = item.album.name
        holder.tvName.text = item.name
        holder.tvLink.text = item.external_urls.spotify

    }

    override fun getItemCount(): Int {
        return list.size
    }
}