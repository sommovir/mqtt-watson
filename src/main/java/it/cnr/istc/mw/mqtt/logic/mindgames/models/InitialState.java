/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

/**
 *
 * @author Luca
 */
public abstract class InitialState<S extends Solution> {
    
    private GameType gameType;
    protected S solution;

    public InitialState(GameType gameType) {
        this.gameType = gameType;
    }

    public GameType getGameType() {
        return gameType;
    }

    public  abstract S getSolution();
    
    /**
     * metodo che restituisce la stringa da sintetizzare vocalmente
     * @return 
     */
    public abstract String getWatsonText();
    
    /**
     * restituisce la stringa che descrive testualmente il gioco
     * @return 
     */
    public abstract String getDescriptionText();
    
    /**
     * restituisce la stringa che descrive vocalmente il gico
     * @return 
     */
    public abstract String getDescriptionVocal();
    
    
    
    
}
