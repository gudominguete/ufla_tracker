package com.flex.flextracker.di.modulos

import com.flex.flextracker.ui.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ModulosUI = module {
    viewModel { LoginViewModel() }
}