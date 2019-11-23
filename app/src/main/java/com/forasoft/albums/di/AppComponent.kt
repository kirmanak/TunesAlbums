package com.forasoft.albums.di

import android.content.Context
import com.forasoft.albums.view.AlbumsListFragment
import com.forasoft.albums.view.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(listFragment: AlbumsListFragment)
}