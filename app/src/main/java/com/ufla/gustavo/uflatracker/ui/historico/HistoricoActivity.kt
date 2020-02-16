package com.ufla.gustavo.uflatracker.ui.historico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.TrackerApplication
import kotlinx.android.synthetic.main.activity_historico.*

class HistoricoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)
        configurarRecyclerView()
    }

    fun configurarRecyclerView(){

        var atividades = TrackerApplication.database?.atividadeDao()?.getAtividades()
        var historicoAdapter= HistoricoAdapter(this, atividades!!)
        lista_historico.adapter = historicoAdapter
        lista_historico.layoutManager = LinearLayoutManager(this)
    }
}
