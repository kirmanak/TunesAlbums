package com.forasoft.albums.model.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.forasoft.albums.model.storage.entity.SongEntity
import javax.inject.Singleton

/**
 * Dao to access songs info
 */
@Dao
@Singleton
interface SongDao {
    @Query("SELECT * FROM SongEntity WHERE SongEntity.album_id = :albumId")
    fun getAllByAlbumId(albumId: Long): LiveData<Array<SongEntity>>

    @Query("SELECT COUNT(*) FROM SongEntity WHERE SongEntity.album_id = :albumId")
    fun countAllByAlbumId(albumId: Long): Long

    @Insert(onConflict = REPLACE)
    fun insertAll(songs: List<SongEntity>)
}