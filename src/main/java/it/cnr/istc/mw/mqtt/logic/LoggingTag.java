/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic;

/**
 *
 * @author francesco
 */
public enum LoggingTag {
    
    ELAPSED_TIME("ELAPSED TIME"),
    SYSTEM_TURNS("SYSTEM_TURNS"),
    USER_TURNS("USER TURNS"),
    TOTAL_TURNS("TOTAL TURNS"),
    TIMEOUT("TIMEOUT"),
    REJECTS("REJECTS"),
    REPROMPT("REPROMPT"),
    NOANSWER("NOANSWER"),
    NOINPUT("NOINPUT"),
    CANCEL("CANCEL"),
    BARGEINS("BARGEINS");
    

    private LoggingTag(String tag) {
        this.tag = tag;
    }
    
    private String tag;
    
    public String getTag(){
        return "<" + tag + ">";
    }
}