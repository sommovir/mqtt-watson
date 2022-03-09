/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.exceptions;

/**
 *
 * @author sommovir
 */
public class InvalidRepartsExceptions extends MindGameException {

    @Override
    public String errorMessage() {
       return "la lista contiene un reparto nullo o invalido";
    }
    
}
