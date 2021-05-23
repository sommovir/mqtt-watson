/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author francesco
 */
public enum LoggingTag {
    
    ELAPSED_TIME("ELAPSED TIME"),
    SYSTEM_TURNS("SYSTEM TURNS"),
    TOTAL_SYSTEM_TURNS("TOTAL SYSTEM TURNS"),
    USER_TURNS("USER TURNS"),
    TOTAL_USER_TURNS("TOTAL USER TURNS"),
    TOTAL_TURNS("TOTAL TURNS"),
    TIMEOUT("TIMEOUT"),
    REJECTS("REJECTS"),
    REPROMPT("REPROMPT"),
    NOANSWER("NOANSWER"),
    NO_USER_ANSWER("NO USER ANSWER"),
    CANCEL("CANCEL"),
    NOTE("NOTE"),
    BARGEINS("BARGEINS"),
    USER_CONNECTED("USER CONNECTED"),
    USER_DISCONNECTED("USER DISCONNECTED"),
    FACE("FACE"),
    TABLE("TABLE"),
    VIDEO("VIDEO"),
    IMG("IMG"),
    LINK("LINK"),
    CHANGE_USERNAME("CHANGE USERNAME"), //when the user change his username
    REC_BUTTON_PRESSED("REC BUTTON PRESSED BY USER"),
    WRONG_ANSWER("WRONG ANSWER"),
    NEGATIVE_ANS("NEGATIVE ANSWER"),
    BYPASS("BYPASS"),
    POSITIVE_ANS("POSITIVE ANSWER"),
    REPEAT("REPEAT"),
    END_PRETEST("END PRETEST"),
    SPEAK("SPEAK"), //no need to be "helped"
    LOW_DELTA("LOW DELTA"),
    WATSON_HARD_RESET("WATSON HARD RESET"),
    CONFIDENCE_INTENTS ("CONFIDENCE INTENTS"),
    CONFIDENCE_ENTITIES ("CONFIDENCE ENTITIES"),
    PRECISION_INTENTS ("PRECISION INTENTS"),
    PRECISION_FAILED_INTENTS ("PRECISION FAILED INTENTS"),
    PRECISION_ENTITIES ("PRECISION ENTITIES"),
    ALPHA("ALPHA"),
    BETA("BETA");

    private LoggingTag(String tag) {
        this.tag = tag;
    }
    
    private String tag;
    
    /**
     * Ritorna il valore del tag circondato da minore e maggiore
     * @return il tag con maggiore e minore
     */
    public String getTag(){
        return "<" + tag + ">";
    }
    
    /**
     * Ritorna il valore del tag semplice
     * @return il tag
     */
    public String getUndecoratedTag(){
        return tag;
    }
    
    public static void printAlphabeticOrder(){
        List<LoggingTag> asList = Arrays.asList(LoggingTag.values());
        List<String> stringhini = new ArrayList<>(asList.size());
        for (LoggingTag asli : asList) {
            stringhini.add(asli.getTag());
        }
        Collections.sort(stringhini);
        for (String tag : stringhini) {
            System.out.println(tag);
        }
    }
}
