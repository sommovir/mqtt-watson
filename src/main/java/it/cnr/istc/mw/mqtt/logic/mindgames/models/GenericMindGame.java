/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

import it.cnr.istc.mw.mqtt.logic.mindgames.GameDifficulty;
import it.cnr.istc.mw.mqtt.logic.mindgames.GameType;

/**
 *
 * @author sommovir
 */
public interface GenericMindGame {
   
        public GameType getGameType();
        
        public String getInitialState();
        
        public boolean checkSolution(String solution);
        
        public GameDifficulty getInitialDifficulty();
        
        
        
        
        
    
    
}
