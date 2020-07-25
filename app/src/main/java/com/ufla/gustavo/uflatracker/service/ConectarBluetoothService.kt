package com.ufla.gustavo.uflatracker.service

import android.app.Activity
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.ufla.gustavo.uflatracker.ui.conexaobluetooth.hr.AndroidBLEHRProvider
import com.ufla.gustavo.uflatracker.ui.conexaobluetooth.hr.HRDeviceRef
import com.ufla.gustavo.uflatracker.ui.conexaobluetooth.hr.HRManager
import com.ufla.gustavo.uflatracker.ui.conexaobluetooth.hr.HRProvider
import kotlinx.android.synthetic.main.activity_graphic.*
import java.util.*
import kotlin.random.Random

class ConectarBluetoothService() : Service(),HRProvider.HRClient {

    private var valorAtual:Int = 0
    private var handler = Handler()
    private val mBinder = ConectarBluetoothBinder()
    private var hrProvider: HRProvider? = null
    private var hrReader: Timer? = null
    private var btProviderName: String? = null
    var bluetoothDevice :BluetoothDevice? = null
    private lateinit var activity: Activity
    var conectado = false
    var foiDesconectado = false

    override fun onOpenResult(ok: Boolean) {
    }

    override fun onScanResult(device: HRDeviceRef?) {
    }

    override fun onConnectResult(connectOK: Boolean) {Log.i("teste",hrProvider?.getProviderName() + "::onConnectResult(" + connectOK + ")")
        if (connectOK) {
            save()
            startTimer()
            conectado = true
        } else {
            conectado = false
        }
    }

    override fun onDisconnectResult(disconnectOK: Boolean) {
        Log.i("erro", "desconectou")
        conectado = false
        foiDesconectado = true
    }

    override fun onCloseResult(closeOK: Boolean) {

    }

    override fun log(src: HRProvider?, msg: String?) {

    }


    private fun save() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val ed = prefs.edit()
            .putString("pref_bt_name", bluetoothDevice?.name)
            .putString("pref_bt_address", bluetoothDevice?.address)
            .putString("pref_bt_provider", hrProvider?.getProviderName())
            .putBoolean("conectado", true)
        ed.apply()
    }


    override fun onBind(intent: Intent): IBinder? {
        load()
        return mBinder
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun iniciarHandler(bluetoothDevice: BluetoothDevice, activity: Activity){
        this.activity = activity
        connect(bluetoothDevice)
    }

    fun connect(bluetoothDevice: BluetoothDevice) {
        stopTimer()
        if (hrProvider == null || bluetoothDevice.name == null || bluetoothDevice.address == null) {
            return
        }
        if (hrProvider?.isConnecting()!! || hrProvider?.isConnected()!!) {
            Log.i("Teste",hrProvider?.getProviderName() + ".disconnect()")
            hrProvider?.disconnect()
            hrProvider?.close()
            return
        }

        var name: String? =  bluetoothDevice.name
        if (name == null || name.length == 0) {
            name =  bluetoothDevice.address
        }
        Log.i("TEste",hrProvider?.getProviderName() + ".connect(" + name + ")")
        hrProvider?.connect(HRDeviceRef.create(btProviderName, bluetoothDevice.name, bluetoothDevice.address))
        this.bluetoothDevice = bluetoothDevice
    }

    fun disconnect(){
        hrProvider?.disconnect()
        hrProvider?.close()
        foiDesconectado = true
    }

    fun load() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        btProviderName = prefs.getString("pref_bt_provider", null)


        hrProvider = AndroidBLEHRProvider(this)
        open()

    }

    private fun open() {
        if (hrProvider != null && !(hrProvider?.isEnabled())!!) {
            if (hrProvider?.startEnableIntent(this.activity, 0)!!) {
                return
            }
            hrProvider = null
        }
        if (hrProvider != null) {
            hrProvider?.open(handler, this)
        } else {
//            updateView()
        }
    }


    private fun startTimer() {
        hrReader = Timer()
        hrReader?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post(Runnable { readHR() })
            }
        }, 0, 500)
    }

    private fun readHR() {
        if (hrProvider != null) {
            val data = hrProvider?.getHRData()
            if (data != null) {
                var hrValue: Long = 0
                if (data.hasHeartRate)
                    hrValue = data.hrValue

                valorAtual = hrValue.toInt()
            }
        }
    }


    private fun stopTimer() {
        if (hrReader == null)
            return

        hrReader?.cancel()
        hrReader?.purge()
        hrReader = null
    }

    fun getValorAtual():Int {
        return valorAtual
    }

    inner class ConectarBluetoothBinder: Binder() {
        val service: ConectarBluetoothService
            get() = this@ConectarBluetoothService
    }
}