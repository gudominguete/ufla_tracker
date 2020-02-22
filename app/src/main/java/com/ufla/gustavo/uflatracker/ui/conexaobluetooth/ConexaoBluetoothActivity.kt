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
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.ui.conexaobluetooth.hr.AndroidBLEHRProvider
import com.ufla.gustavo.uflatracker.ui.conexaobluetooth.hr.HRDeviceRef
import com.ufla.gustavo.uflatracker.ui.conexaobluetooth.hr.HRManager
import com.ufla.gustavo.uflatracker.ui.conexaobluetooth.hr.HRProvider
import kotlinx.android.synthetic.main.activity_conexao_bluetooth.*
import java.util.*
import kotlin.collections.ArrayList


class ConexaoBluetoothActivity() : AppCompatActivity(), HRProvider.HRClient {
    private var mIsScanning = false

    private var bluetoothDevice :BluetoothDevice? = null
    override fun onOpenResult(ok: Boolean) {
        Log.i("testE", hrProvider?.getProviderName() + "::onOpenResult(" + ok + ")")
//        if (mIsScanning) {
//            mIsScanning = false
//            startScan()
//            return
//        }
//
//        updateView()
    }

    override fun onScanResult(device: HRDeviceRef?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectResult(connectOK: Boolean) {
        Log.i("teste",hrProvider?.getProviderName() + "::onConnectResult(" + connectOK + ")")
        if (connectOK) {
            save()
//            if (hrProvider.getBatteryLevel() > 0) {
//                tvBatteryLevel.setVisibility(View.VISIBLE)
//                tvBatteryLevel.setText(
//                    String.format(
//                        Locale.getDefault(), "%s: %d%%",
//                        resources.getText(R.string.Battery_level), hrProvider.getBatteryLevel()
//                    )
//                )
//            }
            startTimer()
        }
    }

    private fun save() {
        val res = resources
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val ed = prefs.edit()
            .putString("pref_bt_name", bluetoothDevice?.name)
            .putString("pref_bt_address", bluetoothDevice?.address)
            .putString("pref_bt_provider", hrProvider?.getProviderName())
        ed.apply()
    }


    override fun onDisconnectResult(disconnectOK: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCloseResult(closeOK: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun log(src: HRProvider?, msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var mBTDevices: ArrayList<BluetoothDevice> = ArrayList()
    var mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var btProviderName: String? = null
    private val handler = Handler()

    private var lastTimestamp: Long = 0
    private var timerStartTime: Long = 0

    private var hrProvider: HRProvider? = null
    private var hrReader: Timer? = null
    var mBluetoothConnection: BluetoothConnectionService? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ufla.gustavo.uflatracker.R.layout.activity_conexao_bluetooth)

        load()
        mBluetoothConnection =BluetoothConnectionService(this)
        if (!mBluetoothAdapter.isEnabled()) {
            val eintent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(eintent, 100)
        }

        discoverBluetooth()
    }

    private fun load() {
        val res = resources
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        btProviderName = prefs.getString("pref_bt_provider", null)

        if (btProviderName != null) {
            Log.i("teste", "HRManager.get($btProviderName)")
            hrProvider = HRManager.getHRProvider(this, btProviderName)
        }else {
            hrProvider = AndroidBLEHRProvider(this)
            open()
        }
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
        var adapter = DispositivosAdapter(this, mBTDevices) {
            Log.i("teste", it.name )
            bluetoothDevice = it
            connect(it)
        }
        recycle_dispositivos.setAdapter(adapter);
    }

    private fun open() {
        if (hrProvider != null && !(hrProvider?.isEnabled())!!) {
            if (hrProvider?.startEnableIntent(this, 0)!!) {
                return
            }
            hrProvider = null
        }
        if (hrProvider != null) {
            Log.i("Teste", hrProvider?.getProviderName() + ".open(this)")
            hrProvider?.open(handler, this)
        } else {
//            updateView()
        }
    }


    private fun connect(bluetoothDevice: BluetoothDevice) {
        stopTimer()
        if (hrProvider == null || bluetoothDevice.name == null || bluetoothDevice.address == null) {
//            updateView()
            return
        }
        if (hrProvider?.isConnecting()!! || hrProvider?.isConnected()!!) {
            Log.i("Teste",hrProvider?.getProviderName() + ".disconnect()")
            hrProvider?.disconnect()
            hrProvider?.close()
//            updateView()
            return
        }

//        tvBTName.setText(getName())
//        tvHR.setText("?")
        var name: String? =  bluetoothDevice.name
        if (name == null || name.length == 0) {
            name =  bluetoothDevice.address
        }
        Log.i("TEste",hrProvider?.getProviderName() + ".connect(" + name + ")")
        hrProvider?.connect(HRDeviceRef.create(btProviderName, bluetoothDevice.name, bluetoothDevice.address))
//        updateView()
    }

    private fun stopTimer() {
        if (hrReader == null)
            return

        hrReader?.cancel()
        hrReader?.purge()
        hrReader = null
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
                val age = data!!.timestamp
                var hrValue: Long = 0
                if (data!!.hasHeartRate)
                    hrValue = data!!.hrValue

                numero_batimento.setText(String.format(Locale.getDefault(), "%d", hrValue))

//                if (age != lastTimestamp) {
//                    if (graphViewSeries == null) {
//                        timerStartTime = System.currentTimeMillis()
//                        val empty = arrayOf<DataPoint>()
//                        graphViewSeries = LineGraphSeries(empty)
//                        graphView.addSeries(graphViewSeries)
//                        graphView.getGridLabelRenderer()
//                            .setLabelFormatter(object : DefaultLabelFormatter() {
//                                override fun formatLabel(value: Double, isValueX: Boolean): String {
//                                    return if (isValueX) {
//                                        formatter.formatDistance(
//                                            Formatter.Format.TXT_SHORT,
//                                            value.toLong()
//                                        )
//                                    } else {
//                                        formatter.formatHeartRate(Formatter.Format.TXT_SHORT, value)
//                                    }
//                                }
//                            })
//                    }
//
//                    graphViewListData.add(
//                        DataPoint(
//                            ((age - timerStartTime) / 1000).toDouble(),
//                            hrValue.toDouble()
//                        )
//                    )
//                    while (graphViewListData.size > GRAPH_HISTORY_SECONDS) {
//                        graphViewListData.removeAt(0)
//                    }
//                    graphViewArrayData = graphViewListData.toTypedArray()
//                    graphViewSeries.resetData(graphViewArrayData)
//                    lastTimestamp = age
//                }
            }
        }
    }
}
