package com.flex.flextracker.di

import com.flex.flextracker.di.modulos.ModulosUI
import org.koin.core.module.Module

object ModulosApp{
    fun obterModulos(): List<Module> = listOf(
        ModulosUI
    )
}