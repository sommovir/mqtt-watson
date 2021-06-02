/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.gui;

import javax.swing.Icon;

/**
 *
 * @author sommovir
 */
public enum Icons {
    
    GREEN_DOT(new javax.swing.ImageIcon(Icons.class.getResource("/icons/green16.png"))),
    RED_DOT(new javax.swing.ImageIcon(Icons.class.getResource("/icons/red16.png"))),
    YELLOW_DOT(new javax.swing.ImageIcon(Icons.class.getResource("/icons/yellow16.png")));
    
    
    private Icons(Icon icon){
        this.icon = icon;
    }
    
    private Icon icon;

    public Icon getIcon() {
        return icon;
    }
    
    
    
}
