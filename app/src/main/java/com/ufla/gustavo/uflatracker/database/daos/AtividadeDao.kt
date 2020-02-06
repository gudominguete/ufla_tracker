package com.ufla.gustavo.uflatracker.database.daos

import androidx.room.*
import com.ufla.gustavo.uflatracker.entity.Atividade
import com.ufla.gustavo.uflatracker.entity.RegistroAtividade

@Dao
interface AtividadeDao {

    @Query("SELECT * FROM atividade")
    fun getAtividades(): List<Atividade>

    @Query("SELECT * FROM atividade WHERE id = :id")
    fun getAtividadesById(id:Int) : Atividade
    @Delete
    fun deleteAtividade(atividade: Atividade)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAtividades(atividade: Atividade): Long
}