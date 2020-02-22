package com.ufla.gustavo.uflatracker.ui.conexaobluetooth.hr;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.UUID;

abstract class BtHRBase implements HRProvider {
    static final UUID HRP_SERVICE = UUID
            .fromString("0000180D-0000-1000-8000-00805f9b34fb");
    static final UUID BATTERY_SERVICE = UUID
            .fromString("0000180f-0000-1000-8000-00805f9b34fb");
    static final UUID FIRMWARE_REVISON_UUID = UUID
            .fromString("00002a26-0000-1000-8000-00805f9b34fb");
    static final UUID DIS_UUID = UUID
            .fromString("0000180a-0000-1000-8000-00805f9b34fb");
    static final UUID HEART_RATE_MEASUREMENT_CHARAC = UUID
            .fromString("00002A37-0000-1000-8000-00805f9b34fb");
    static final UUID BATTERY_LEVEL_CHARAC = UUID
            .fromString("00002A19-0000-1000-8000-00805f9b34fb");
    static final UUID CCC = UUID
            .fromString("00002902-0000-1000-8000-00805f9b34fb");

    HRProvider.HRClient hrClient;
    Handler hrClientHandler;

    void log(final String msg) {
        if (hrClient != null) {
            if(Looper.myLooper() == Looper.getMainLooper()) {
//                hrClient.log(this, msg);
                Log.i("teste", msg);
            } else {
                hrClientHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hrClient != null)
//                            hrClient.log(BtHRBase.this, msg);
                            Log.i("teste", msg);
                    }
                });
            }
        }
        else
            System.err.println(msg);
    }
}
