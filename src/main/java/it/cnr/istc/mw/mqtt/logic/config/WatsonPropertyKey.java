/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.config;

/**
 * Fare una BUILDER
 *
 * @author sommovir
 */
public class WatsonPropertyKey {

    private String scope; //obbligatorio
    private String account; //obbligatorio
    private WatsonAuthParam param; //obbligatorio
    private boolean defaultParam = false; //se true forza il retrieve di questo param dal file hard-coded
    private boolean verbose = false; //se true avvisa in console se il parametro manca nel file
    private String freeParam = null; //se diverso da null sovrascrive param
    private String subParam = null; //se diverso da null viene aggiunto in coda al param

    public String key() {
        String sub = this.subParam == null ? "" : "."+this.subParam;
        if (freeParam == null) {
            return scope + "." + account + "." + param.getParam()+sub;
        }else{
            return scope + "." + account + "." + freeParam+sub;
        }
    }

}
