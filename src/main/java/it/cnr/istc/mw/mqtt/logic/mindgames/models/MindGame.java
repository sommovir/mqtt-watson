/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

import it.cnr.istc.mw.mqtt.db.Person;

/**
 * Bridge Pattern
 * @author sommovir
 */
public abstract class MindGame<I extends InitialState, S extends Solution> {
    
    private Long id ;
    private GameType type;

    public abstract I generateInitialState(GameDifficulty difficulty);
    
    public abstract boolean validate(I initialState,S solution);
    
    
    
}
