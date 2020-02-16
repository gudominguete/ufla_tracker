package com.ufla.gustavo.uflatracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ufla.gustavo.uflatracker.entity.RegistroAtividade
import com.ufla.gustavo.uflatracker.ui.GraphicActivity
import com.ufla.gustavo.uflatracker.ui.conexaobluetooth.ConexaoBluetoothActivity
import com.ufla.gustavo.uflatracker.ui.historico.HistoricoActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepararClickListeners()
    }

    private fun prepararClickListeners() {
        botao_graficos.setOnClickListener{
            var intent = Intent(this, GraphicActivity::class.java)
            startActivity(intent)
        }

        botao_adicionar_registro.setOnClickListener {

            val p1 = RegistroAtividade(56, 80,1)

            TrackerApplication.database?.registroAtividadeDao()?.insertOrUpdateRegistrosAtividades(p1)

            var lista = TrackerApplication.database?.registroAtividadeDao()?.getRegistrosAtividades()

            Toast.makeText(this, lista?.size.toString(), Toast.LENGTH_LONG).show()
        }

        botao_historico_atividades.setOnClickListener {
            var intent = Intent(this, HistoricoActivity::class.java)
            startActivity(intent)
        }

        botao_conexao_bluetooth.setOnClickListener {
            var intent = Intent(this, ConexaoBluetoothActivity::class.java)
            startActivity(intent)
        }
    }
}
