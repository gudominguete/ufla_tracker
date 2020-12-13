package com.flex.flextracker.ui.perfil

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.flex.flextracker.R
import com.flex.flextracker.TrackerApplication
import com.flex.flextracker.entity.Usuario
import com.flex.flextracker.ui.util.DialogPadrao
import com.flex.flextracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_editar_perfil.*
import kotlinx.android.synthetic.main.activity_editar_perfil.edit_valor_altura
import kotlinx.android.synthetic.main.activity_editar_perfil.edit_valor_idade
import kotlinx.android.synthetic.main.activity_editar_perfil.edit_valor_nome
import kotlinx.android.synthetic.main.activity_editar_perfil.edit_valor_peso
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
            if(verificaExisteDadosFormulario()){
                abrirModalFormularioPreenchido()
            } else {
                finish()
            }
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
                ConfirmarEdicaoDialog(this) {
                    finish()
                }.show()
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
            edit_valor_peso.setText(usuario?.peso.toString().replace(".", ","))
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
            texto_erro_peso_editar.text = mensagemErro
            texto_erro_peso_editar.visibility = View.VISIBLE
        }
        return valido
    }

    override fun onBackPressed() {
        if(verificaExisteDadosFormulario()){
            abrirModalFormularioPreenchido()
        } else {
            super.onBackPressed()
        }
    }

    private fun abrirModalFormularioPreenchido(){
        DialogPadrao(this, "Existem informações modificadas. Se voltar para a tela anterior, os dados modificados serão perdidos e voltará para os dados originais. Deseja voltar para a tela anterior?",
        "Sim", {
                finish()
            }, "Não", {

            }, true).show()
    }

    private fun verificaExisteDadosFormulario(): Boolean {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val dataNascUsuario = format.format(usuario?.dataNasc?.time)
        return !edit_valor_altura.text.toString().equals(usuario?.altura.toString()) ||
                !edit_valor_idade.text.toString().equals(dataNascUsuario) ||
                !edit_valor_nome.text.toString().equals(usuario?.nome) ||
                !edit_valor_peso.text.toString().toDouble().toInt().equals(usuario?.peso?.toInt())
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
