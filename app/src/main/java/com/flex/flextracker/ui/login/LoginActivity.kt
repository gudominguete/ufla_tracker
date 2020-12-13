package com.flex.flextracker.ui.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.flex.flextracker.R
import com.flex.flextracker.TrackerApplication
import com.flex.flextracker.entity.Usuario
import com.flex.flextracker.ui.home.HomeActivity
import com.flex.flextracker.utils.Constantes
import com.flex.flextracker.utils.KeyboardUtils
import com.flex.flextracker.utils.StringUtils
import kotlinx.android.synthetic.main.activity_login.*
import java.text.SimpleDateFormat
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

    override fun onResume() {
        super.onResume()
        voltarFormCpf()

    }

    private fun abrirModalFormularioPreenchido(){
        CancelarCadastroDialog(this, {
            voltarFormCpf()
        }).show()
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
            if(validarFormulario()){
                val dataNascimento = Calendar.getInstance()
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                dataNascimento.time = sdf.parse(edit_valor_idade.text.toString())
                var peso =  edit_valor_peso.text.toString().replace(",", ".").toDouble()
                var altura = edit_valor_altura.text.toString().toInt()
                salvarUsuario(edit_valor_cpf.rawText, edit_valor_nome.text.toString(), dataNascimento,
                    peso, altura)
                
                fazerLogin(edit_valor_cpf.rawText, edit_valor_nome.text.toString(), dataNascimento.toString(),
                    edit_valor_peso.text.toString(), edit_valor_altura.text.toString())
            }
        }
    }

    private fun salvarUsuario(cpf: String, nome: String, dataNasc: Calendar, peso: Double, altura: Int) {

        var usuario = Usuario(cpf, nome, altura, dataNasc, peso )

        TrackerApplication.database?.usuarioDao()?.insertOrUpdateAtividades(usuario)
    }

    private fun fazerLogin(cpf: String, nome: String, idade: String, peso: String, altura: String){

        var intent = Intent(this, HomeActivity::class.java)
        val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString(Constantes.CPF, cpf)
        editor.putString(Constantes.NOME, nome)
        editor.putString(Constantes.IDADE,idade)
        editor.putString(Constantes.PESO, peso)
        editor.putString(Constantes.ALTURA, altura)
        editor.apply()
        startActivity(intent)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun realizarLoginCpf() {
        if(!edit_valor_cpf.rawText.isNullOrEmpty() && edit_valor_cpf.rawText.length == 11){
            if(StringUtils.validateCPF(edit_valor_cpf.rawText)){

                var usuario =
                    TrackerApplication.database?.usuarioDao()?.getUsuariosByCpf(edit_valor_cpf.rawText)
                if(usuario == null){
                    esconderFormularioCpf()
                } else {
                    fazerLogin(usuario.cpf, usuario.nome!! ,usuario.dataNasc.toString(),
                        usuario.peso.toString(), usuario.altura.toString() )
                }
            } else {
                texto_erro_cpf.text = "O CPF digitado é inválido!"
                texto_erro_cpf.visibility = View.VISIBLE
            }
        } else{
            texto_erro_cpf.text = "O campo CPF não foi preenchido!"
            texto_erro_cpf.visibility = View.VISIBLE
        }
    }

    private fun esconderFormularioCpf(){
        layout_cpf.visibility = View.GONE
        texto_erro_cpf.visibility = View.GONE
        layout_formulario.visibility = View.VISIBLE
        text_cpf_nao_encontrado.text = "O CPF " + StringUtils.formatCpf(edit_valor_cpf.rawText) + " não está cadastrado."
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
                var pesoInput  = peso.replace(",", ".").toDouble()
                if(pesoInput < 15 ) {

                    valido = false
                    mensagemErro = "O valor informado é muito baixo. O valor mínimo é de 15 kg."
                }
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
            mensagemErro = "O campo altura não foi preenchido"
        } else {
            try{
                var altura = altura.toInt()
                if(altura > 230 || altura < 100){
                    valido = false
                    mensagemErro = "O valor informado não corresponde a uma altura correta. Os valores permitidos são de 100cm a 230cm"
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
        if(edit_valor_idade.rawText.toString().isNullOrEmpty()){
            valido = false
            mensagemErro = "O campo data de nascimento não foi preenchido"
        } else{

            try {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
                var dataInput = LocalDate.parse(data, formatter)
                if(dataInput > LocalDate.now()) {
                    mensagemErro = "A data informada é maior que a data atual"
                    valido = false;
                }
            }catch (ex: Exception){
                mensagemErro = "A data informada não é uma data real. Por favor digite uma data correta."
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
