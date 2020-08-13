package com.ufla.gustavo.uflatracker.ui.historico

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.TrackerApplication
import com.ufla.gustavo.uflatracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_historico.*

class HistoricoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)
        configurarRecyclerView()
        configurarClickListeners()
    }

    fun configurarRecyclerView(){
        val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val cpf = sharedPref.getString(Constantes.CPF, "")
        var atividades = TrackerApplication.database?.atividadeDao()?.getAtividadesByCpf(cpf)
        if(atividades != null && atividades.size > 0){

            var historicoAdapter= HistoricoAdapter(this, atividades!!)
            lista_historico.adapter = historicoAdapter
            lista_historico.layoutManager = LinearLayoutManager(this)
            label_ultimas_atividades.visibility = View.VISIBLE
            lista_historico.visibility = View.VISIBLE
            label_sem_atividades.visibility = View.GONE
        } else{
            label_ultimas_atividades.visibility = View.GONE
            lista_historico.visibility = View.GONE
            label_sem_atividades.visibility = View.VISIBLE
        }
    }

    private fun configurarClickListeners() {

        back_button.setOnClickListener {
            finish()
        }
    }
}
