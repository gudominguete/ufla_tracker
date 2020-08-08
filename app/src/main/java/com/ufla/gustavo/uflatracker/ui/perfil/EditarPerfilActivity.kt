package com.ufla.gustavo.uflatracker.ui.perfil

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.TrackerApplication
import com.ufla.gustavo.uflatracker.entity.Usuario
import com.ufla.gustavo.uflatracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_editar_perfil.*
import kotlinx.android.synthetic.main.activity_editar_perfil.edit_valor_altura
import kotlinx.android.synthetic.main.activity_editar_perfil.edit_valor_idade
import kotlinx.android.synthetic.main.activity_editar_perfil.edit_valor_nome
import kotlinx.android.synthetic.main.activity_editar_perfil.edit_valor_peso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_perfil.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class EditarPerfilActivity : AppCompatActivity() {

    private var usuario: Usuario? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)
        iniciarDados()
        prepararClickListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepararClickListeners() {
        back_button_editar.setOnClickListener {
            finish()
        }
        prepararEventListenerNome()
        prepararEventListenerAltura()
        prepararEventListenerPeso()
        prepararEventListenerDataNascimento()
        botao_editar_confirmar.setOnClickListener {
            if(validarFormulario()){
                val dataNascimento = Calendar.getInstance()
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                dataNascimento.time = sdf.parse(edit_valor_idade.text.toString())
                var peso =  edit_valor_peso.text.toString().replace(",", ".").toDouble()
                var altura = edit_valor_altura.text.toString().toInt()
                editarUsuario(usuario!!.cpf, edit_valor_nome.text.toString(), dataNascimento,
                    peso, altura)
                Toast.makeText(this, "Perfil editado com sucesso!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun iniciarDados() {
        val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val cpf = sharedPref.getString(Constantes.CPF, "")
        usuario = TrackerApplication.database?.usuarioDao()?.getUsuariosByCpf(cpf)!!
        if(usuario != null){
            text_cpf.setText("CPF: " + usuario?.cpf)
            edit_valor_nome.setText(usuario?.nome)
            edit_valor_altura.setText(usuario?.altura.toString())
            edit_valor_peso.setText(usuario?.peso.toString())
            val format = SimpleDateFormat("dd/MM/yyy")
            edit_valor_idade.setText(format.format(usuario?.dataNasc?.time))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun validarFormulario() : Boolean{
        var valido = true
        resetarErros()
        if(edit_valor_nome.text.toString().isNullOrEmpty()){
            texto_erro_nome_editar.visibility = View.VISIBLE
            valido = false
        }
        valido = validarPeso() && valido
        valido = validarDataNascimento() && valido
        valido = validarAltura() && valido
        return valido
    }

    private fun resetarErros() {
        texto_erro_nome_editar.visibility = View.GONE
        texto_erro_idade_editar.visibility = View.GONE
        texto_erro_peso_editar.visibility = View.GONE
        texto_erro_altura_editar.visibility = View.GONE
    }

    private fun editarUsuario(cpf: String, nome: String, dataNasc: Calendar, peso: Double, altura: Int) {

        var usuario = Usuario(cpf, nome, altura, dataNasc, peso )

        TrackerApplication.database?.usuarioDao()?.insertOrUpdateAtividades(usuario)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun validarDataNascimento(): Boolean{
        var data = edit_valor_idade.text.toString()
        var mensagemErro = ""
        var valido = true
        if(data.isNullOrEmpty()){
            valido = false
            mensagemErro = "O campo data de nascimento não foi preenchido"
        } else{

            try {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
                LocalDate.parse(data, formatter)
            }catch (ex: Exception){
                mensagemErro = "A data informada não é uma data real"
                valido = false;
            }
        }

        if(!valido){
            texto_erro_idade_editar.text = mensagemErro
            texto_erro_idade_editar.visibility = View.VISIBLE
        }

        return valido
    }

    private fun validarPeso(): Boolean{
        var peso = edit_valor_peso.text.toString()
        var mensagemErro = ""
        var valido = true
        if(peso.isNullOrEmpty()){
            valido = false
            mensagemErro = "O campo peso não foi preenchido"
        } else {
            try{
                peso.replace(",", ".").toDouble()
            } catch (exception: Exception){
                valido = false
                mensagemErro = "O valor informado não corresponde a um peso correto."
            }
        }

        if(!valido){
            texto_erro_peso_editar.text = mensagemErro
            texto_erro_peso_editar.visibility = View.VISIBLE
        }
        return valido
    }

    private fun validarAltura(): Boolean{
        var altura = edit_valor_altura.text.toString()
        var mensagemErro = ""
        var valido = true
        if(altura.isNullOrEmpty()){
            valido = false
            mensagemErro = "O campo peso não foi preenchido"
        } else {
            try{
                var altura = altura.toInt()
                if(altura > 230){
                    valido = false
                    mensagemErro = "O valor informado não corresponde a uma altura correta."
                }
            } catch (exception: Exception){
                valido = false
                mensagemErro = "O valor informado não corresponde a uma altura correta."
            }
        }

        if(!valido){
            texto_erro_altura_editar.text = mensagemErro
            texto_erro_altura_editar.visibility = View.VISIBLE
        }
        return valido
    }

    private fun prepararEventListenerAltura() {

        edit_valor_altura.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                texto_erro_altura_editar.visibility = View.GONE
            }
        })
    }

    private fun prepararEventListenerPeso() {
        edit_valor_peso.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                texto_erro_peso_editar.visibility = View.GONE
            }
        })
    }

    private fun prepararEventListenerDataNascimento() {
        edit_valor_idade.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                texto_erro_idade_editar.visibility = View.GONE
            }
        })
    }

    private fun prepararEventListenerNome() {
        edit_valor_nome.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                texto_erro_nome_editar.visibility = View.GONE
            }
        })
    }


}
