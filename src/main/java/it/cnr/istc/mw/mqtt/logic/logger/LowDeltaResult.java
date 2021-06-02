/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.logger;

/**
 *
 * @author sommovir
 */
public enum LowDeltaResult {
    
    HIGH_DELTA("Non c'è indecisione nel calcolo degli intenti",false),
    LOW_MAX("L'intento principale è sotto la soglia prestabilita", true),
    ZERO_PRECISION("Non è stato trovato nessun intent per l'input dato",true), //caso particolare che dovrebbe essere assorbito dal bypass
    INDECISION("C'è poca differenza tra le confidence dei primi due intent trovati",true),
    WATSON_SUGGESTION("Watson ha stabilito autonomamente che c'è poca precisione e fornisce una lista di suggerimenti",true);

    private LowDeltaResult(String cause, boolean result) {
        this.cause = cause;
        this.result = result;
    }
    
    private String cause;
    private boolean result;

    public String getCause() {
        return cause;
    }

    public boolean isLowDelta() {
        return result;
    }
    
    
    
}
