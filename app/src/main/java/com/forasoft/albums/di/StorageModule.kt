package com.forasoft.albums.di

import android.content.Context
import androidx.room.Room
import com.forasoft.albums.model.storage.TunesDatabase
import dagger.Module
import dagger.Provides

@Module
class StorageModule {
    @Provides
    fun getSearchDao(tunesDb: TunesDatabase) = tunesDb.searchRequestDao()

    @Provides
    fun getAlbumDao(tunesDb: TunesDatabase) = tunesDb.albumDao()

    @Provides
    fun getSongDao(tunesDb: TunesDatabase) = tunesDb.songDao()

    @Provides
    fun createDb(context: Context): TunesDatabase = Room.databaseBuilder(
        context.applicationContext,
        TunesDatabase::class.java,
        "tunes"
    ).fallbackToDestructiveMigration().build()
}