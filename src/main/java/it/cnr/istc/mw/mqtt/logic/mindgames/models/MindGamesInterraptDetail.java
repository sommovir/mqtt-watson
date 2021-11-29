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
public enum MindGamesInterraptDetail {
    VLNTR("VLNTR"),
    EXCPT("EXCPT"),
    FNLFS("FNLFS"),
    LNTMT("LNTMT"),
    UNKNOWN("UNKNOWN");  
    
    private String cause;

    private MindGamesInterraptDetail(String cause) {
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }
    
    public static MindGamesInterraptDetail of(String cause){
        switch(cause){
            case "VLNTR": return MindGamesInterraptDetail.VLNTR;
            case "EXCPT": return MindGamesInterraptDetail.EXCPT;
            case "FNLFS": return MindGamesInterraptDetail.FNLFS;
            case "LNTMT": return MindGamesInterraptDetail.LNTMT;
            default: return MindGamesInterraptDetail.UNKNOWN;
        }
    }  
    
    
}
