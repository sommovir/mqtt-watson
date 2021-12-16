/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.generals;

/**
 *
 * @author Luca
 */
public enum DeviceType {
    MOBILE("Mobile", "DVC-MBL-OIE"),
    ROBOT("Robot", "DVC-RBT-OOE"),
    TV("TV", "DVC-TLV-EEI"),
    UNKNOWN("unknown", "DVC-UNK-NOW");

    String maxVersion;
    private String deviceType, deviceID;

    private DeviceType(String deviceType, String deviceID) {
        this.deviceType = deviceType;
        this.deviceID = deviceID;
    }

    public String getDeviceType() {
        return deviceType;
    }
    
    public static DeviceType of(String device){
        switch(device){
            case "Mobile": return DeviceType.MOBILE;
            case "Robot": return DeviceType.ROBOT;
            case "TV": return DeviceType.TV;
            default: return DeviceType.UNKNOWN;
        }
    }

}
