package com.ufla.gustavo.uflatracker.ui.conexaobluetooth.hr;


public class HRDeviceRef {

    private final String provider;
    public final String deviceName;
    public final String deviceAddress;

    private HRDeviceRef(final String provider, final String name, final String address) {
        this.provider = provider;
        this.deviceName = name;
        this.deviceAddress = address;
    }

    public static HRDeviceRef create(String providerName, String deviceName, String deviceAddress) {
        return new HRDeviceRef(providerName, deviceName, deviceAddress);
    }

    public String getProvider() {
        return provider;
    }

    public String getName() {
        if (deviceName != null && !"".contentEquals(deviceName))
            return deviceName;
        return deviceAddress;
    }

    public String getAddress() {
        return deviceAddress;
    }
}
