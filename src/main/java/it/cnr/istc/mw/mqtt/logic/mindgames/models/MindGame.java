/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

import it.cnr.istc.mw.mqtt.db.Person;
import it.cnr.istc.mw.mqtt.exceptions.CodeIsInvalidException;
import it.cnr.istc.mw.mqtt.exceptions.MindGameException;

/**
 * Bridge Pattern
 *
 * @author sommovir
 * @param <I>
 * @param <S>
 */
;

public abstract class MindGame<I extends InitialState, S extends Solution> {

    private Long id;
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

    /**
     * 
     * @param difficulty la difficoltá del gioco
     * @return un istanza che contiene le informazioni iniziali del gioco
     * @throws MindGameException eccezione che lancia un messaggio di errore
     */
    public abstract I generateInitialState(GameDifficulty difficulty) throws MindGameException;
    
    /**
     * 
     * @param initialState un istanza che contiene le informazioni iniziali del gioco
     * @return se l'istanza è valida
     */
    public abstract boolean validate(I initialState);

}
