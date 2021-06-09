/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.exceptions;

/**
 *
 * @author sommovir
 */
public class DBUniqueViolationException extends Exception implements GuiPrintableException{

    public DBUniqueViolationException() {
        super("Unique-constraint violation detected. [parameter=unknown]");
    }
    
    public DBUniqueViolationException(String parameter) {
        super("Unique-constraint violation detected. [parameter="+parameter+"]");
    }

    @Override
    public String getGuiErrorMessage() {
        return "Installazione fallita. Database già installato";
    }
    
}