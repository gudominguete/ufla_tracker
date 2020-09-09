package com.ufla.gustavo.uflatracker.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.TrackerApplication
import com.ufla.gustavo.uflatracker.service.ConectarBluetoothService
import com.ufla.gustavo.uflatracker.ui.atividade.AtividadeActivity
import com.ufla.gustavo.uflatracker.ui.historico.HistoricoActivity
import com.ufla.gustavo.uflatracker.ui.perfil.PerfilActivity
import com.ufla.gustavo.uflatracker.utils.Constantes
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    private lateinit var bluetoothDeviceSelecionado: BluetoothDevice
    private var conectarBluetoothService: ConectarBluetoothService? = null
    private var status = false
    private var handler = Handler()
    private var loadingHandler = Handler()
    private var begin : Long = 0
    private var end : Long = 0


    private val sc = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ConectarBluetoothService.ConectarBluetoothBinder
            conectarBluetoothService = binder.service
            status = true
            conectarBluetoothService?.iniciarHandler(bluetoothDeviceSelecionado, this@HomeActivity)
        }

        override fun onServiceDisconnected(name: ComponentName) {

            status = false
            conectarBluetoothService = null
        }
    }


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        configurarClickListeners()
        iniciarHeader()
    }

    fun habilitarBotaoEntrar(){
        botao_iniciar_home.backgroundTintList = ContextCompat.getColorStateList(this, R.color.green)
        botao_iniciar_home.setTextColor(-0x1)
        botao_iniciar_home.setIconTintResource(R.color.white)

    }
    fun desabilitarBotarEntrar(){
        botao_iniciar_home.backgroundTintList = ContextCompat.getColorStateList(this, R.color.cinza)
        botao_iniciar_home.setTextColor(-0x1000000)
        botao_iniciar_home.setIconTintResource(R.color.black)
    }

    override fun onRestart() {
        super.onRestart()
        iniciarHeader()
        handler.postDelayed(myRunnable, 100)
    }

    private fun showLoading(){
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        progressBar?.visibility = View.VISIBLE
        begin = System.currentTimeMillis()
        loadingHandler.postDelayed(loadingRunnable, 100)
    }

    private fun hideLoading(){
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progressBar?.visibility = View.GONE
        loadingHandler.removeCallbacks(loadingRunnable)
    }

    private fun iniciarHeader() {
        val sharedPref = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val cpf = sharedPref.getString(Constantes.CPF, "")
        var usuario =
            TrackerApplication.database?.usuarioDao()?.getUsuariosByCpf(cpf =cpf )
        if(usuario != null){

            texto_nome.text = usuario.nome
        }
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

            if(status){
                handler.removeCallbacks(myRunnable)
                var intent = Intent(this, AtividadeActivity::class.java)
                startActivity(intent)
            } else {
                abrirModalAtencaoSemDispositivo()
            }
        }
        botao_conectar_bluetooth.setOnClickListener {
            checkBTPermissions()
        }
        desconectar_equipamento.setOnClickListener {
            abrirModalDesconectar()
        }
        botao_logout_layout.setOnClickListener {
            finish()
        }
    }

    private fun abrirModalDesconectar(){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Você realmente deseja desconectar o equipamento vestível?")
            .setCancelable(false)
            .setPositiveButton("Sim", DialogInterface.OnClickListener {
                    dialog, id -> conectarBluetoothService?.disconnect()
            })
            .setNegativeButton("Não", {
                dialog, which ->
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Atenção")
        alert.show()
    }

    private fun abrirModalAtencaoSemDispositivo(){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Para iniciar a atividade deve-se selecionar um monitor de batimento cardíaco! A conexão é realizada ao clicar no botão conectar na página principal.")
            .setCancelable(false)
            .setPositiveButton("Ok", DialogInterface.OnClickListener {
                    dialog, id ->
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Atenção")
        alert.show()
    }

    var loadingRunnable: Runnable = object : Runnable{
        override fun run() {
            end = System.currentTimeMillis()
            if((end-begin)<10000)
                loadingHandler.postDelayed(this, 100)
            else
                hideLoading()
        }
    }

    var myRunnable: Runnable = object : Runnable {
        override fun run() {
            if(status && conectarBluetoothService != null && conectarBluetoothService!!.conectado){
                if(progressBar.visibility.equals(View.VISIBLE)){
                    hideLoading()
                }
                conectarBluetoothService?.getValorAtual()
                habilitarBotaoEntrar()
                layout_conectar_dispositivo.visibility = View.GONE
                mensagem_nao_conectado.visibility = View.GONE
                aparelho_conectado.visibility = View.VISIBLE
                valor_nome_bluetooth.text = bluetoothDeviceSelecionado.name
                layout_batimento_home.visibility = View.VISIBLE
                var valorAtual = conectarBluetoothService?.getValorAtual()
                valor_batimentos_cardiacos_home.text = valorAtual.toString()
            } else if(conectarBluetoothService != null && conectarBluetoothService!!.foiDesconectado){

                layout_conectar_dispositivo.visibility = View.VISIBLE
                mensagem_nao_conectado.visibility = View.VISIBLE
                aparelho_conectado.visibility = View.GONE
                valor_nome_bluetooth.text = ""
                layout_batimento_home.visibility = View.GONE
                valor_batimentos_cardiacos_home.text = ""
                exibirDesconexao()
//                Toast.makeText(this@HomeActivity, "O aparelho foi desconectado", Toast.LENGTH_LONG).show()
                desabilitarBotarEntrar()
                conectarBluetoothService?.foiDesconectado = false
            } else if(conectarBluetoothService != null && !conectarBluetoothService!!.conectado){
                layout_conectar_dispositivo.visibility = View.VISIBLE
                mensagem_nao_conectado.visibility = View.VISIBLE
                aparelho_conectado.visibility = View.GONE
                valor_nome_bluetooth.text = ""
                layout_batimento_home.visibility = View.GONE
                valor_batimentos_cardiacos_home.text = ""
                desabilitarBotarEntrar()
            }
            handler.postDelayed(this, 500)

        }
    }

    private fun exibirDesconexao(){
        lateinit var dialog:AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("O equipamento foi desconectado")
        builder.setMessage("O equipamento foi desconectado!")
        val dialogClickListener = DialogInterface.OnClickListener{_,which ->
        }
        builder.setPositiveButton("Ok",dialogClickListener)
        dialog = builder.create()
        dialog.show()
    }


    private fun checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        1001)
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        1001)

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                abrirListaDispostivos()
            }

        } else {
            Log.d(
                "teste",
                "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP."
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1001 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    abrirListaDispostivos()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }



    private fun abrirListaDispostivos() {

        val dialog = ListaDispositivosDialog(this){
            bluetoothDeviceSelecionado = it
            if(conectarBluetoothService == null) {
                val intent = Intent(this, ConectarBluetoothService::class.java)
                bindService(intent, sc, Context.BIND_AUTO_CREATE)

            }else {
                conectarBluetoothService!!.load()
                conectarBluetoothService!!.connect(it)
            }

            showLoading()
            status = true
            handler.postDelayed(myRunnable, 100)
        }
        dialog.show()
    }
}
