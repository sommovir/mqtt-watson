/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.events;

import it.cnr.istc.mw.mqtt.logic.logger.LoggingTag;

/**
 *
 * @author sommovir
 */
public interface LoggerEventListener {
    
    /**
     * notify whenever the log [on/off] command is invoked. 
     * @param mode 
     */
    public void loggingModeChanged(boolean mode);
    
    /**
     * notify whenever a logging tag is being inserted
     * @param tag 
     * The new tag
     * @param manually 
     * true if the tag is being inserted manyally by the Logger Manager Administrator
     */
    public void newTagAdded(LoggingTag tag, boolean manually);
    
    /**
     * notify when the pre-test phase is declared ended. 
     */
    public void pretestEnded();
    
    /**
     * notify when the current logging operation is concluded.
     */
    public void logStop();
    
    
    /**
     * notify when the Logger Admin, invoke the resume. 
     */
    public void resume();
    
}
