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
public class InvalidFolderNameException extends Exception implements GuiPrintableException{

    public InvalidFolderNameException() {
        super("The name of the folder can be empty or null");
    }

    @Override
    public String getGuiErrorMessage() {
        return "qualcuno del @JDT scriva qualcosa";
    }
    
    
    
}
