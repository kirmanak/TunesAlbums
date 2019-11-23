package com.forasoft.albums.model.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

/**
 * Represents a music album in persistent storage
 */
@Entity(
    foreignKeys = [ForeignKey(
        entity = SearchRequestEntity::class,
        parentColumns = ["uid"],
        childColumns = ["search_id"],
        onDelete = CASCADE
    )]
)
data class AlbumEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid", index = true) val uid: Long,
    @ColumnInfo(name = "remote_id") val remoteId: Long,
    @ColumnInfo(name = "artist_name") val artistName: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "poster_name") val posterUrl: String,
    @ColumnInfo(name = "release_date") val releaseDate: Date,
    @ColumnInfo(name = "genre") val genre: String,
    @ColumnInfo(name = "search_id", index = true) val searchId: Long
) {
    override fun toString(): String {
        return "AlbumEntity(uid=$uid, remoteId=$remoteId, artistName='$artistName', name='$name', posterUrl='$posterUrl', releaseDate=$releaseDate, genre='$genre', searchId=$searchId)"
    }
}
