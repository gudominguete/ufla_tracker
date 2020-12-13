package com.flex.flextracker

import android.app.Application
import androidx.room.Room
import com.facebook.stetho.Stetho
import com.flex.flextracker.database.AppDatabase
import com.flex.flextracker.di.ModulosApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class TrackerApplication : Application() {

    companion object {
        var database: AppDatabase? = null
    }


    override fun onCreate() {
        super.onCreate()
        //Room
        database = Room.databaseBuilder(this, AppDatabase::class.java, "my-db").allowMainThreadQueries().build()

        //Stetho
        val initializerBuilder = Stetho.newInitializerBuilder(this)
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
        val initializer = initializerBuilder.build()
        Stetho.initialize(initializer)
    }

    private fun setupKoin(){
        startKoin{
            androidContext(this@TrackerApplication)
            modules(ModulosApp.obterModulos())
        }
    }
}