package com.forasoft.albums.model.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.forasoft.albums.model.storage.entity.AlbumEntity
import com.forasoft.albums.model.storage.entity.SearchRequestEntity
import java.util.*
import javax.inject.Singleton

/**
 * Handles SQLite db (creation, migration, etc.)
 */
@Database(
    entities = [AlbumEntity::class, SearchRequestEntity::class],
    version = 2,
    exportSchema = false
)
@Singleton
@TypeConverters(Converter::class)
abstract class TunesDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun searchRequestDao(): SearchRequestDao
}

class Converter {
    @TypeConverter
    fun dateToTimeStamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun timeStampToDate(timeStamp: Long): Date {
        return Date(timeStamp)
    }
}