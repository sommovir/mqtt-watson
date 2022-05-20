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
public abstract class MindGameException extends Exception{
    
    public abstract String errorMessage();
            
   
}
