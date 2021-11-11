/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameType;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.InitialState;
import java.util.List;

/**
 *
 * @author Luca
 */
public class SuperMarketInitialState extends InitialState<SuperMarketSolution> {

    private List<Product> products = null;

    public SuperMarketInitialState(List<Product> products) {
        super(GameType.UNO);
        this.products = products;
    }

    @Override
    public String getWatsonText() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDescriptionText() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toMQTTString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SuperMarketSolution getSolution() {
         //TASK #103
         return null;
    }

}
