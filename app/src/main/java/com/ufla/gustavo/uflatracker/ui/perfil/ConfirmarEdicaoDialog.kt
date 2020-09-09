package com.ufla.gustavo.uflatracker.ui.perfil

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.ufla.gustavo.uflatracker.R
import kotlinx.android.synthetic.main.dialog_confirmacao_editar.*

class ConfirmarEdicaoDialog (context: Context, funcaoOk: (()->Unit)): Dialog(context){
    private var funcaoOk: (()->Unit)

    init {
        this.funcaoOk = funcaoOk
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_confirmacao_editar)
        prepararOnClickListeners()
    }

    private fun prepararOnClickListeners() {
        btn_confirmar_confirmacao.setOnClickListener {
            funcaoOk()
            dismiss()
        }
    }
}