/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import it.cnr.istc.mw.mqtt.db.DBManager;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameDifficulty;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.MindGame;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Luca
 */
public class GameSuperMarket extends MindGame<SuperMarketInitialState, SuperMarketSolution>{

    private static GameSuperMarket _instance = null;
    private List<Product> prodotti = null;
    private List<Department> reparti = null;
    
    public static GameSuperMarket getInstance() {
        if (_instance == null) {
            _instance = new GameSuperMarket();
        }
        return _instance;
    }
    
    private GameSuperMarket() {
        super();
        prodotti = DBManager.getInstance().getAllProducts();
        reparti = DBManager.getInstance().getAllDepartments();
    }
    
    
    @Override
    public SuperMarketInitialState generateInitialState(GameDifficulty difficulty) {
        return null;
    }

    @Override
    public boolean validate(SuperMarketInitialState initialState, SuperMarketSolution solution) {
        return true;
    }
    
    //TO INTEGRATE IN generateInitialState ^
    private SuperMarketInitialState sub_generateInitialState(GameDifficulty difficulty) {
        
        int howMany = 5;
       
         Collections.shuffle(prodotti);
         for (int i = 0; i < 10; i++) {
            
        }
         
         //List<Product> gameProduct =
        
        return null;
        
    }
    
    
}
