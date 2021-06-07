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
public class PDFNoManualHere extends Exception implements GuiPrintableException{

    public PDFNoManualHere() {
        super("Impossibile produrre il PDF di autenticazione perché manca il manuale d'uso da fondere.");
    }

    @Override
    public String getGuiErrorMessage() {
        return "Impossibile produrre il PDF di autenticazione perché manca il manuale d'uso da fondere.";
    }
    
}