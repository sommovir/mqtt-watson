/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameDifficulty;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.MindGame;

/**
 *
 * @author Luca
 */
public class GameSuperMarket extends MindGame<SuperMarketInitialState, SuperMarketSolution>{

    @Override
    public SuperMarketInitialState generateInitialState(GameDifficulty difficulty) {
        return null;
    }

    @Override
    public boolean validate(SuperMarketInitialState initialState, SuperMarketSolution solution) {
        return true;
    }
    
    
    
}
