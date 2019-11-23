package com.forasoft.albums.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.util.*

data class Album(
    val artistName: String,
    val name: String,
    val posterUrl: String,
    val releaseDate: Date,
    val genre: String
) {
    companion object {
        @JvmStatic
        @BindingAdapter("albumPoster")
        fun loadImage(view: ImageView, imageUrl: String) {
            Glide.with(view)
                .load(imageUrl)
                .thumbnail(0.1f)
                .into(view)
        }
    }

    fun getYear(): String {
        val calendar = Calendar.getInstance()
        calendar.time = releaseDate
        return calendar.get(Calendar.YEAR).toString()
    }

    override fun toString(): String {
        return "Album(artistName='$artistName', name='$name', posterUrl='$posterUrl', releaseDate=$releaseDate, genre='$genre')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Album

        if (artistName != other.artistName) return false
        if (name != other.name) return false
        if (posterUrl != other.posterUrl) return false
        if (releaseDate != other.releaseDate) return false
        if (genre != other.genre) return false

        return true
    }

    override fun hashCode(): Int {
        var result = artistName.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + posterUrl.hashCode()
        result = 31 * result + releaseDate.hashCode()
        result = 31 * result + genre.hashCode()
        return result
    }

}