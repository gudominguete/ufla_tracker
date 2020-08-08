package com.ufla.gustavo.uflatracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate
import java.util.*


@Entity(
    tableName = "usuario",
    indices = [Index("cpf")]
)
data class Usuario  (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "cpf")
    var cpf: String,

    @ColumnInfo(name = "nome")
    var nome: String?,

    @ColumnInfo(name = "altura")
    var altura: Int?,

    @ColumnInfo(name = "dataNasc")
    var dataNasc: Calendar? =null,

    @ColumnInfo(name = "peso")
    var peso: Double?
): Serializable