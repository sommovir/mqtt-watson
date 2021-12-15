/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.logger;

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
    CONFIDENCE_INTENTS("CONFIDENCE INTENTS"),
    CONFIDENCE_ENTITIES("CONFIDENCE ENTITIES"),
    PRECISION_INTENTS("PRECISION INTENTS"),
    PRECISION_FAILED_INTENTS("PRECISION FAILED INTENTS"),
    PRECISION_ENTITIES("PRECISION ENTITIES"),
    ALPHA("ALPHA"),
    BETA("BETA"),
    WALL_SPEAK("WALL SPEAK"),
    GAMMA("GAMMA"),
    EXTRA_INPUT("EXTRA_INPUT"),
    WRONG_INPUT("WRONG_INPUT"),
    LOGGER_ADMIN("LOGGER ADMIN");

    private LoggingTag(String tag) {
        this.tag = tag;
    }

    private String tag;

    /**
     * Ritorna il valore del tag circondato da minore e maggiore
     *
     * @return il tag con maggiore e minore
     */
    public String getTag() {
        return "<" + tag + ">";
    }

    /**
     * Ritorna il valore del tag semplice
     *
     * @return il tag
     */
    public String getUndecoratedTag() {
        return tag;
    }

    public static LoggingTag of(String tag) {
        switch (tag) {
            case "ELAPSED TIME":
                return LoggingTag.ELAPSED_TIME;
            case "SYSTEM TURNS":
                return LoggingTag.SYSTEM_TURNS;
            case "TOTAL SYSTEM TURNS":
                return LoggingTag.TOTAL_SYSTEM_TURNS;
            case "USER TURNS":
                return LoggingTag.USER_TURNS;
            case "TOTAL USER TURNS":
                return LoggingTag.TOTAL_USER_TURNS;
            case "TOTAL TURNS":
                return LoggingTag.TOTAL_TURNS;
            case "TIMEOUT":
                return LoggingTag.TIMEOUT;
            case "REJECTS":
                return LoggingTag.REJECTS;
            case "REPROMPT":
                return LoggingTag.REPROMPT;
            case "NOANSWER":
                return LoggingTag.NOANSWER;
            case "NO USER ANSWER":
                return LoggingTag.NO_USER_ANSWER;
            case "CANCEL":
                return LoggingTag.CANCEL;
            case "NOTE":
                return LoggingTag.NOTE;
            case "BARGEINS":
                return LoggingTag.BARGEINS;
            case "USER CONNECTED":
                return LoggingTag.USER_CONNECTED;
            case "USER DISCONNECTED":
                return LoggingTag.USER_DISCONNECTED;
            case "FACE":
                return LoggingTag.FACE;
            case "TABLE":
                return LoggingTag.TABLE;
            case "VIDEO":
                return LoggingTag.VIDEO;
            case "IMG":
                return LoggingTag.IMG;
            case "LINK":
                return LoggingTag.LINK;
            case "CHANGE USERNAME":
                return LoggingTag.CHANGE_USERNAME;
            case "REC BUTTON PRESSED BY USER":
                return LoggingTag.REC_BUTTON_PRESSED;
            case "WRONG ANSWER":
                return LoggingTag.WRONG_ANSWER;
            case "NEGATIVE ANSWER":
                return LoggingTag.NEGATIVE_ANS;
            case "BYPASS":
                return LoggingTag.BYPASS;
            case "POSITIVE ANSWER":
                return LoggingTag.POSITIVE_ANS;
            case "REPEAT":
                return LoggingTag.REPEAT;
            case "END PRETEST":
                return LoggingTag.END_PRETEST;
            case "SPEAK":
                return LoggingTag.SPEAK;
            case "LOW DELTA":
                return LoggingTag.LOW_DELTA;
            case "WATSON HARD RESET":
                return LoggingTag.WATSON_HARD_RESET;
            case "CONFIDENCE INTENTS":
                return LoggingTag.CONFIDENCE_INTENTS;
            case "CONFIDENCE ENTITIES":
                return LoggingTag.CONFIDENCE_ENTITIES;
            case "PRECISION INTENTS":
                return LoggingTag.PRECISION_INTENTS;
            case "PRECISION FAILED INTENTS":
                return LoggingTag.PRECISION_FAILED_INTENTS;
            case "PRECISION ENTITIES":
                return LoggingTag.PRECISION_ENTITIES;
            case "ALPHA":
                return LoggingTag.ALPHA;
            case "BETA":
                return LoggingTag.BETA;
            case "WALL SPEAK":
                return LoggingTag.WALL_SPEAK;
            case "GAMMA":
                return LoggingTag.GAMMA;
            case "EXTRA_INPUT":
                return LoggingTag.EXTRA_INPUT;
            case "WRONG_INPUT":
                return LoggingTag.WRONG_INPUT;
            case "LOGGER ADMIN":
                return LoggingTag.LOGGER_ADMIN;
        }
        return null;
    }

    public static void printAlphabeticOrder() {
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
