package com.ufla.gustavo.uflatracker.di.modulos

import com.ufla.gustavo.uflatracker.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ModulosUI = module {
    viewModel {MainViewModel()}
}