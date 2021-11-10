/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import it.cnr.istc.mw.mqtt.db.DBManager;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameDifficulty;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.MindGame;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Luca
 */
public class GameSuperMarket extends MindGame<SuperMarketInitialState, SuperMarketSolution> {

    private static GameSuperMarket _instance = null;
    private List<Product> prodotti = null;
    private List<Department> reparti = null;
    private Map<Department, List<Product>> productMap = new HashMap<>();
    
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
        for (Product product : prodotti) {
            if(this.productMap.containsKey(product.getDepartment())){
               this.productMap.put(product.getDepartment(), new LinkedList<>());
               
            }
           this.productMap.get(product.getDepartment()).add(product);
           
            
        }
    }
    
    
    @Override
    public SuperMarketInitialState generateInitialState(GameDifficulty difficulty) {
        int howManyProducts=0;

        switch (difficulty) {

            case Facile: {
                howManyProducts = 5;
                break;
            }

            case Medio: {
                howManyProducts = 7;
                break;
            }

            case Difficile: {
                howManyProducts = 10;
                break;
            }

        }
         Collections.shuffle(prodotti);
        
         List<Product> gameProduct = new LinkedList<Product>(prodotti);
        for (int i = 0; i<howManyProducts; i++) {
            
           gameProduct.add(prodotti.get(i));         
        }
        
        if(isValideProductList(gameProduct)){
            
            //in questo caso la selezione casuale è andata a buon fine 
        }else{
            //in questo caso ho meno di 3 reparti diversi ;(
            
            
            
        }
          
        return null;
    }
    /**
     * questo metodo serve a controllare se nella lista di prodotto ci siano almeno 3 reparti diversi
     * @return 
     */
    public boolean isValideProductList(List<Product> gameList){
        
        return false;
        
    }
    
    public List<Product> generateProducts(int howmany){
        
        List<Product> copiaProdotti = new LinkedList<Product>();
        Collections.copy(copiaProdotti, prodotti);
        
        Collections.shuffle(this.reparti);
        List<Product> gameProducts = new LinkedList<Product>();
        int h = ThreadLocalRandom.current().nextInt(3, howmany + 1);
        for (int i = 0; i <h; i++) {
            
            Department dep = this.reparti.get(i);
            List<Product> prod = this.productMap.get(dep);
            Collections.shuffle(prod);
            gameProducts.add(prod.get(0));
            copiaProdotti.remove(prod.get(0));            
            
        }
        int last= howmany-h;
        
        for (int i = 0; i < last; i++) {
            gameProducts.add(copiaProdotti.get(i));
            
        }
        Collections.shuffle(gameProducts);
        return gameProducts;
    }

    @Override
    public boolean validate(SuperMarketInitialState initialState, SuperMarketSolution solution) {
        return true;
    }
   
    
    
}
