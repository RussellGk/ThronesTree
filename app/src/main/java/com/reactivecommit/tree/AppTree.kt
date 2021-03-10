package com.reactivecommit.tree

import android.app.Application
import com.reactivecommit.tree.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppTree : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(this@AppTree)
            modules(dataModule)
        }
        NetworkMonitor(this).startNetworkCallback()
    }

    override fun onTerminate() {
        super.onTerminate()
        NetworkMonitor(this).stopNetworkCallback()
    }

    companion object {
        lateinit var instance: AppTree
    }
}