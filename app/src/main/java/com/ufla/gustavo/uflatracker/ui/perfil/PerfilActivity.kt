package com.ufla.gustavo.uflatracker.ui.perfil

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_atividade.*
import kotlinx.android.synthetic.main.activity_perfil.*

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        iniciarDados()
        prepararClickListeners()
    }

    private fun prepararClickListeners() {
        back_button.setOnClickListener {
            finish()
        }
    }

    private fun iniciarDados() {
        val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val nome = sharedPref.getString(Constantes.NOME, "")
        val idade = sharedPref.getString(Constantes.IDADE, "")
        val altura = sharedPref.getString(Constantes.ALTURA, "")
        val peso = sharedPref.getString(Constantes.PESO, "")
        nome_perfil.text = nome
        valor_peso.text = "${peso}kg"
        valor_altura.text = "${altura}m"
        valor_nome.text = nome
        valor_idade.text = "${idade} anos"
    }
}
