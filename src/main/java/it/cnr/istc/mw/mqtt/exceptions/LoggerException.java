/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.exceptions;

/**
 * usable Exception class for any generic logger exception
 * @author sommovir
 */
public class LoggerException extends Exception implements GuiPrintableException{

    private String guiMessage;
    
    public LoggerException(String consoleMessage, String guiMessage) {
        super(consoleMessage);
        this.guiMessage = guiMessage;
    }
    
    @Override
    public String getGuiErrorMessage() {
        return this.guiMessage;
    }
}
