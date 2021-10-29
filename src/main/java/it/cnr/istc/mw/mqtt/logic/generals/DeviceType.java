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
    MOBILE("Mobile"),
    ROBOT("Robot"),
    TV("TV");

    String maxVersion;
    private String deviceType;

    private DeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

}
