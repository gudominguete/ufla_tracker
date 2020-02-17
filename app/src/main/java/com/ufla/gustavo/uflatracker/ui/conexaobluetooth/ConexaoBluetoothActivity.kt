package com.ufla.gustavo.uflatracker.ui.conexaobluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
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


class ConexaoBluetoothActivity() : AppCompatActivity() {

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
        var adapter = DispositivosAdapter(this, mBTDevices)
        recycle_dispositivos.setAdapter(adapter);
    }
}
