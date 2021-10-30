/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.mindgames;

import java.util.List;

/**
 *
 * @author sommovir
 */
public class MindGameEngine {
    
    private static MindGameEngine _instance = null;
    
    public static MindGameEngine getInstance() {
        if (_instance == null) {
            _instance = new MindGameEngine();
        }
        return _instance;
    }
    
    private MindGameEngine() {
        super();
    }
    
    
    /**
     * ritorna true se la lista contiene almeno un duplicato, false altrimenti
     * @param products
     * @return 
     */
    public boolean checkDuplicate(List<Product> products){
        
        return true;
        
    }
}
