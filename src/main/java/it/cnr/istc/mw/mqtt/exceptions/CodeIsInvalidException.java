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
public class CodeIsInvalidException extends MindGameException{
    private String details;
    
    
    
    public CodeIsInvalidException(String details) {
        this.details = details;
    }

    public CodeIsInvalidException() {
        
    }

    
    
    @Override
    public String errorMessage() {
        return details;
        
    }
    
}
