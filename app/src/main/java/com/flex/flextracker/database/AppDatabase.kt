package com.flex.flextracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flex.flextracker.database.converter.TimestampConverter
import com.flex.flextracker.database.daos.AtividadeDao
import com.flex.flextracker.database.daos.RegistroAtividadeDao
import com.flex.flextracker.database.daos.UsuarioDao
import com.flex.flextracker.entity.Atividade
import com.flex.flextracker.entity.RegistroAtividade
import com.flex.flextracker.entity.Usuario

private const val DATABASE = "notes"

@Database(
    entities = [RegistroAtividade::class, Atividade::class, Usuario::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(TimestampConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun registroAtividadeDao(): RegistroAtividadeDao
    abstract fun atividadeDao(): AtividadeDao
    abstract fun usuarioDao(): UsuarioDao


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