/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.exceptions;

/**
 *
 * @author Federico
 */
public class TooFewRepartsExceptions extends MindGameException{

    @Override
    public String errorMessage() {
       return "La lista contiene meno di 3 reparti";
    }

    

    
    
    
}
