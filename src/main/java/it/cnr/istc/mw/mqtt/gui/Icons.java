/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.gui;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author sommovir
 */
public enum Icons {
    
    GREEN_DOT(new ImageIcon(Icons.class.getResource("/icons/green16.png"))),
    RED_DOT(new ImageIcon(Icons.class.getResource("/icons/red16.png"))),
    YELLOW_DOT(new ImageIcon(Icons.class.getResource("/icons/yellow16.png"))),
    LOG_32(new ImageIcon(Icons.class.getResource("/icons/logf.png")));
    
    
    private Icons(ImageIcon icon){
        this.icon = icon;
    }
    
    private ImageIcon icon;

    public ImageIcon getIcon() {
        return icon;
    }
    
    
    
}
