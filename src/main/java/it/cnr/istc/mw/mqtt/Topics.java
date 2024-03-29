/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt;

/**
 *
 * @author Luca
 */
public enum Topics {

    ATTEMPT_LOGIN("attempt_login"),
    ACK_LOGIN("ack_login"),
    CHAT("chat"),
    ATTEMPT_LOGIN_RESPONSE("attempt_login_response"),
    USER_CONNECTED("UserConnected"),
    USER_DISCONNECTED("UserDisconnected"),
    EMERGENCY("emergency"),
    LOG("log"),
    USERNAME("username"),
    RESPONSES("responses"),
    COMMAND("commands"),
    BUTTON_PRESSED("button_pressed"),
    REPEAT("repeat"),
    INFO("info_channel"),
    GETDEVICE("get_device"),
    ROBOT("robot"),
    MINDGAME("mindgames");
    
    private Topics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
    
    
    
    
    private String topic;
}
