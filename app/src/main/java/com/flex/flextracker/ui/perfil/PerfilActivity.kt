package com.flex.flextracker.ui.perfil

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.flex.flextracker.R
import com.flex.flextracker.TrackerApplication
import com.flex.flextracker.entity.Usuario
import com.flex.flextracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_perfil.*
import kotlinx.android.synthetic.main.activity_perfil.back_button
import kotlinx.android.synthetic.main.activity_perfil.botao_editar
import java.text.SimpleDateFormat

class PerfilActivity : AppCompatActivity() {

    private var usuario: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        iniciarDados()
        prepararClickListeners()
    }

    override fun onRestart() {
        super.onRestart()
        iniciarDados()
    }

    private fun prepararClickListeners() {
        back_button.setOnClickListener {
            finish()
        }

        botao_editar.setOnClickListener {

            var intent = Intent(this, EditarPerfilActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarDados() {

        val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val cpf = sharedPref.getString(Constantes.CPF, "")
        usuario = TrackerApplication.database?.usuarioDao()?.getUsuariosByCpf(cpf)!!
        if(usuario != null){

            nome_perfil.setText(usuario?.nome)
            valor_altura.setText(usuario?.altura.toString())
            valor_peso.setText(usuario?.peso.toString().replace(".", ","))
            val format = SimpleDateFormat("dd/MM/yyy")
            valor_idade.setText(format.format(usuario?.dataNasc?.time))
        }

    }
}
