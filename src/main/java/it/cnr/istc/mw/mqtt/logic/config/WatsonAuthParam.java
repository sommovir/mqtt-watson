/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.config;

/**
 *
 * @author sommovir
 */
public enum WatsonAuthParam {
    API_KEY("apikey"),
    URL("url"),
    VERSION("version"),
    CLIENT_ID("clientid");
    
    private String stringParam;

    private WatsonAuthParam(String stringParam) {
        this.stringParam = stringParam;
    }
    
    
    //lasciare questa signature plz
    public String getParam(){
        return "TODO";
    }
}
