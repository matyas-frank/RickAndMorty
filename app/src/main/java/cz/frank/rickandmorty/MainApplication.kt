package cz.frank.rickandmorty

import android.app.Application
import cz.frank.rickandmorty.di.rootModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(rootModule)
            androidContext(this@MainApplication)
        }
    }
}
