package com.ufla.gustavo.uflatracker.entity

import androidx.room.*
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "atividade",
    indices = [Index("id")],
    foreignKeys = arrayOf(
        ForeignKey(entity = Usuario::class,
        parentColumns = arrayOf("cpf"),
        childColumns = arrayOf("cpfUsuario"),
        onDelete = ForeignKey.CASCADE)
    )
)
data class Atividade(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? =null,

    @ColumnInfo(name = "data_criacao")
    var dataCriacao: Calendar? =null,

    @ColumnInfo(name="tempo_atividade")
    var tempoAtividade: String,

    @ColumnInfo(name = "nome")
    var nome: String,

    @ColumnInfo(name = "valor_maximo")
    var valor_maximo: Double,

    @ColumnInfo(name = "valor_minimo")
    var valor_minimo: Double,

    @ColumnInfo(name = "valor_medio")
    var valor_medio: Double,

    val cpfUsuario: String
): Serializable