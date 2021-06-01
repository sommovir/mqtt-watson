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
public class DBAlreadyInstalledException extends Exception implements GuiPrintableException{

    public DBAlreadyInstalledException() {
        super("Installazione fallita. Database già installato");
    }

    @Override
    public String getGuiErrorMessage() {
        return "Installazione fallita. Database già installato";
    }
    
}
