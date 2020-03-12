package com.ufla.gustavo.uflatracker.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.ui.atividade.AtividadeActivity
import com.ufla.gustavo.uflatracker.ui.historico.HistoricoActivity
import com.ufla.gustavo.uflatracker.ui.perfil.PerfilActivity
import com.ufla.gustavo.uflatracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        configurarClickListeners()
        iniciarHeader()
    }

    private fun iniciarHeader() {
        val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val nome = sharedPref.getString(Constantes.NOME, "")
        texto_nome.text = nome
    }

    private fun configurarClickListeners() {
        botao_perfil.setOnClickListener {

            var intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }
        botao_historico.setOnClickListener {

            var intent = Intent(this, HistoricoActivity::class.java)
            startActivity(intent)
        }
        botao_iniciar_home.setOnClickListener {
            var intent = Intent(this, AtividadeActivity::class.java)
            startActivity(intent)
        }
    }
}
