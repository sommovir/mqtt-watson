/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames;

/**
 *
 * @author Admin
 */
public enum GameDifficulty {
    Facile(5),
    Medio(7),
    Difficile(10);
    
    
    
    private int difficulty;

    private GameDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }
 
}
