/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

/**
 *
 * @author loren
 */
public enum GameResult {

    FAIL("L'Utente ha concluso la sessisione miseramente"),
    SUCCESS("L'utente ha concluso la sessione vincendo la partita"),
    TERMINATED("L'utente ha smesso di giocare anzitempo"),
    NOT_FINISHED("L'utente non ha ancora finito la sessione");

    private GameResult(String gameResult) {
        this.gameResult = gameResult;
    }

    private String gameResult;

    public String getGameResult() {
        return gameResult;
    }

}
