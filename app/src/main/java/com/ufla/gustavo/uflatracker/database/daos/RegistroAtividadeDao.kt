package com.ufla.gustavo.uflatracker.database.daos

import androidx.room.*
import com.ufla.gustavo.uflatracker.entity.RegistroAtividade

@Dao
interface RegistroAtividadeDao {

    @Query("SELECT * FROM registro_atividade")
    fun getRegistrosAtividades(): List<RegistroAtividade>

    @Query("SELECT * FROM registro_atividade WHERE id = :id")
    fun getRegistrosAtividadesById(id:Int) :RegistroAtividade
    @Delete
    fun deleteRegistroAtividade(registroAtividade: RegistroAtividade)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateRegistrosAtividades(registroAtividade: RegistroAtividade): Long
}