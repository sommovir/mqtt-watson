/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.generals;

/**
 *
 * @author sommovir
 */
public enum DefaultPaths {
    
    LOGS("./logs/"),
    MANUALS("./manuals/"),
    REGISTRATION_PDF("./registration/"),
    LINK_LAST_RELEASE("https://drive.google.com/file/d/1yDiI3x9lpAjrigsaiqCAL-3zrIp_0u1w/view?usp=sharing"),
    QR("./qr/");
    
    
    private DefaultPaths(String path){
        this.path = path;
    }
    
    private String path;

    public String getPath() {
        return path;
    }
    
    
    
    
}
