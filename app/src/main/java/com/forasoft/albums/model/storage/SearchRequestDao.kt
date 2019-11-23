package com.forasoft.albums.model.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.forasoft.albums.model.storage.entity.SearchRequestEntity
import javax.inject.Singleton

/**
 * Dao to insert search requests to persistent storage
 */
@Dao
@Singleton
interface SearchRequestDao {
    @Query("SELECT COUNT(*) FROM SearchRequestEntity WHERE term = :term AND date >= :date")
    fun countAllSince(term: String, date: Long): Long

    @Insert
    fun insert(searchRequest: SearchRequestEntity): Long
}