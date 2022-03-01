/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import it.cnr.istc.mw.mqtt.db.DBManager;
import it.cnr.istc.mw.mqtt.exceptions.InvalidRepartsExceptions;
import it.cnr.istc.mw.mqtt.exceptions.ProductDuplicateException;
import it.cnr.istc.mw.mqtt.exceptions.TooFewRepartsExceptions;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameDifficulty;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameType;
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

    private static List<Product> prodotti = null;
    private static List<Department> reparti = null;
    private static Map<Department, List<Product>> productMap = new HashMap<>();
    

    static {
        prodotti = DBManager.getInstance().getAllProducts();
        reparti = DBManager.getInstance().getAllDepartments();
        for (Product product : prodotti) {
            if (productMap.containsKey(product.getDepartment())) {
                productMap.put(product.getDepartment(), new LinkedList<>());

            }
            productMap.get(product.getDepartment()).add(product);
        }
    }

    public GameSuperMarket() {
        super(GameType.LISTA_SPESA);

    }

    @Override
    public SuperMarketInitialState generateInitialState(GameDifficulty difficulty) throws ProductDuplicateException, TooFewRepartsExceptions, InvalidRepartsExceptions {
        int howManyProducts = 0;

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

        List<Product> gameProduct = generateProducts(howManyProducts);

        
        SuperMarketInitialState initialState = new SuperMarketInitialState(gameProduct);
        initialState.getSolution().checkDuplicate();
        initialState.getSolution().checkReparts();
        
        
        
        return initialState;
    }


    public List<Product> generateProducts(int howmany) {

        List<Product> copiaProdotti = new LinkedList<Product>();
        Collections.copy(copiaProdotti, prodotti);

        Collections.shuffle(this.reparti);
        List<Product> gameProducts = new LinkedList<Product>();
        int h = ThreadLocalRandom.current().nextInt(3, howmany + 1);
        for (int i = 0; i < h; i++) {

            Department dep = this.reparti.get(i);
            List<Product> prod = this.productMap.get(dep);
            Collections.shuffle(prod);
            gameProducts.add(prod.get(0));
            copiaProdotti.remove(prod.get(0));

        }
        int last = howmany - h;

        for (int i = 0; i < last; i++) {
            gameProducts.add(copiaProdotti.get(i));

        }
        Collections.shuffle(gameProducts);
        return gameProducts;
    }
    
    @Override
    public boolean validate(SuperMarketInitialState initialState) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
