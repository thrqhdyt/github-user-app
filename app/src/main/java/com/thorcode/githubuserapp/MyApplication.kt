package com.thorcode.githubuserapp

import android.app.Application
import com.thorcode.githubuserapp.di.useCaseModule
import com.thorcode.githubuserapp.di.viewModelModule
import dev.thorcode.core.di.dataStoreModule
import dev.thorcode.core.di.databaseModule
import dev.thorcode.core.di.networkModule
import dev.thorcode.core.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    dataStoreModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}