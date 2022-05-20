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
 * @param <G>
 */
public class GameInstance<G extends MindGame> {

    private InitialState initialState;
    private Person person;
    private G mindGame;
    private GameResult gameResult;      //- esito del gioco

    public GameInstance() {
    }

    public GameInstance(InitialState initialState, Person person, G mindGame, GameResult gameResult) {
        this.initialState = initialState;
        this.person = person;
        this.mindGame = mindGame;
        this.gameResult = gameResult;
    }

    public InitialState getInitialState() {
        return initialState;
    }

    public void setInitialState(InitialState initialState) {
        this.initialState = initialState;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public G getMindGame() {
        return mindGame;
    }

    public void setMindGame(G mindGame) {
        this.mindGame = mindGame;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }





    
}
