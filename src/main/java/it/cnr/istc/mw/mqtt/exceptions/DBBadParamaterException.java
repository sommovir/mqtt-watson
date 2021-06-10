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
public class DBBadParamaterException extends Exception implements GuiPrintableException{
    
    public enum ErrorType{
        NULL("Parametro nullo"),
        EMPTY("Parametro inserito come stringa vuota");
        
        private String description;
        
        ErrorType(String description){
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
        
        
    }

    public DBBadParamaterException() {
        super("Parametro della query inserito erroneamente");
    }
    
    public DBBadParamaterException(String parameter, ErrorType type) {
        super("Parametro della query ["+parameter+"] inserito erroneamente: "+type.getDescription());
    }

    @Override
    public String getGuiErrorMessage() {
        return "Installazione fallita. Database già installato";
    }
    
}

