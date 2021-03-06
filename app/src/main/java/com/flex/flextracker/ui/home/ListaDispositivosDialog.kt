package com.flex.flextracker.ui.home

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.flex.flextracker.R
import kotlinx.android.synthetic.main.dialog_lista_dispositivos.*

class ListaDispositivosDialog (activity: Activity, funcaoConectar: (BluetoothDevice)->(Unit)): Dialog(activity){

    var mBTDevices: ArrayList<BluetoothDevice> = ArrayList()
    var mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    var activity: Activity
    var funcaoConectar: (BluetoothDevice)->(Unit)
    private var handler = Handler()

    init {
        setCancelable(false)
        this.activity =activity
        this.funcaoConectar = funcaoConectar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_lista_dispositivos)
        if (!mBluetoothAdapter.isEnabled()) {
            val eintent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(eintent, 100)
        }
        prepararClickListener()
        discoverBluetooth()
        atualizarRecycleView()
    }

    private fun prepararClickListener() {
        botao_fechar_dialog_listagem_dispositivos.setOnClickListener {
            dismiss()
        }
        atualizar_lista.setOnClickListener {
            if (!mBluetoothAdapter.isDiscovering()) {
                mBTDevices = arrayListOf()
                mBluetoothAdapter.cancelDiscovery()
                mBluetoothAdapter.startDiscovery()
            }
        }
    }

    private fun atualizarRecycleView() {
        if(mBTDevices.size > 0){

            recycle_dispositivos.layoutManager = LinearLayoutManager(context)

            var adapter = DispositivosAdapter(mBTDevices) {
                loading.visibility = View.VISIBLE
                funcaoConectar(it)
                handler.postDelayed(myRunnable, 10)
            }
            recycle_dispositivos.setAdapter(adapter)
            texto_nao_foram_encontrados.visibility = View.GONE
            recycle_dispositivos.visibility = View.VISIBLE
        } else {
            texto_nao_foram_encontrados.visibility = View.VISIBLE
            recycle_dispositivos.visibility = View.GONE
        }
    }

    private val buscarDispostivosReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothDevice.ACTION_FOUND) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                var possui = false
                mBTDevices.forEach {
                    if(it.name != null && it.name.equals(device.name) && it.address.equals(device.address)){
                        possui = true
                    }
                }
                if (!possui) mBTDevices.add(device)
                atualizarRecycleView()
            }

        }
    }

    var myRunnable: Runnable = object : Runnable {
        override fun run() {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            var conectado = prefs.getBoolean("conectado", false)
            if(conectado){
                dismiss()
            } else {

                var erroConexao = prefs.getBoolean("erroConexao", false)
                if(erroConexao) {
                    mostrarDialogErroConexao()
                    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                    val ed = prefs.edit()
                        .putBoolean("erroConexao", false)
                    ed.apply()
                }

                handler.postDelayed(this, 10)
            }
        }
    }

    private fun mostrarDialogErroConexao(){

        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Não foi possível realizar a conexão com o dispositivo")
            .setCancelable(false)
            .setPositiveButton("Ok", DialogInterface.OnClickListener {
                    dialog, id -> loading.visibility = View.GONE
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Atenção")
        alert.show()
    }

    private fun discoverBluetooth(){
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery()

            //check BT permissions in manifest
            checkBTPermissions()

            mBluetoothAdapter.startDiscovery()
            val discoverDevicesIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
            activity.registerReceiver(buscarDispostivosReceiver, discoverDevicesIntent)
        }
        if (!mBluetoothAdapter.isDiscovering()) {

            //check BT permissions in manifest
            checkBTPermissions()

            mBluetoothAdapter.startDiscovery()
            val discoverDevicesIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
            activity.registerReceiver(buscarDispostivosReceiver, discoverDevicesIntent)
        }
    }


        private fun checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            var permissionCheck =
                activity.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION")
            permissionCheck += activity.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION")
            if (permissionCheck != 0) {

                activity.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1001
                ) //Any number
            }
        } else {
            Log.d(
                "teste",
                "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP."
            )
        }
    }

}