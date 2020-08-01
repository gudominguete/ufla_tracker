package com.ufla.gustavo.uflatracker.ui.login

import androidx.lifecycle.ViewModel
import com.ufla.gustavo.uflatracker.TrackerApplication
import com.ufla.gustavo.uflatracker.entity.Usuario

class LoginViewModel (): ViewModel(){

    fun loginCpf(cpf: String): Usuario?{
        return TrackerApplication.database?.usuarioDao()?.getUsuariosByCpf(cpf)

    }

}