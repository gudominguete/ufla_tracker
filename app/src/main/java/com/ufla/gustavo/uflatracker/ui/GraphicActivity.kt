package com.ufla.gustavo.uflatracker.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import com.ufla.gustavo.uflatracker.R
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.ufla.gustavo.uflatracker.TrackerApplication
import com.ufla.gustavo.uflatracker.entity.Atividade
import com.ufla.gustavo.uflatracker.entity.RegistroAtividade
import kotlinx.android.synthetic.main.activity_graphic.*
import java.time.OffsetDateTime
import java.util.*
import kotlin.random.Random


class GraphicActivity : AppCompatActivity() {
    private var handler = Handler()
    private var contador = 51
    private var lista = mutableListOf<DataPoint>()
    private var listaRegistroAtividade = mutableListOf<Int>()
    private var valor_minimo = 12000
    private var valor_maximo = 0
    private var valor_medio = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphic)
        prepararLista()
        prepararDadosGrafico()
        prepararClickListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepararClickListeners() {
        btn_start.setOnClickListener {
            cmTimer.start()
            handler.postDelayed(myRunnable, 100)
        }

        btn_stop.setOnClickListener {
            cmTimer.stop()
            handler.removeCallbacks(myRunnable);
        }
        btn_finish.setOnClickListener {
            cmTimer.stop()
            handler.removeCallbacks(myRunnable);
            valor_medio = valor_medio/ listaRegistroAtividade.size
            var atividade = Atividade(null, Calendar.getInstance() , "Atividade " + Date(), valor_maximo.toDouble(), valor_minimo.toDouble(), valor_medio.toDouble())
            atividade.id = TrackerApplication.database?.atividadeDao()?.insertOrUpdateAtividades(atividade)
            listaRegistroAtividade.forEach {

                TrackerApplication.database?.registroAtividadeDao()?.insertOrUpdateRegistrosAtividades(
                    RegistroAtividade(null, it,atividade.id!!)
                )
            }
        }
    }

    private fun prepararLista() {
        for (i in 1..50){

            lista.add(DataPoint(i.toDouble(), 0.toDouble()))
        }
    }

    private fun prepararDadosGrafico() {
        val graph = findViewById(R.id.graph) as GraphView
        val series = LineGraphSeries(lista.toTypedArray())
        graph.addSeries(series)
    }



    var myRunnable: Runnable = object : Runnable {
        override fun run() {
            val nextValues = Random.nextInt(50, 200)
            lista.removeAt(0)
            if(nextValues < valor_minimo){
                valor_minimo = nextValues
            }
            if(nextValues > valor_maximo){
                valor_maximo = nextValues
            }
            valor_medio += nextValues

            lista.add(DataPoint(contador.toDouble(), nextValues.toDouble()))
            graph.removeAllSeries()
            graph.addSeries(LineGraphSeries(lista.toTypedArray()))
            contador++
            handler.postDelayed(this, 1000)
            listaRegistroAtividade.add(nextValues)
        }
    }
}
