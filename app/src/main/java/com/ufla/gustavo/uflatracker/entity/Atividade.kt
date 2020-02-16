package com.ufla.gustavo.uflatracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "atividade",
    indices = [Index("id")]
)
data class Atividade(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? =null,

    @ColumnInfo(name = "data_criacao")
    var dataCriacao: Calendar? =null,

    @ColumnInfo(name = "nome")
    var nome: String,

    @ColumnInfo(name = "valor_maximo")
    var valor_maximo: Double,

    @ColumnInfo(name = "valor_minimo")
    var valor_minimo: Double,

    @ColumnInfo(name = "valor_medio")
    var valor_medio: Double
): Serializable