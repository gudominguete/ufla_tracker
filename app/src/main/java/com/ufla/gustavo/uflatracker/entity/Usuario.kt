package com.ufla.gustavo.uflatracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(
    tableName = "usuario",
    indices = [Index("cpf")]
)
data class Usuario(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "cpf")
    var cpf: String,

    @ColumnInfo(name = "nome")
    var nome: String?,

    @ColumnInfo(name = "altura")
    var altura: String?,

    @ColumnInfo(name = "idade")
    var idade: Int?,

    @ColumnInfo(name = "peso")
    var peso: Float?
): Serializable