/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

import it.cnr.istc.mw.mqtt.exceptions.MindGameException;

/**
 *
 * @author Luca
 */
public class InvalidGameInstanceException extends MindGameException {

    public InvalidGameInstanceException() {
    }

    @Override
    public String errorMessage() {
        return "Il gioco contiene dati invalidi";
    }
    
}
