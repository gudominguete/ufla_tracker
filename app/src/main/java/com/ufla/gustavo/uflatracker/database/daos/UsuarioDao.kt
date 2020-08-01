package com.ufla.gustavo.uflatracker.database.daos

import androidx.room.*
import com.ufla.gustavo.uflatracker.entity.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario WHERE cpf = :cpf")
    fun getUsuariosByCpf(cpf:String) : Usuario
}