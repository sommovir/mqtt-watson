/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import it.cnr.istc.mw.mqtt.exceptions.ProductDuplicateException;
import it.cnr.istc.mw.mqtt.exceptions.TooFewRepartsExceptions;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.Solution;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Luca
 */
public class SuperMarketSolution extends Solution{
    
    List<Product> products = null;
    private Department selectedDepartment = null;
    private List<Product> solutionProduct = null;

    public SuperMarketSolution( List<Product> products) {
        this.products = products;
        this.selectedDepartment = selectedDepartment;
        this.solutionProduct = solutionProduct;

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
    
    public List<Product> charge(){
    
        for (Product product : products) {
            if(product.getDepartment().equals(selectedDepartment)){
                solutionProduct.add(product);
            }
        }
        return solutionProduct;
    }
    
    /** 
     *  questo metodo controlla che ci siano almeno 3 reparti differenti,
     *  che la lista che viene passata nel costruttore non sia vuota e che la lista non sia nulla,
     *  viceversa lanci l'eccezione tooFewRepartsException.
     * 
     * @throws TooFewRepartsExceptions
     * l'eccezione viene lanciata quando la lista passata nel costruttore è vuota oppure nulla ed
     * il set ha all'interno meno di 3 reparti.
     */
   
    public final void checkReparts  () throws TooFewRepartsExceptions
    {
        Set<Department> set = new HashSet<>();
        
        for (Product product : products) {
            
            set.add(product.getDepartment());
            
        }
        
        if(products.isEmpty() || products == null || set.size()<3) {
                
            throw new TooFewRepartsExceptions();

        }
 
}
    
    public final void checkDuplicate() throws ProductDuplicateException{
        Product product;
        
     for(int i=1;i<products.size();i++){
         for(int j=0;j<products.size();j++){
             if(products.get(i).equals(products.get(j))){
                 
                 product = products.get(i);
                 throw new ProductDuplicateException(product);
             }
             
         }
     }   
    }

    @Override
    public String toMQTTString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
