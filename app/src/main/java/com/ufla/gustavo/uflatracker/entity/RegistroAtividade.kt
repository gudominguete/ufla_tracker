package com.ufla.gustavo.uflatracker.entity

import androidx.room.*

@Entity(
    tableName = "registro_atividade",
    indices = [Index("id")],
    foreignKeys = arrayOf(ForeignKey(entity = Atividade::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("atividadeId"),
    onDelete = ForeignKey.CASCADE)))
data class RegistroAtividade(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? =null,
    @ColumnInfo(name = "valor_batimento")
    var valorBatimento: Int,

    val atividadeId: Long
)