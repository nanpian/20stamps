package com.stamp20.app.data;

public class Device {

    public static final String DeviceIdCustomStamp = "Custom Stamp";
    public static final String DeviceIdFlatPhotoCard = "Greeting Card";

    // stamps是个奇葩, 选了rate之后,要利用DeviceStamp重新设定Price, TaxableAmount和Name

    static public String getDeviceName(String deviceId) {
        if (deviceId.equals(DeviceIdFlatPhotoCard)) {
            return "Greeting Card";
        } else if (deviceId.equals(DeviceIdCustomStamp)) {
            return "Custom Postage";
        }
        return "Unknown";
    }

    static public int getPrice(String deviceId) {
        if (deviceId.equals(DeviceIdFlatPhotoCard)) {
            return 5500;
        } else if (deviceId.equals(DeviceIdCustomStamp)) {
            return 2450;
        }
        return 0;
    }

    static public int getTaxableAmount(String deviceId) {
        return getPrice(deviceId);
    }
}
