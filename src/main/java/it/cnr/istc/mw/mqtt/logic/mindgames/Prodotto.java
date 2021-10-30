/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames;

import java.util.logging.Logger;

/**
 *
 * @author Luca
 */
public class Prodotto extends Reparto{
    
    private long id;
    private String name;
    private Reparto reparto = new Reparto(id, name);

    public Prodotto() {
    }

    public Prodotto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Reparto getFrist() {
        return reparto;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFrist(Reparto frist) {
        this.reparto = frist;
    }
    
    
    

    
    
    
    
    
    
    
}
