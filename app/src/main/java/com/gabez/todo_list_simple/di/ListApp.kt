package com.gabez.todo_list_simple.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin

class ListApp: Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }


    private fun initKoin() {
        startKoin {
            androidContext(this@ListApp)
            modules(appModule)
        }
    }
}