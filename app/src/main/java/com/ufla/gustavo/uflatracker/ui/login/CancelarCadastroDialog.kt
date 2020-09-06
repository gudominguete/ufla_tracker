package com.ufla.gustavo.uflatracker.ui.login

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.ufla.gustavo.uflatracker.R
import kotlinx.android.synthetic.main.dialog_confirmar_sair_formulario_cadastro.*

class CancelarCadastroDialog (context: Context, funcaoOk: (()->Unit)): Dialog(context){
    private var funcaoOk: (()->Unit)

    init {
        setCancelable(true)
        this.funcaoOk = funcaoOk
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_confirmar_sair_formulario_cadastro)
        prepararOnClickListeners()
    }

    private fun prepararOnClickListeners() {
        btn_cancelar_dialog.setOnClickListener {
            dismiss()
        }
        btn_confirmar_cancelamento.setOnClickListener {
            funcaoOk()
            dismiss()
        }
    }
}