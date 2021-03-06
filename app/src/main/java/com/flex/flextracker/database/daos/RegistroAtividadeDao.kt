package com.flex.flextracker.database.daos

import androidx.room.*
import com.flex.flextracker.entity.RegistroAtividade

@Dao
interface RegistroAtividadeDao {

    @Query("SELECT * FROM registro_atividade")
    fun getRegistrosAtividades(): List<RegistroAtividade>

    @Query("SELECT * FROM registro_atividade WHERE id = :id")
    fun getRegistrosAtividadesById(id:Int) :RegistroAtividade

    @Query("SELECT * FROM registro_atividade WHERE atividadeId = :idAtividade")
    fun getRegistrosAtividadesByIdAtividade(idAtividade:Long) : List<RegistroAtividade>

    @Delete
    fun deleteRegistroAtividade(registroAtividade: RegistroAtividade)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateRegistrosAtividades(registroAtividade: RegistroAtividade): Long
}