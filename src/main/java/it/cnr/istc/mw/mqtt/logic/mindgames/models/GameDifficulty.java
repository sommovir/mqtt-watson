/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

/**
 * TO ADAPT
 * @author Admin
 */
public enum GameDifficulty {
    
    Facile("Facile"),
    Medio("Medio"),
    Difficile("Difficie");
    
    
    
    private String difficulty;

    private GameDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDifficulty() {
        return difficulty;
    }
 
}

