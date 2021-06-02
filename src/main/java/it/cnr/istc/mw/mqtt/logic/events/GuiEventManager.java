/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.events;

import it.cnr.istc.mw.mqtt.logic.logger.LoggingTag;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sommovir
 */
public class GuiEventManager {

    private static GuiEventManager _instance = null;
    private List<LoggerEventListener> loggerListeners = new LinkedList<LoggerEventListener>();
    private List<MQTTEventListener> mqttListeners = new LinkedList<MQTTEventListener>();
    
    public static GuiEventManager getInstance() {
        if (_instance == null) {
            _instance = new GuiEventManager();
        }
        return _instance;
    }

    private GuiEventManager() {
        super();
    }

    public void addMQTTEventListener(MQTTEventListener listener){
        this.mqttListeners.add(listener);
    }
    
    public void removeMQTTEventListener(MQTTEventListener listener){
        this.mqttListeners.remove(listener);
    }
    
    public void addLoggerEventListener(LoggerEventListener listener){
        this.loggerListeners.add(listener);
    }
    
    public void removeLoggerEventListener(LoggerEventListener listener){
        this.loggerListeners.remove(listener);
    }
    
    public void dispatchUserConnected(String id){
        for (MQTTEventListener listener : mqttListeners) {
            listener.userConnected(id);
        }
    }
    
    public void dispatchUserDisconnected(String id){
        for (MQTTEventListener listener : mqttListeners) {
            listener.userDisconnected(id);
        }
    }
    
    public void dispatchLoggingModeChange(boolean mode){
        for (LoggerEventListener listener : loggerListeners) {
            listener.loggingModeChanged(mode);
        }
    }
    
    public void dispatchLogStop(){
        for (LoggerEventListener listener : loggerListeners) {
            listener.logStop();
        }
    }
    
    public void dispatchNewTagAdded(LoggingTag tag, boolean manually){
        for (LoggerEventListener listener : loggerListeners) {
            listener.newTagAdded(tag, manually);
        }
    }
    
    public void dispatchPretestEnded(){
        for (LoggerEventListener listener : loggerListeners) {
            listener.pretestEnded();
        }
    }
    
    public void dispatchResume(){
        for (LoggerEventListener listener : loggerListeners) {
            listener.resume();
        }
    }
    
    
    
    
    
    
}
