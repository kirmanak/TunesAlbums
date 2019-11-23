package com.forasoft.albums.model.storage

import androidx.room.Dao
import androidx.room.Insert
import com.forasoft.albums.model.storage.entity.SearchRequestEntity
import javax.inject.Singleton

/**
 * Dao to insert search requests to persistent storage
 */
@Dao
@Singleton
interface SearchRequestDao {
    @Insert
    fun insert(searchRequest: SearchRequestEntity): Long
}