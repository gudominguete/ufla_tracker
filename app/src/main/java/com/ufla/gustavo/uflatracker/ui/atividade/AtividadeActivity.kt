package com.ufla.gustavo.uflatracker.ui.atividade

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.service.ConectarBluetoothService
import com.ufla.gustavo.uflatracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_atividade.*
import kotlinx.android.synthetic.main.activity_historico.*
import kotlin.random.Random

class AtividadeActivity : AppCompatActivity() {

    private lateinit var conectarBluetoothService: ConectarBluetoothService
    private var status = false

    private var handler = Handler()

    private val sc = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ConectarBluetoothService.ConectarBluetoothBinder
            conectarBluetoothService = binder.service
            status = true
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividade)
        prepararClickListeners()
        iniciarHeader()
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
    }

    private fun iniciarAtividade() {
        botao_iniciar_atividade.visibility = View.GONE
        botao_pausar_atividade.visibility = View.VISIBLE
        botao_parar_atividade.visibility = View.VISIBLE
    }


    var myRunnable: Runnable = object : Runnable {
        override fun run() {
            if(status){
                valor_batimentos_cardiacos.text = conectarBluetoothService.getValorAtual().toString()
            }

            handler.postDelayed(this, 1000)
        }
    }
}
