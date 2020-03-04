package com.ufla.gustavo.uflatracker.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prepararClickListeners()
    }

    private fun prepararClickListeners() {
        botao_entrar.setOnClickListener{
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
//
//        botao_adicionar_registro.setOnClickListener {
//
//            val p1 = RegistroAtividade(56, 80,1)
//
//            TrackerApplication.database?.registroAtividadeDao()?.insertOrUpdateRegistrosAtividades(p1)
//
//            var lista = TrackerApplication.database?.registroAtividadeDao()?.getRegistrosAtividades()
//
//            Toast.makeText(this, lista?.size.toString(), Toast.LENGTH_LONG).show()
//        }
//
//        botao_historico_atividades.setOnClickListener {
//            var intent = Intent(this, HistoricoActivity::class.java)
//            startActivity(intent)
//        }
//
//        botao_conexao_bluetooth.setOnClickListener {
//            var intent = Intent(this, ConexaoBluetoothActivity::class.java)
//            startActivity(intent)
//        }
    }
}
