package com.ufla.gustavo.uflatracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ufla.gustavo.uflatracker.R
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint


class GraphicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphic)
        prepararDadosGrafico()
    }

    private fun prepararDadosGrafico() {
        val graph = findViewById(R.id.graph) as GraphView
        val series = LineGraphSeries(
            arrayOf(
                DataPoint(0.toDouble(), 1.toDouble()),
                DataPoint(1.toDouble(), 5.toDouble()),
                DataPoint(2.toDouble(), 3.toDouble()),
                DataPoint(3.toDouble(), 2.toDouble()),
                DataPoint(4.toDouble(), 6.toDouble())
            )
        )
        graph.addSeries(series)
    }
}
