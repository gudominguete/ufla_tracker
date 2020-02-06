package com.ufla.gustavo.uflatracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
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
    var nome: String
)