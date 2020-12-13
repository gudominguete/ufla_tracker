package com.flex.flextracker.ui.conexaobluetooth.hr;

import java.util.Arrays;

public class HRData {

    public boolean hasHeartRate = false;
    public long hrValue = -1;
    private boolean timeStampIsFromDevice = false;
    public long timestamp = -1;
    private boolean hasRrIntervals = false;
    private long[] rrIntervals = null;

    @Override
    public String toString() {
        return "HRData{" + ", hrValue=" + (hasHeartRate ? hrValue : "<no_heart_rate_data>") +
                '\n' +
                ", timeStampIsFromDevice=" + timeStampIsFromDevice +
                '\n' +
                ", timestamp=" + timestamp +
                '\n' +
                ", rrIntervals=" + (hasRrIntervals ? Arrays.toString(rrIntervals) : "<no_rr_interval_data>") +
                '\n' +
                '}';
    }

    public HRData setHeartRate(long heartRate){
        hasHeartRate = true;
        hrValue = heartRate;
        return this;
    }

    public HRData setTimestampEstimate(long timestamp){
        timeStampIsFromDevice = false;
        this.timestamp = timestamp;
        return this;
    }

    public HRData setTimestamp(long timestampFromDevice){
        timeStampIsFromDevice = true;
        this.timestamp = timestampFromDevice;
        return this;
    }

    public HRData setRrIntervals(long[] rrIntervals){
        hasRrIntervals = true;
        this.rrIntervals = rrIntervals;
        return this;
    }

}
