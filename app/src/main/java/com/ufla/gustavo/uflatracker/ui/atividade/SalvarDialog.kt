package com.ufla.gustavo.uflatracker.ui.atividade

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.ufla.gustavo.uflatracker.R
import kotlinx.android.synthetic.main.dialog_salvar.*

class SalvarDialog (context: Context, funcaoOk: ((String)->Unit), desconectado: Boolean): Dialog(context){
    private var funcaoOk: ((String)->Unit)
    private var desconectado: Boolean = false
    init {
        setCancelable(true)
        this.funcaoOk = funcaoOk
        this.desconectado = desconectado
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_salvar)
        prepararOnClickListeners()
        if(desconectado){
            text_cancelar_atividade.visibility = View.GONE
        } else {
            text_cancelar_atividade.visibility = View.VISIBLE
        }
        this.setCancelable(false)
    }

    private fun prepararOnClickListeners() {
        text_cancelar_atividade.setOnClickListener {
            dismiss()
        }
        btn_enviar_atividade.setOnClickListener {
            if(validarFormulario()){
                funcaoOk(edit_valor_nome_atividade.text.toString())
            }
        }
    }

    private fun validarFormulario() : Boolean{
        var valido = true
        resetarErros()
        if(edit_valor_nome_atividade.text.toString().isNullOrEmpty()){
            texto_erro_nome_atividade.visibility = View.VISIBLE
            valido = false
        }
        return valido
    }

    private fun resetarErros() {
        texto_erro_nome_atividade.visibility = View.GONE
    }
}