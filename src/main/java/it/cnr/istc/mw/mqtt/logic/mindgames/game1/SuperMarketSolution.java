/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import it.cnr.istc.mw.mqtt.db.DBManager;
import it.cnr.istc.mw.mqtt.exceptions.InvalidProductException;
import it.cnr.istc.mw.mqtt.exceptions.InvalidRepartsExceptions;
import it.cnr.istc.mw.mqtt.exceptions.MindGameException;
import it.cnr.istc.mw.mqtt.exceptions.ProductDuplicateException;
import it.cnr.istc.mw.mqtt.exceptions.TooFewRepartsExceptions;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.Solution;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca
 */
public class SuperMarketSolution extends Solution {

    private List<Product> products = null;
    private static Department selectedDepartment = null;
    private static List<Product> solutionProduct = null;
    private static Map<Department, List<Product>> productMap = new HashMap<>();

    public SuperMarketSolution(List<Product> products) throws InvalidProductException {
        this.products = products;
        if (products == null || products.isEmpty()) {
            return;
        }
        this.selectedDepartment = generateDepartment();
        for (Product product : products) {
            if (product == null || product.getDepartment() == null) {
                continue;
            }
            if (!productMap.containsKey(product.getDepartment())) {
                productMap.put(product.getDepartment(), new LinkedList<>());

            }
            productMap.get(product.getDepartment()).add(product);
        }
        this.solutionProduct = productMap.get(selectedDepartment);

    }

    private Department generateDepartment() {

        Collections.shuffle(products);

        return products.get(0).getDepartment();

    }

    public List<Product> getProducts() {
        return products;
    }

    public Department getSelectedDepartment() {
        return selectedDepartment;
    }

    public List<Product> getSolutionProduct() {
        return solutionProduct;
    }

    /**
     * questo metodo controlla che ci siano almeno 3 reparti differenti, che la
     * lista che viene passata nel costruttore non sia vuota e che la lista non
     * sia nulla, viceversa lanci l'eccezione tooFewRepartsException.
     *
     * @throws TooFewRepartsExceptions l'eccezione viene lanciata quando la
     * lista passata nel costruttore è vuota oppure nulla ed il set ha
     * all'interno meno di 3 reparti.
     * @throws it.cnr.istc.mw.mqtt.exceptions.InvalidRepartsExceptions ritorna
     * questa eccezione quando il singolo reparto è nullo o invalido.
     * @throws it.cnr.istc.mw.mqtt.exceptions.InvalidProductException ritorna
     * questa eccezione quando un prodotto della lista è nullo oppure invalido.
     *
     */
    public final void checkReparts() throws TooFewRepartsExceptions, InvalidRepartsExceptions, InvalidProductException {
        Set<Department> set = new HashSet<>();
        
        if(products == null || products.isEmpty()){
            throw new InvalidProductException();
        }
        for (Product product : products) {
            if(product.getDepartment()== null){
               throw new InvalidRepartsExceptions();
            }
            set.add(product.getDepartment());
        }

        if (set.size() < 3) {

            throw new TooFewRepartsExceptions();

        }
       

    }

    /**
     * Controlla la lista di prodotti e al primo duplicato lancia un
     * eccezione,se nella lista è presente un elemento nullo, oppure la lista e
     * nulla, il metodo ritorna una MindGameException.
     *
     * @throws ProductDuplicateException,MindGameException
     */
    public final void checkDuplicate() throws ProductDuplicateException, MindGameException {
        Product product;
        if (this.products == null) {
            throw new MindGameException() {
                @Override
                public String errorMessage() {
                    return "NULL PRODUCT";
                }
            };
        }
        
        

        for (int i = 0; i < products.size(); i++) {
            for (int j = i + 1; j < products.size(); j++) {
                if (products.get(i) == null || products.get(j) == null) {
                    throw new MindGameException() {
                        @Override
                        public String errorMessage() {
                            return "NULL PRODUCT";
                        }
                    };
                }
                if (products.get(i).equals(products.get(j))) {

                    product = products.get(i);
                    throw new ProductDuplicateException(product);
                }

            }
        }
    }

    /**
     * il metodo isValideProductList controlla che i metodi chekDuplicate e
     * checkReparts non lancino un eccezione e ritorna true. viceversa ritorna
     * false
     *
     * @return retorna true se nessuna eccezione è stata lanciata viceversa
     * ritorna false
     */
    public boolean isValideProductList() {
        try {
            checkDuplicate();
        } catch (ProductDuplicateException product) {
            return false;
        } catch (MindGameException ex) {
            return false;
        }
        try {
            checkReparts();
        } catch (TooFewRepartsExceptions department) {
            return false;
        } catch (InvalidRepartsExceptions ex) {
            return false;
        } catch (InvalidProductException ex) {
            return false;
        }
        return true;
    }

    @Override
    public String toMQTTString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
