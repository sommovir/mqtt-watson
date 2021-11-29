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
public enum MindGamesInstructions {
    
    START_GAME("START_GAME"), 
    STOP_GAME("STOP_GAME"),
    PAUSE_GAME("PAUSE_GAME") ,
    REPLAY_GAME("REPLAY_GAME"),
    SAVE_GAME("SAVE_GAME"),
    LOAD_GAME("LOAD_GAME"),
    STATS_GAME("STATS_GAME"),
    UNKNOWN("UNKNOWN");
    
    private String instructions;

    private MindGamesInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getInstructions() {
        return instructions;
    }
    
    public static MindGamesInstructions of(String instructions){
        switch(instructions){
            case "START_GAME": return MindGamesInstructions.START_GAME;
            case "STOP_GAME": return MindGamesInstructions.STOP_GAME;
            case "PAUSE_GAME": return MindGamesInstructions.PAUSE_GAME;
            case "REPLAY_GAME": return MindGamesInstructions.REPLAY_GAME;
            case "SAVE_GAME": return MindGamesInstructions.SAVE_GAME;
            case "LOAD_GAME": return MindGamesInstructions.LOAD_GAME;
            case "STATS_GAME": return MindGamesInstructions.STATS_GAME;
            default: return MindGamesInstructions.UNKNOWN;
        }
    }  
}
