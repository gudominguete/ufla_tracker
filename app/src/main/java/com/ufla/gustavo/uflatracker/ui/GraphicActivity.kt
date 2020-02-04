package com.ufla.gustavo.uflatracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.ufla.gustavo.uflatracker.R
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import kotlinx.android.synthetic.main.activity_graphic.*
import kotlin.random.Random


class GraphicActivity : AppCompatActivity() {
    private var handler = Handler()
    private var contador = 51
    private var lista = mutableListOf<DataPoint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphic)
        prepararLista()
        prepararDadosGrafico()
        handler.postDelayed(myRunnable, 100)
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
            lista.add(DataPoint(contador.toDouble(), nextValues.toDouble()))
            Log.i("teste", lista.size.toString())
            graph.removeAllSeries()
            graph.addSeries(LineGraphSeries(lista.toTypedArray()))
            contador++
            handler.postDelayed(this, 100)
        }
    }
}
