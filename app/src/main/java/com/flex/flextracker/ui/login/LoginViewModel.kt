package com.flex.flextracker.ui.login

import androidx.lifecycle.ViewModel
import com.flex.flextracker.TrackerApplication
import com.flex.flextracker.entity.Usuario

class LoginViewModel (): ViewModel(){

    fun loginCpf(cpf: String): Usuario?{
        return TrackerApplication.database?.usuarioDao()?.getUsuariosByCpf(cpf)

    }

}