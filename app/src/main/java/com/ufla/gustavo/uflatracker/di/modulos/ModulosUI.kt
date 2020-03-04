package com.ufla.gustavo.uflatracker.di.modulos

import com.ufla.gustavo.uflatracker.ui.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ModulosUI = module {
    viewModel { LoginViewModel() }
}