/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.mindgames;

import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameDifficulty;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sommovir
 */
public class OLD_MindGameEngine {
    
    private static OLD_MindGameEngine _instance = null;
    
    public static OLD_MindGameEngine getInstance() {
        if (_instance == null) {
            _instance = new OLD_MindGameEngine();
        }
        return _instance;
    }
    
    private OLD_MindGameEngine() {
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
    
    /**
     * 
     * @param difficulty
     * @deprecated
     */
    @Deprecated
    public void generateGame1Input(GameDifficulty difficulty){
        
        List<Product> productList = new ArrayList<Product>();               
        Collections.shuffle(productList);

        switch(difficulty){
            
            case Facile: 
                 Product productEasy = productList.get(5);
                break;
                
            case Medio: 
                Product productMedium = productList.get(5);
                break;
                
            case Difficile:
                 Product productHard = productList.get(5);
                break;
              
        }
        
    }
}
