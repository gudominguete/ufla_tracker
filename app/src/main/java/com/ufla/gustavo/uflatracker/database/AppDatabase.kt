package com.ufla.gustavo.uflatracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ufla.gustavo.uflatracker.database.converter.TimestampConverter
import com.ufla.gustavo.uflatracker.database.daos.AtividadeDao
import com.ufla.gustavo.uflatracker.database.daos.RegistroAtividadeDao
import com.ufla.gustavo.uflatracker.entity.Atividade
import com.ufla.gustavo.uflatracker.entity.RegistroAtividade

private const val DATABASE = "notes"

@Database(
    entities = [RegistroAtividade::class, Atividade::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TimestampConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun registroAtividadeDao(): RegistroAtividadeDao
    abstract fun atividadeDao(): AtividadeDao


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