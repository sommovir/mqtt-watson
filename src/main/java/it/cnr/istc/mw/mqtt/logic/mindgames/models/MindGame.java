/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

/**
 * Bridge Pattern
 * @author sommovir
 */
public final class MindGame {
    
    //Bridge
    private GenericMindGame mindGame;

    public MindGame(GenericMindGame mindGame) {
        this.mindGame = mindGame;
    }
    
    
    
}
