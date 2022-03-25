package it.cnr.istc.mw.mqtt.logic.generals;

/**
 *
 * @author Luca
 */
public enum WatsonDeviceType {
    MOBILE("DVC-MBL-OIE"),
    ROBOT("DVC-RBT-OOE"),
    TV("DVC-TLV-EEI"),
    UNKNOWN("DVC-UNK-NOW");
    
    private String deviceValue;

    private WatsonDeviceType(String deviceValue){
        this.deviceValue = deviceValue;
    }

    public String getDeviceValue() {
        return deviceValue;
    }
}
