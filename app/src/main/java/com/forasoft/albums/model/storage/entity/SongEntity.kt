package com.forasoft.albums.model.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = AlbumEntity::class,
        parentColumns = ["uid"],
        childColumns = ["album_id"],
        onDelete = CASCADE
    )]
)
data class SongEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid", index = true) val uid: Long,
    @ColumnInfo(name = "album_id", index = true) val albumId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "previewUrl") val previewUrl: String,
    @ColumnInfo(name = "track_time_ms") val trackTimeMillis: Long,
    @ColumnInfo(name = "remote_id") val remoteId: Long

)