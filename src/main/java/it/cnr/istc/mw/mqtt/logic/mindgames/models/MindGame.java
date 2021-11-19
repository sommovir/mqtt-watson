/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

import it.cnr.istc.mw.mqtt.db.Person;
import it.cnr.istc.mw.mqtt.exceptions.MindGameException;

/**
 * Bridge Pattern
 * @author sommovir
 */
public abstract class MindGame<I extends InitialState, S extends Solution> {
    
    private Long id ;
    private GameType type;

    public MindGame(GameType type) {
        this.id = -1l;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public GameType getType() {
        return type;
    }
    
    public abstract I generateInitialState(GameDifficulty difficulty) throws MindGameException;
    
   
    
    public abstract boolean validate(I initialState,S solution);
    
    
    
}
