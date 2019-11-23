package com.forasoft.albums.model.network

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Generic object returned from any call to iTunes API
 */
data class TunesItem(
    @SerializedName("collectionId") val collectionId: Long?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("collectionName") val collectionName: String?,
    @SerializedName("artworkUrl100") val artworkUrl100: String?,
    @SerializedName("releaseDate") val releaseDate: Date?,
    @SerializedName("primaryGenreName") val primaryGenreName: String?
) {
    override fun toString(): String {
        return "TunesItem(collectionId=$collectionId, artistName=$artistName, collectionName=$collectionName, artworkUrl100=$artworkUrl100, releaseDate=$releaseDate, primaryGenreName=$primaryGenreName)"
    }
}