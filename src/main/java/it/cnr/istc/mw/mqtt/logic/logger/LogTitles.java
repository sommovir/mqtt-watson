/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.logger;

import it.cnr.istc.mw.mqtt.logic.generals.ConsoleColors;

/**
 *
 * @author Matteo
 */
public enum LogTitles {
    
    SERVER("Server",ConsoleColors.ANSI_ORANGE),
    LOGGER("Logger",ConsoleColors.ANSI_GREEN),
    GUI("GUI",ConsoleColors.BLUE_BRIGHT),
    DATABASE("DataBase",ConsoleColors.IVORY);

    private LogTitles(String title, String color) {
        this.color = color;
        this.title = title;
    }
    
    private String title,color;
    
    public String getTitle(){
        return(color+"["+title+"] "+ConsoleColors.ANSI_RESET);
    }
    
    public String getDecoloredTitle(){
        return("["+title+"] ");
    }
    
}
