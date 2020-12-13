package com.flex.flextracker.database.daos

import androidx.room.*
import com.flex.flextracker.entity.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario WHERE cpf = :cpf")
    fun getUsuariosByCpf(cpf:String) : Usuario


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAtividades(usuario: Usuario): Long
}