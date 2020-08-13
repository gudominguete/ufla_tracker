package com.ufla.gustavo.uflatracker.ui.visualizacao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.TrackerApplication
import com.ufla.gustavo.uflatracker.entity.Atividade
import com.ufla.gustavo.uflatracker.entity.RegistroAtividade
import kotlinx.android.synthetic.main.activity_visualizacao.*

class VisualizacaoActivity : AppCompatActivity() {

    private var lista = mutableListOf<DataPoint>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizacao)
        var myIntent = getIntent();
        var atividade = myIntent.getSerializableExtra("atividade") as Atividade
        preencherLista(atividade)
        preencherCampos(atividade)
        prepararClickListener()
    }

    private fun preencherCampos(atividade: Atividade) {
        valor_tempo.text = atividade.tempoAtividade
        valor_batimentos_cardiacos.text = atividade.valor_medio.toString()
        valor_batimentos_cardiacos_minimo.text = atividade.valor_minimo.toString()
        valor_batimentos_cardiacos_maximo.text = atividade.valor_maximo.toString()
    }

    private fun prepararClickListener() {
        botao_voltar_visualizacao.setOnClickListener {
            finish()
        }
    }

    private fun preencherLista(atividade:Atividade){
        var registros = TrackerApplication.database?.registroAtividadeDao()?.getRegistrosAtividadesByIdAtividade(atividade.id!!)
        prepararLista(registros!!)
    }
    private fun prepararLista(registros: List<RegistroAtividade>) {
        var item = 0
        registros.forEach {

            lista.add(DataPoint( item.toDouble(),it.valorBatimento.toDouble()))
            item++
        }

        val graph = findViewById(R.id.visualizacao_graph) as GraphView
        val series = LineGraphSeries(lista.toTypedArray())
        graph.addSeries(series)
    }

}
