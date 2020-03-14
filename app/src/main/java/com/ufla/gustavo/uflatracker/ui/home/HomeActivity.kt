package com.ufla.gustavo.uflatracker.ui.home

import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.service.ConectarBluetoothService
import com.ufla.gustavo.uflatracker.ui.atividade.AtividadeActivity
import com.ufla.gustavo.uflatracker.ui.historico.HistoricoActivity
import com.ufla.gustavo.uflatracker.ui.perfil.PerfilActivity
import com.ufla.gustavo.uflatracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    private lateinit var bluetoothDeviceSelecionado: BluetoothDevice
    private lateinit var conectarBluetoothService: ConectarBluetoothService
    private var status = false
    private var handler = Handler()

    private val sc = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ConectarBluetoothService.ConectarBluetoothBinder
            conectarBluetoothService = binder.service
            status = true
            conectarBluetoothService.iniciarHandler(bluetoothDeviceSelecionado, this@HomeActivity)
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        configurarClickListeners()
        iniciarHeader()
    }

    private fun iniciarHeader() {
        val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val nome = sharedPref.getString(Constantes.NOME, "")
        texto_nome.text = nome
    }

    private fun configurarClickListeners() {
        botao_perfil.setOnClickListener {

            var intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }
        botao_historico.setOnClickListener {

            var intent = Intent(this, HistoricoActivity::class.java)
            startActivity(intent)
        }
        botao_iniciar_home.setOnClickListener {
            var intent = Intent(this, AtividadeActivity::class.java)
            startActivity(intent)
        }
        botao_conectar_bluetooth.setOnClickListener {
            val dialog = ListaDispositivosDialog(this){
                bluetoothDeviceSelecionado = it
                val intent = Intent(this, ConectarBluetoothService::class.java)
                bindService(intent, sc, Context.BIND_AUTO_CREATE)
                status = true
                handler.postDelayed(myRunnable, 100)
            }
            dialog.show()
        }
    }

    var myRunnable: Runnable = object : Runnable {
        override fun run() {
            if(status){
                conectarBluetoothService.getValorAtual()
                layout_conectar_dispositivo.visibility = View.GONE
                mensagem_nao_conectado.visibility = View.GONE
                aparelho_conectado.visibility = View.VISIBLE
                valor_nome_bluetooth.text = bluetoothDeviceSelecionado.name
                layout_batimento_home.visibility = View.VISIBLE
                var valorAtual = conectarBluetoothService!!.getValorAtual()
                valor_batimentos_cardiacos_home.text = valorAtual.toString()
            }

            handler.postDelayed(this, 1000)
        }
    }
}
