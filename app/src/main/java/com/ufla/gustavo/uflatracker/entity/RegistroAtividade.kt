package com.ufla.gustavo.uflatracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "registro_atividade",
    indices = [Index("id")]
)
data class RegistroAtividade(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "valor_batimento")
    var valorBatimento: Int,

    @ColumnInfo(name = "id_atividade")
    var idAtividade: Int
)