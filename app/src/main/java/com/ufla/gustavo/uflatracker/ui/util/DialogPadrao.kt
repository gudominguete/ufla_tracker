package com.ufla.gustavo.uflatracker.ui.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.ufla.gustavo.uflatracker.R
import kotlinx.android.synthetic.main.dialog_padrao.*

class DialogPadrao (context: Context, var mensagem: String,
                    var textoOk: String, var funcaoOk: (()->Unit),
                    var textoCancelar: String, var funcaoCancelar: (()->Unit), var mostrarCancelar: Boolean): Dialog(context){


    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_padrao)
        prepararOnClickListeners()
        configurarDialog()
    }

    private fun configurarDialog() {
        texto_dialog_padrao.text = mensagem
        btn_cancelar_dialog.text = textoCancelar
        btn_confirmar_padrao.text = textoOk
        if(mostrarCancelar) {
            btn_cancelar_dialog.visibility = View.VISIBLE
        } else {
            btn_cancelar_dialog.visibility = View.GONE
        }
    }

    private fun prepararOnClickListeners() {
        btn_confirmar_padrao.setOnClickListener {
            funcaoOk()
            dismiss()
        }
        btn_cancelar_dialog.setOnClickListener {
            funcaoCancelar()
            dismiss()
        }
    }
}