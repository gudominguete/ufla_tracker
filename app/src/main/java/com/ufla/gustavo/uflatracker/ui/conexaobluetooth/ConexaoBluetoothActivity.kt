package com.ufla.gustavo.uflatracker.ui.conexaobluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_conexao_bluetooth.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


private const val BASE_UUID = "00000000-0000-1000-8000-00805F9B34FB"

class ConexaoBluetoothActivity() : AppCompatActivity() {


    var socket : BluetoothSocket? = null;
    var mBTDevices: ArrayList<BluetoothDevice> = ArrayList()
    var mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ufla.gustavo.uflatracker.R.layout.activity_conexao_bluetooth)


        if (!mBluetoothAdapter.isEnabled()) {
            val eintent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(eintent, 100)
        }

        discoverBluetooth()
    }

    private fun discoverBluetooth(){
        Log.d("teste", "btnDiscover: Looking for unpaired devices.")

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery()
            Log.d("teste", "btnDiscover: Canceling discovery.")

            //check BT permissions in manifest
            checkBTPermissions()

            mBluetoothAdapter.startDiscovery()
            val discoverDevicesIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent)
        }
        if (!mBluetoothAdapter.isDiscovering()) {

            //check BT permissions in manifest
            checkBTPermissions()

            mBluetoothAdapter.startDiscovery()
            val discoverDevicesIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent)
        }
    }

    private fun checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            var permissionCheck =
                this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION")
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION")
            if (permissionCheck != 0) {

                this.requestPermissions(
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

    private val mBroadcastReceiver3 = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            Log.d("teste", "onReceive: ACTION FOUND.")

            if (action == BluetoothDevice.ACTION_FOUND) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                mBTDevices.add(device)

                Log.d("teste", "onReceive: " + device.name + ": " + device.address)
                atualizarRecycleView()
            }
        }
    }

    private fun atualizarRecycleView() {
        recycle_dispositivos.layoutManager = LinearLayoutManager(this)
        var adapter = DispositivosAdapter(this, mBTDevices, {
            Log.i("teste", it.name)
            parearDispositivo(it)
        })
        recycle_dispositivos.setAdapter(adapter);
    }

    private fun parearDispositivo(bluetoothDevice: BluetoothDevice){

        var connectionThread = ConnectionThread(bluetoothDevice)
        { isSuccess -> {Log.i("SUCESSO", "true")}}
        connectionThread?.start()
    }

    private class ConnectionThread(private val device : BluetoothDevice,
                                   private val onComplete: (isSuccess : Boolean) -> Unit) : Thread() {

        private var bluetoothAdapter : BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        private var bluetoothSocket : BluetoothSocket? = createSocket();

        private fun createSocket() : BluetoothSocket? {
            var socket : BluetoothSocket? = null;

            try {
                val uuid = if (device.uuids!= null && device.uuids.size > 0)
                    device.uuids[0].uuid
                else UUID.fromString(BASE_UUID);

                socket = device.createRfcommSocketToServiceRecord(uuid)
            }
            catch (e : IOException) {}

            return socket;
        }

        override fun run() {
            super.run()

            bluetoothAdapter.cancelDiscovery()
            var isSuccess = false

            try {
                if (bluetoothSocket != null) {
                    bluetoothSocket?.connect()
                    isSuccess = true
                }

            }
            catch (e: Exception) {
                Log.e("error", e.message)
            }

            onComplete(isSuccess)
        }

        fun cancel() {
            if (bluetoothSocket != null)
                bluetoothSocket?.close()
        }
    }
}
