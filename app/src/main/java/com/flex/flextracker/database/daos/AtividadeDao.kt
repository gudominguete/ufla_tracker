package com.flex.flextracker.database.daos

import androidx.room.*
import com.flex.flextracker.entity.Atividade

@Dao
interface AtividadeDao {

    @Query("SELECT * FROM atividade")
    fun getAtividades(): List<Atividade>

    @Query("SELECT * FROM atividade WHERE id = :id")
    fun getAtividadesById(id:Int) : Atividade
    @Delete
    fun deleteAtividade(atividade: Atividade)

    @Query("SELECT * FROM atividade WHERE cpfUsuario = :cpf")
    fun getAtividadesByCpf(cpf:String) : List<Atividade>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAtividades(atividade: Atividade): Long
}