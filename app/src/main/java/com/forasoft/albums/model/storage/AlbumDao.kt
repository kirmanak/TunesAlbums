package com.forasoft.albums.model.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.forasoft.albums.model.storage.entity.AlbumEntity
import javax.inject.Singleton

/**
 * Dao to query albums from DB and insert them there
 */
@Dao
@Singleton
interface AlbumDao {
    @Query("SELECT * FROM AlbumEntity WHERE AlbumEntity.search_id = (SELECT uid FROM SearchRequestEntity WHERE SearchRequestEntity.term = :term ORDER BY date DESC LIMIT 1)")
    fun getAllBySearchTerm(term: String): LiveData<Array<AlbumEntity>>

    @Insert
    fun insertAll(albumEntities: List<AlbumEntity>)
}