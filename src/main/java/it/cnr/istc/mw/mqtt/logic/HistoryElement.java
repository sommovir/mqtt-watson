/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic;

import java.util.Date;

/**
 *
 * @author sommovir
 */
public class HistoryElement {
    
    private String input;
    private String output;
    private Date timestamp;
    private String face;

    public HistoryElement() {
    }

    public HistoryElement(String input, String output, Date timestamp) {
        this.input = input;
        this.output = output;
        this.timestamp = timestamp;
    }
    
    public HistoryElement(String input, String output, Date timestamp, String face) {
        this.input = input;
        this.output = output;
        this.timestamp = timestamp;
        this.face = face;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }
    
    
    
    
}
