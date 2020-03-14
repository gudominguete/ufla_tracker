package com.ufla.gustavo.uflatracker.ui.historico

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.TrackerApplication
import com.ufla.gustavo.uflatracker.entity.Atividade
import com.ufla.gustavo.uflatracker.ui.atividade.AtividadeActivity
import com.ufla.gustavo.uflatracker.ui.perfil.PerfilActivity
import com.ufla.gustavo.uflatracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_historico.*
import kotlinx.android.synthetic.main.activity_historico.botao_historico
import kotlinx.android.synthetic.main.activity_historico.botao_perfil
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HistoricoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)
        configurarRecyclerView()
        configurarClickListeners()
        iniciarHeader()
    }

    private fun iniciarHeader() {
        val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val nome = sharedPref.getString(Constantes.NOME, "")
        texto_nome_historico.text = nome
    }


    fun configurarRecyclerView(){

        var atividades = TrackerApplication.database?.atividadeDao()?.getAtividades()
        var historicoAdapter= HistoricoAdapter(this, atividades!!)
        lista_historico.adapter = historicoAdapter
        lista_historico.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarClickListeners() {
        botao_perfil.setOnClickListener {
            var intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        botao_iniciar.setOnClickListener {
            var intent = Intent(this, AtividadeActivity::class.java)
            startActivity(intent)
        }
    }
}
