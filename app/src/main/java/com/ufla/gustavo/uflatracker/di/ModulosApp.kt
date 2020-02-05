package com.ufla.gustavo.uflatracker.di

import com.ufla.gustavo.uflatracker.di.modulos.ModulosUI
import org.koin.core.module.Module

object ModulosApp{
    fun obterModulos(): List<Module> = listOf(
        ModulosUI
    )
}