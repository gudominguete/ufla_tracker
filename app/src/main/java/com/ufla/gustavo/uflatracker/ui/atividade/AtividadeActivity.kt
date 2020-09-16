package com.ufla.gustavo.uflatracker.ui.atividade

import android.bluetooth.BluetoothDevice
import android.content.*
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.Utils
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.TrackerApplication
import com.ufla.gustavo.uflatracker.entity.Atividade
import com.ufla.gustavo.uflatracker.entity.RegistroAtividade
import com.ufla.gustavo.uflatracker.service.ConectarBluetoothService
import com.ufla.gustavo.uflatracker.ui.util.DialogPadrao
import com.ufla.gustavo.uflatracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_atividade.*
import java.util.*


class AtividadeActivity : AppCompatActivity() {

    private var conectarBluetoothService: ConectarBluetoothService? = null
    private var status = false
    private var iniciado = false
    private var inicadoGeral = false
//    private var lista = mutableListOf<DataPoint>()

    private lateinit var  set1: LineDataSet
    private var lista = mutableListOf<Entry>()
    private var listaRegistroAtividade = mutableListOf<Int>()
    private var valor_minimo = 12000
    private var valor_maximo = 0
    private var valor_medio = 0
    private var contador = 1
    private var mLastStopTime: Long = 0

    var bluetoothConectado: BluetoothDevice? =null

    private var handler = Handler()

    private val sc = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ConectarBluetoothService.ConectarBluetoothBinder
            conectarBluetoothService = binder.service
            status = true
            aparelho_conectado.visibility = View.VISIBLE
            mensagem_nao_conectado.visibility = View.GONE
            valor_nome_bluetooth.text = binder.service.bluetoothDevice?.name
        }
        override fun onServiceDisconnected(name: ComponentName) {

            aparelho_conectado.visibility = View.GONE
            mensagem_nao_conectado.visibility = View.VISIBLE
            conectarBluetoothService = null
            status = false
            exibirDesconexao()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividade)
        prepararClickListeners()
        prepararLista()
        prepararService()
        prepararDadosGrafico()
        configurarChart()
        setData(10,200F)
    }


    private fun exibirDesconexao(){
        lateinit var dialog:AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("O equipamento foi desconectado")
        builder.setMessage("O equipamento foi desconectado, a atividade será finalizada!")
        val dialogClickListener = DialogInterface.OnClickListener{_,which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> pararAtividade(true)
            }
        }
        builder.setPositiveButton("Ok",dialogClickListener)
        dialog = builder.create()
        dialog.show()
    }

    private fun prepararLista() {
//        for (i in 1..50){
//            lista.add(DataPoint(i.toDouble(), 0.toDouble()))
//        }
    }

    private fun reconectar(){
        if(conectarBluetoothService != null){
            conectarBluetoothService!!.load()
            conectarBluetoothService?.connect(conectarBluetoothService!!.bluetoothDevice!!)
        } else {
            Toast.makeText(this@AtividadeActivity, "Negative/No button clicked.", Toast.LENGTH_LONG).show()
        }
    }

    private fun prepararDadosGrafico() {
//        val graph = findViewById(R.id.graph_atividade) as GraphView
//        val series = LineGraphSeries(lista.toTypedArray())
//        graph.addSeries(series)
    }

    private fun prepararService() {

        handler.postDelayed(myRunnable, 100)
        val intent = Intent(this, ConectarBluetoothService::class.java)
        bindService(intent, sc, Context.BIND_AUTO_CREATE)
        status = true
    }



    private fun prepararClickListeners() {
        botao_voltar_atividade.setOnClickListener {
            if(inicadoGeral){
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Tem certeza que deseja sair? Você irá perder os dados caso confirme.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", DialogInterface.OnClickListener {
                            dialog, id -> finish()
                    })
                    .setNegativeButton("Cancelar", {
                            dialog, id ->
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("Atenção")
                alert.show()
            } else {
                finish()
            }
        }

        botao_iniciar_atividade.setOnClickListener {
            iniciarAtividade()
        }
        botao_pausar_atividade.setOnClickListener {
            pausarAtividade()
        }
        botao_parar_atividade.setOnClickListener {
            pararAtividade(false)
        }
    }

    private fun pararAtividade(desconectado: Boolean) {
        mLastStopTime = SystemClock.elapsedRealtime() - valor_tempo.getBase()
        valor_tempo.stop()
        iniciado = false
        mudarLayoutAtividade()
        var dialog = SalvarDialog(this, {
            handler.removeCallbacks(myRunnable)
            valor_medio = valor_medio/ listaRegistroAtividade.size
            val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val cpf = sharedPref.getString(Constantes.CPF, "")
            var atividade = Atividade(null, Calendar.getInstance() ,  valor_tempo.text.toString(), it,  valor_maximo.toDouble(), valor_minimo.toDouble(), valor_medio.toDouble(), cpf )
            atividade.id = TrackerApplication.database?.atividadeDao()?.insertOrUpdateAtividades(atividade)
            listaRegistroAtividade.forEach {

                TrackerApplication.database?.registroAtividadeDao()?.insertOrUpdateRegistrosAtividades(
                    RegistroAtividade(null, it,atividade.id!!)
                )
            }
            chamarAlertaSucesso(it)
        }, desconectado)
        dialog.show()
    }

    private fun chamarAlertaSucesso(nome: String){

        DialogPadrao(this, "A atividade de nome:  '"+ nome +"' foi salva com sucesso no histórico de atividades",
            "Ok", {
                finish()
            }, "", {
            }, false).show()

//        val dialogBuilder = AlertDialog.Builder(this)
//        dialogBuilder.setMessage("A atividade de nome: <b>"+ nome +"</b> foi salva com sucesso no histórico de atividades")
//            .setCancelable(false)
//            .setPositiveButton("Ok", DialogInterface.OnClickListener {
//                    dialog, id -> finish()
//            })
//
//        val alert = dialogBuilder.create()
//        alert.setTitle("Sucesso")
//        alert.show()
    }

    private fun pausarAtividade() {
        this.iniciado = !iniciado
        if (!iniciado){
            mLastStopTime = SystemClock.elapsedRealtime() - valor_tempo.getBase()
            valor_tempo.stop()
        } else {
            valor_tempo.setBase( SystemClock.elapsedRealtime() - mLastStopTime)
            valor_tempo.start()
        }
        mudarLayoutAtividade()
    }

    private fun mudarLayoutAtividade(){
        if(!iniciado){
            imagem_parar.setImageDrawable(getDrawable(R.drawable.ic_play_arrow))
            texto_botao_pausar.text = "Retomar"
            botao_pausar_atividade.setBackgroundResource(R.drawable.bg_botao_iniciar)
        } else {

            imagem_parar.setImageDrawable(getDrawable(R.drawable.ic_pause))
            texto_botao_pausar.text = "Pausar"
            botao_pausar_atividade.setBackgroundResource(R.drawable.bg_botao_pausar)
        }
    }

    private fun iniciarAtividade() {
        label_atividade_nao_iniciada.visibility = View.GONE
        valor_tempo.visibility = View.VISIBLE
        botao_iniciar_atividade.visibility = View.GONE
        botao_pausar_atividade.visibility = View.VISIBLE
        botao_parar_atividade.visibility = View.VISIBLE
        inicadoGeral = true
        if ( mLastStopTime == 0L )
            valor_tempo.setBase( SystemClock.elapsedRealtime() )
        valor_tempo.start()
        iniciado = true
        bluetoothConectado = conectarBluetoothService!!.bluetoothDevice
    }

    override fun onBackPressed() {
        if(inicadoGeral){
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Tem certeza que deseja sair? Você irá perder os dados caso confirme.")
                .setCancelable(false)
                .setPositiveButton("Ok", DialogInterface.OnClickListener {
                        dialog, id -> finish()
                })
                .setNegativeButton("Cancelar", {
                        dialog, id ->
                })

            val alert = dialogBuilder.create()
            alert.setTitle("Atenção")
            alert.show()
        } else {
            finish()
        }
    }

    var myRunnable: Runnable = object : Runnable {
        override fun run() {
            if(status && conectarBluetoothService != null && conectarBluetoothService!!.conectado){
                var valorAtual = conectarBluetoothService!!.getValorAtual()
                valor_batimentos_cardiacos.text = valorAtual.toString()
                if(iniciado){
                    if(lista.size > 0) lista.removeAt(0)
                    if(valorAtual < valor_minimo){
                        valor_minimo = valorAtual
                    }
                    if(valorAtual > valor_maximo){
                        valor_maximo = valorAtual
                    }
                    valor_medio += valorAtual
//                    lista.add(Entry(contador.toFloat(), valorAtual.toFloat()))
                    addEntry(Entry(contador.toFloat(), valorAtual.toFloat()))
//                    lista.add(DataPoint(contador.toDouble(), valorAtual.toDouble()))
//                    graph_atividade.removeAllSeries()
//                    graph_atividade.addSeries(LineGraphSeries(lista.toTypedArray()))
                    contador++
                    listaRegistroAtividade.add(valorAtual)

                }

                handler.postDelayed(this, 1000)
            } else if(!conectarBluetoothService!!.conectado  && conectarBluetoothService!!.foiDesconectado) {
                pausarAtividade()

                aparelho_conectado.visibility = View.GONE
                mensagem_nao_conectado.visibility = View.VISIBLE
                exibirDesconexao()
                handler.postDelayed(this, 3000)
                conectarBluetoothService!!.foiDesconectado = false
            } else{

                handler.postDelayed(this,1000)
            }
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
        xAxis.valueFormatter = XAxisFormatter()
        chart.axisRight.isEnabled = false
    }


    private fun createSet(): LineDataSet? {
        val set = LineDataSet(null, "Batimentos por minuto")
        set.axisDependency = AxisDependency.LEFT
        set.color = ColorTemplate.getHoloBlue()
        set.setCircleColor(Color.WHITE)
        set.lineWidth = 2f
        set.circleRadius = 4f
        set.fillAlpha = 65
        set.fillColor = ColorTemplate.getHoloBlue()
        set.highLightColor = Color.rgb(244, 117, 117)
        set.valueTextColor = Color.WHITE
        set.valueTextSize = 9f
        set.setDrawValues(false)
        return set
    }
    private var firstIndex: Float = 0f

    private fun addEntry(entry: Entry) {
        val data = chart.data
        if (data != null) {
            var set = data.getDataSetByIndex(0)
            // set.addEntry(...); // can be called as well
            if (set == null) {
                set = createSet()
                data.addDataSet(set)
            }
            data.addEntry(
                entry, 0
            )
            if(data.entryCount > 10)
            {

                data.removeEntry(firstIndex,0)
                firstIndex += 1f
            }
            data.notifyDataChanged()

            // let the chart know it's data has changed
            chart.notifyDataSetChanged()

            // limit the number of visible entries
            chart.setVisibleXRangeMaximum(240f)
//             chart.setVisibleYRange(10F, AxisDependency.LEFT);

            // move to the latest entry
            chart.moveViewToX(data.entryCount.toFloat())

            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    fun setData(count: Int, range: Float) {
        val values = ArrayList<Entry>()
//        for (i in 0 until count) {
//            val f = (Math.random() * range).toFloat() - 30
//            values.add(Entry(i.toFloat(), f))
//        }
        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0
        ) {
            set1 = chart.getData().getDataSetByIndex(0) as LineDataSet
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
}
