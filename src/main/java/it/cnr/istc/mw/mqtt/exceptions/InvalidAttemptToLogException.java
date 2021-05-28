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
public class InvalidAttemptToLogException extends Exception implements GuiPrintableException{

    public InvalidAttemptToLogException() {
        super("non puoi più scrivere nel file log una volta stoppato. (per maggiori informazioni consulta help log)");
    }

    public InvalidAttemptToLogException(String message) {
        super(message);
    }

    @Override
    public String getGuiErrorMessage() {
        return "qualcuno del @JDT scriva qualcosa";
    }

    
    
    
    
}
