package com.ufla.gustavo.uflatracker.ui.login

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.entity.Usuario
import kotlinx.android.synthetic.main.dialog_perfil.*

class PerfilDialog (context: Context, funcaoOk: ((usuario: Usuario)->Unit)): Dialog(context){
    private var funcaoOk: ((usuario: Usuario)->Unit)
    init {
        setCancelable(true)
        this.funcaoOk = funcaoOk
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_perfil)
        prepararOnClickListeners()
    }

    private fun prepararOnClickListeners() {
        text_cancelar.setOnClickListener {
            dismiss()
        }
        btn_enviar.setOnClickListener {
            if(validarFormulario()){

//                funcaoOk(Usuario(edit_valor_nome.text.toString(),
//                    edit_valor_altura.text.toString(),
//                    edit_valor_idade.text.toString(),
//                    edit_valor_peso.text.toString()))
            }
        }
    }

    private fun validarFormulario() : Boolean{
        var valido = true
        resetarErros()
        if(edit_valor_nome.text.toString().isNullOrEmpty()){
            texto_erro_nome.visibility = View.VISIBLE
            valido = false
        }
        if(edit_valor_idade.text.toString().isNullOrEmpty()){
            texto_erro_idade.visibility = View.VISIBLE
            valido = false
        }
        if(edit_valor_peso.text.toString().isNullOrEmpty()){
            texto_erro_peso.visibility = View.VISIBLE
            valido = false
        }
        if(edit_valor_altura.text.toString().isNullOrEmpty()){
            //texto_erro_altura.visibility = View.VISIBLE
            valido = false
        }
        return valido
    }

    private fun resetarErros() {
        texto_erro_nome.visibility = View.GONE
        texto_erro_idade.visibility = View.GONE
        texto_erro_peso.visibility = View.GONE
        //texto_erro_altura.visibility = View.GONE
    }
}