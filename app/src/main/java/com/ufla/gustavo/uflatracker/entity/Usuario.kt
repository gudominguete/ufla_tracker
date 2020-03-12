package com.ufla.gustavo.uflatracker.entity

import java.io.Serializable

data class Usuario(
    var nome: String,
    var altura: String,
    var idade: String,
    var peso: String
): Serializable