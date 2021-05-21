/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.exceptions;

/**
 *
 * @author Alessio
 */
public class LogOffException extends Exception{

    public LogOffException() {
        super("il modulo di logging è disattivato, digita log on per attivare (per maggiori informazioni consulta help log)");
    }

    public LogOffException(String message) {
        super(message);
    }
    
    
    
}
