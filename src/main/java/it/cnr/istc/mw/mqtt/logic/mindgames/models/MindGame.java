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
 * @author sommovir
 * @param <I>
 * @param <S>
 */
public abstract class MindGame<I extends InitialState, S extends Solution> {
    
    private Long id ;
    private GameType type;
    private  String code;

    public MindGame(GameType type) {
        this.id = -1l;
        this.type = type;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) throws CodeIsInvalidException {
          
           setIfValid(code);
 
    }

    public Long getId() {
        return id;
    }

    public GameType getType() {
        return type;
    }
    
    public void setIfValid(String code) throws CodeIsInvalidException{
        
        if(code == null){
            throw new CodeIsInvalidException("Il codice è nullo ");
        }
        
        if(code.isEmpty()){
            throw new CodeIsInvalidException("Il codice è vuoto ");
        }
        
        if(code.matches("[C]{1}[G]{1}[X,Y,Z]{1}[0-9,A-F]{3}")){
           
           this.code = code; 
           
        }else {
            throw new CodeIsInvalidException("Il codice non segue le istruzione di formattazione ");
        }
         
    }
    
    public abstract I generateInitialState(GameDifficulty difficulty) throws MindGameException;
    
   
    
    public abstract boolean validate(I initialState,S solution);
    
    
    
}
