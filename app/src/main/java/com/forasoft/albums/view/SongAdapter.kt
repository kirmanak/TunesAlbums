package com.forasoft.albums.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.forasoft.albums.databinding.SongViewHolderBinding
import com.forasoft.albums.viewmodel.Song

class SongAdapter : RecyclerView.Adapter<SongViewHolder>() {
    private val differ = AsyncListDiffer<Song>(this, object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.songName == newItem.songName
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SongViewHolderBinding.inflate(inflater)
        return SongViewHolder(binding)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) =
        holder.setData(differ.currentList[position])

    fun setData(songs: List<Song>) = differ.submitList(songs)
}