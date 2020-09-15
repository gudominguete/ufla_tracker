package com.ufla.gustavo.uflatracker.ui.visualizacao

import android.graphics.Color
import android.graphics.DashPathEffect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.TrackerApplication
import com.ufla.gustavo.uflatracker.entity.Atividade
import com.ufla.gustavo.uflatracker.entity.RegistroAtividade
import com.ufla.gustavo.uflatracker.ui.atividade.XAxisFormatter
import com.ufla.gustavo.uflatracker.ui.atividade.YAxisFormatter
import kotlinx.android.synthetic.main.activity_atividade.*
import kotlinx.android.synthetic.main.activity_visualizacao.*
import kotlinx.android.synthetic.main.activity_visualizacao.valor_batimentos_cardiacos
import kotlinx.android.synthetic.main.activity_visualizacao.valor_tempo
import java.text.SimpleDateFormat
import java.util.ArrayList

class VisualizacaoActivity : AppCompatActivity() {

    private var lista = mutableListOf<Entry>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizacao)
        var myIntent = getIntent();
        var atividade = myIntent.getSerializableExtra("atividade") as Atividade
        configurarChart()
        preencherLista(atividade)
        preencherCampos(atividade)
        prepararClickListener()


    }

    private fun preencherCampos(atividade: Atividade) {
        valor_tempo.text = atividade.tempoAtividade
        valor_batimentos_cardiacos.text = atividade.valor_medio.toString()
        valor_batimentos_cardiacos_minimo.text = atividade.valor_minimo.toString()
        valor_batimentos_cardiacos_maximo.text = atividade.valor_maximo.toString()
        nome_atividade_visualizacao.text = atividade.nome
        val pattern = "dd/MM/yyyy HH:mm"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = simpleDateFormat.format(atividade.dataCriacao?.time)
        valor_data.text = date
    }

    private fun prepararClickListener() {
        botao_voltar_visualizacao.setOnClickListener {
            finish()
        }
    }

    private fun preencherLista(atividade:Atividade){
        var registros = TrackerApplication.database?.registroAtividadeDao()?.getRegistrosAtividadesByIdAtividade(atividade.id!!)
        setData(registros)
//        prepararLista(registros!!)
    }
//    private fun prepararLista(registros: List<RegistroAtividade>) {
//        var item = 0
//        registros.forEach {
//
//            lista.add(DataPoint( item.toDouble(),it.valorBatimento.toDouble()))
//            item++
//        }
//
//        val graph = findViewById(R.id.visualizacao_graph) as GraphView
//        val series = LineGraphSeries(lista.toTypedArray())
//        graph.addSeries(series)
//    }

    fun setData(registros: List<RegistroAtividade>?) {
        val values = ArrayList<Entry>()
        var  set1: LineDataSet?
        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0
        ) {
            set1 = chart.getData().getDataSetByIndex(0) as LineDataSet
            //        for (i in 0 until count) {
//            val f = (Math.random() * range).toFloat() - 30
//            values.add(Entry(i.toFloat(), f))
//        }
            var item = 0
            registros?.forEach {

                lista.add(Entry( it.valorBatimento.toFloat(), item.toFloat() ))
                item++
            }
            set1.setValues(lista)
            set1.notifyDataSetChanged()
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "Batimentos por minuto")
            set1.setDrawIcons(false)

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set1.color = Color.BLACK
            set1.setCircleColor(Color.BLACK)

            // line thickness and point size
            set1.lineWidth = 1f
            set1.circleRadius = 3f

            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.getAxisLeft().getAxisMinimum() }

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this, R.drawable.bg_boas_vindas)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }
            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart.setData(data)
        }
    }
    private fun configurarChart(){

        chart.setBackgroundColor(Color.WHITE)
        chart.getDescription().setEnabled(false)
        chart.setTouchEnabled(false)
        var yAxis = chart.axisLeft;
        yAxis.axisMaximum = 250f
        yAxis.axisMinimum = 0f
        yAxis.valueFormatter = YAxisFormatter()
        var xAxis = chart.xAxis
        chart.axisRight.isEnabled = false
    }


}
