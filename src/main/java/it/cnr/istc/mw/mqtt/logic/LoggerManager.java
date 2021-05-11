/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic;

/**
 * init logging 6
 * @author sommovir
 */
public class LoggerManager {

    private static LoggerManager _instance = null;
    private boolean logActive = false;

    public static LoggerManager getInstance() {
        if (_instance == null) {
            _instance = new LoggerManager();

        }
        return _instance;
    }

    private LoggerManager() {
        super();
    }
    
    public void newLog(String logfile){
        
    }
    
    public void log(String textToLog){
        if(!logActive){
            return;
        }
        
    }

    public void setLogActive(boolean logActive) {
        this.logActive = logActive;
    }

    public boolean isLogActive() {
        return logActive;
    }

}
