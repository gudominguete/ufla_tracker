package com.ufla.gustavo.uflatracker.ui.atividade

import android.bluetooth.BluetoothDevice
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.TrackerApplication
import com.ufla.gustavo.uflatracker.entity.Atividade
import com.ufla.gustavo.uflatracker.entity.RegistroAtividade
import com.ufla.gustavo.uflatracker.service.ConectarBluetoothService
import com.ufla.gustavo.uflatracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_atividade.*
import java.util.*

class AtividadeActivity : AppCompatActivity() {

    private var conectarBluetoothService: ConectarBluetoothService? = null
    private var status = false
    private var iniciado = false
    private var inicadoGeral = false
    private var lista = mutableListOf<DataPoint>()
    private var listaRegistroAtividade = mutableListOf<Int>()
    private var valor_minimo = 12000
    private var valor_maximo = 0
    private var valor_medio = 0
    private var contador = 51
    private var mLastStopTime: Long = 0

    var bluetoothConectado: BluetoothDevice? =null

    private var handler = Handler()

    private val sc = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ConectarBluetoothService.ConectarBluetoothBinder
            conectarBluetoothService = binder.service
            status = true
        }
        override fun onServiceDisconnected(name: ComponentName) {

            conectarBluetoothService = null
            status = false
            exibirDesconexao()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividade)
        prepararClickListeners()
        iniciarHeader()
        prepararLista()
        prepararService()
        prepararDadosGrafico()

    }


    private fun exibirDesconexao(){
        lateinit var dialog:AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("O equipamento foi desconectado")
        builder.setMessage("O equipamento foi desconectado, você deseja tentar encerrar a atividade?")
        val dialogClickListener = DialogInterface.OnClickListener{_,which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> pararAtividade()
            }
        }
        builder.setPositiveButton("Sim",dialogClickListener)
        dialog = builder.create()
        dialog.show()
    }

    private fun prepararLista() {
        for (i in 1..50){
            lista.add(DataPoint(i.toDouble(), 0.toDouble()))
        }
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
        val graph = findViewById(R.id.graph_atividade) as GraphView
        val series = LineGraphSeries(lista.toTypedArray())
        graph.addSeries(series)
    }

    private fun prepararService() {

        handler.postDelayed(myRunnable, 100)
        val intent = Intent(this, ConectarBluetoothService::class.java)
        bindService(intent, sc, Context.BIND_AUTO_CREATE)
        status = true
    }

    private fun iniciarHeader() {
        val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val nome = sharedPref.getString(Constantes.NOME, "")
        texto_nome_atividade.text = nome
    }

    private fun prepararClickListeners() {
        botao_voltar_atividade.setOnClickListener {
            finish()
        }

        botao_iniciar_atividade.setOnClickListener {
            iniciarAtividade()
        }
        botao_pausar_atividade.setOnClickListener {
            pausarAtividade()
        }
        botao_parar_atividade.setOnClickListener {
            pararAtividade()
        }
    }

    private fun pararAtividade() {
        mLastStopTime = SystemClock.elapsedRealtime() - valor_tempo.getBase()
        valor_tempo.stop()
        iniciado = false
        mudarLayoutAtividade()
        var dialog = SalvarDialog(this){
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
        }
        dialog.show()
    }

    private fun chamarAlertaSucesso(nome: String){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("A atividade de nome: "+ nome +" foi salva com sucesso no histórico de atividades")
            .setCancelable(false)
            .setPositiveButton("Ok", DialogInterface.OnClickListener {
                    dialog, id -> finish()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Sucesso")
        alert.show()
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
        }
    }

    var myRunnable: Runnable = object : Runnable {
        override fun run() {
            if(status && conectarBluetoothService != null && conectarBluetoothService!!.conectado){
                var valorAtual = conectarBluetoothService!!.getValorAtual()
                valor_batimentos_cardiacos.text = valorAtual.toString()
                if(iniciado){
                    lista.removeAt(0)
                    if(valorAtual < valor_minimo){
                        valor_minimo = valorAtual
                    }
                    if(valorAtual > valor_maximo){
                        valor_maximo = valorAtual
                    }
                    valor_medio += valorAtual

                    lista.add(DataPoint(contador.toDouble(), valorAtual.toDouble()))
                    graph_atividade.removeAllSeries()
                    graph_atividade.addSeries(LineGraphSeries(lista.toTypedArray()))
                    contador++
                    listaRegistroAtividade.add(valorAtual)

                }

                handler.postDelayed(this, 1000)
            } else if(!conectarBluetoothService!!.conectado  && conectarBluetoothService!!.foiDesconectado) {
                pausarAtividade()
                exibirDesconexao()
                handler.postDelayed(this, 3000)
                conectarBluetoothService!!.foiDesconectado = false
            } else{

                handler.postDelayed(this,1000)
            }
        }
    }
}
