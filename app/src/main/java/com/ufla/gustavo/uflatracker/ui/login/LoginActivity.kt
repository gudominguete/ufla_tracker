package com.ufla.gustavo.uflatracker.ui.login

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.TrackerApplication
import com.ufla.gustavo.uflatracker.utils.KeyboardUtils
import com.ufla.gustavo.uflatracker.utils.StringUtils
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class LoginActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prepararClickListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepararClickListeners() {
        botao_login_cpf.setOnClickListener {
            realizarLoginCpf()
        }
        prepararEventListenerCpf()
        prepararEventListenerNome()
        prepararEventListenerDataNascimento()
        prepararEventListenerPeso()
        prepararEventListenerAltura()
        prepararClickListenerEntrar()
        prepararClickListenerVoltar()


        /*botao_entrar.setOnClickListener{
            val dialog = PerfilDialog(this) {
                var intent = Intent(this, HomeActivity::class.java)
                val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString(Constantes.NOME, it.nome)
                editor.putString(Constantes.IDADE, it.idade)
                editor.putString(Constantes.PESO, it.peso)
                editor.putString(Constantes.ALTURA, it.altura)
                editor.apply()
                startActivity(intent)
            }
            dialog.show()
        }*/
    }

    private fun prepararClickListenerVoltar() {
        botao_voltar_cadastro.setOnClickListener {
            if(verificaExisteDadosFormulario()){
                abrirModalFormularioPreenchido()
            } else{
                voltarFormCpf()
            }
        }

    }

    private fun abrirModalFormularioPreenchido(){
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Existe informações preenchidas no formulário. Se cancelar, os dados serão perdidos. Deseja continuar?")
                .setCancelable(false)
                .setPositiveButton("Sim", DialogInterface.OnClickListener {
                        dialog, id -> voltarFormCpf()
                })
                .setNegativeButton("Não", {
                    dialog, id->
                })

            val alert = dialogBuilder.create()
            alert.setTitle("Atenção")
            alert.show()

    }

    private fun verificaExisteDadosFormulario(): Boolean {
        return !edit_valor_altura.text.isNullOrEmpty() ||
                !edit_valor_idade.rawText.isNullOrEmpty() ||
                !edit_valor_nome.text.isNullOrEmpty() ||
                !edit_valor_peso.text.isNullOrEmpty()
    }

    private fun voltarFormCpf() {
        resetarErros()
        layout_cpf.visibility = View.VISIBLE
        layout_formulario.visibility = View.GONE
        edit_valor_cpf.setText("")
        KeyboardUtils.hideKeyboardFrom(this.applicationContext,  botao_login_cpf)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepararClickListenerEntrar() {
        botao_cadastrar.setOnClickListener {
            validarFormulario()
        }
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
                texto_erro_altura.visibility = View.GONE
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
                texto_erro_peso.visibility = View.GONE
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
                texto_erro_idade.visibility = View.GONE
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
            texto_erro_nome.visibility = View.GONE
        }
        })
    }

    private fun prepararEventListenerCpf() {
        edit_valor_cpf.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                texto_erro_cpf.visibility = View.GONE
            }
        })
    }

    private fun realizarLoginCpf() {
        if(!edit_valor_cpf.rawText.isNullOrEmpty() && edit_valor_cpf.rawText.length == 11){
            var usuario =
                TrackerApplication.database?.usuarioDao()?.getUsuariosByCpf(edit_valor_cpf.rawText)
            if(usuario == null){
                esconderFormularioCpf()
            } else {
                //TODO: Fazer quando já tiver usuario
                Toast.makeText(this, "Sucesso", Toast.LENGTH_LONG).show()
            }
        } else{
            texto_erro_cpf.visibility = View.VISIBLE
        }
    }

    private fun esconderFormularioCpf(){
        layout_cpf.visibility = View.GONE
        texto_erro_cpf.visibility = View.GONE
        layout_formulario.visibility = View.VISIBLE
        text_cpf_nao_encontrado.text = "O Cpf " + StringUtils.formatCpf(edit_valor_cpf.rawText) + " não está cadastrado."
        KeyboardUtils.hideKeyboardFrom(this.applicationContext,  botao_login_cpf)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun validarFormulario() : Boolean{
        var valido = true
        resetarErros()
        if(edit_valor_nome.text.toString().isNullOrEmpty()){
            texto_erro_nome.visibility = View.VISIBLE
            valido = false
        }
        valido = validarPeso() && valido
        valido = validarDataNascimento() && valido
        valido = validarAltura() && valido
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
            texto_erro_peso.text = mensagemErro
            texto_erro_peso.visibility = View.VISIBLE
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
            texto_erro_altura.text = mensagemErro
            texto_erro_altura.visibility = View.VISIBLE
        }
        return valido
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
            texto_erro_idade.text = mensagemErro
            texto_erro_idade.visibility = View.VISIBLE
        }

        return valido
    }

    private fun resetarErros() {
        texto_erro_nome.visibility = View.GONE
        texto_erro_idade.visibility = View.GONE
        texto_erro_peso.visibility = View.GONE
        texto_erro_altura.visibility = View.GONE
    }
}
