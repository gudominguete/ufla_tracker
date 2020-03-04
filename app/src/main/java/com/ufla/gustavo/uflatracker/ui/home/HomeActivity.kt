package com.ufla.gustavo.uflatracker.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.ui.perfil.PerfilActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        configurarClickListeners()
    }

    private fun configurarClickListeners() {
        botao_perfil.setOnClickListener {

            var intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

    }
}
