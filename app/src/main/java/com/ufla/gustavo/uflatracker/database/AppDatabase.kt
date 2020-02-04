package com.ufla.gustavo.uflatracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ufla.gustavo.uflatracker.database.daos.RegistroAtividadeDao
import com.ufla.gustavo.uflatracker.entity.RegistroAtividade

private const val DATABASE = "notes"

@Database(
    entities = [RegistroAtividade::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun registroAtividadeDao(): RegistroAtividadeDao


    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE)
                .build()
        }
    }
}