/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

/**
 *
 * @author Federico
 */
public enum MindGamesInterruptDetail {
    VLNTR("VLNTR"),
    EXCPT("EXCPT"),
    FNLFS("FNLFS"),
    LNTMT("LNTMT"),
    UNKNOWN("UNKNOWN");  
    
    private String cause;

    private MindGamesInterruptDetail(String cause) {
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }
    
    public static MindGamesInterruptDetail of(String cause){
        switch(cause){
            case "VLNTR": return MindGamesInterruptDetail.VLNTR;
            case "EXCPT": return MindGamesInterruptDetail.EXCPT;
            case "FNLFS": return MindGamesInterruptDetail.FNLFS;
            case "LNTMT": return MindGamesInterruptDetail.LNTMT;
            default: return MindGamesInterruptDetail.UNKNOWN;
        }
    }  
    
    
}
