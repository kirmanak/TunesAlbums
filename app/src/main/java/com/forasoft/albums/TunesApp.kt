package com.forasoft.albums

import android.app.Application
import com.forasoft.albums.di.DaggerAppComponent

open class TunesApp : Application() {
    val appComponent by lazy { DaggerAppComponent.factory().create(applicationContext) }
}