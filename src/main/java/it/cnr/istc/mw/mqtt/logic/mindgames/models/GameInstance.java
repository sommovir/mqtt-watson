/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

import it.cnr.istc.mw.mqtt.db.Person;
import java.util.Date;

/**
 *
 * @author Luca
 */
public class GameInstance<G extends MindGame> {

    private InitialState initialState;
    private Solution solution ;
    private Date date;
    private Person person;
    private G mindGame;
    private GameResult gameResult;      //- esito del gioco

    public GameInstance(InitialState initialState, Solution solution) {
        this.initialState = initialState;
        this.solution = solution;
    }

    
}
