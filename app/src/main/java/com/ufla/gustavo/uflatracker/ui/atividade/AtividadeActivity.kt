package com.ufla.gustavo.uflatracker.ui.atividade

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_atividade.*
import kotlinx.android.synthetic.main.activity_historico.*

class AtividadeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividade)
        prepararClickListeners()
        iniciarHeader()

    }

    private fun iniciarHeader() {
        val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val nome = sharedPref.getString(Constantes.NOME, "")
        texto_nome_atividade.text = nome
    }

    private fun prepararClickListeners() {
        botao_voltar_atividade.setOnClickListener {
            finish()
        }

        botao_iniciar_atividade.setOnClickListener {
            iniciarAtividade()
        }
    }

    private fun iniciarAtividade() {
        botao_iniciar_atividade.visibility = View.GONE
        botao_pausar_atividade.visibility = View.VISIBLE
        botao_parar_atividade.visibility = View.VISIBLE
    }
}
