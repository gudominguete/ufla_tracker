package com.ufla.gustavo.uflatracker

import android.app.Application
import androidx.room.Room
import com.facebook.stetho.Stetho
import com.ufla.gustavo.uflatracker.database.AppDatabase

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
}