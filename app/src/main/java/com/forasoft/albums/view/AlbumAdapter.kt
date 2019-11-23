package com.forasoft.albums.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.forasoft.albums.databinding.AlbumViewHolderBinding
import com.forasoft.albums.viewmodel.Album

/**
 * Adapts albums to be shown in {@link RecyclerView}
 */
class AlbumAdapter(private val listener: (Album) -> Unit) :
    RecyclerView.Adapter<AlbumViewHolder>() {

    private val differ = AsyncListDiffer<Album>(this, object : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlbumViewHolderBinding.inflate(inflater, parent, false)
        return AlbumViewHolder(binding)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) =
        holder.setData(differ.currentList[position], listener)

    fun updateData(albums: List<Album>) = differ.submitList(albums)
}