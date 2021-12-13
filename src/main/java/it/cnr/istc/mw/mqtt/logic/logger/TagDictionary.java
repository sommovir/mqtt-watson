/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.logger;


import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sommovir
 */
public class TagDictionary {
    private Map<LoggingTag,Integer> dictionary = new HashMap<>();
    private Map<LoggingTag,Integer> rowMap = new HashMap<>();
    private String columnOfLog; //la colonna dove verranno salvati i tag in esame
                

    public TagDictionary(String columnOfLog) {
        this.columnOfLog = columnOfLog; 
        LoggingTag[] loggingTagValues = LoggingTag.values();
        for (LoggingTag loggingTagValue : loggingTagValues) {
            dictionary.put(loggingTagValue, 0);
        }
    }
    
    
}
