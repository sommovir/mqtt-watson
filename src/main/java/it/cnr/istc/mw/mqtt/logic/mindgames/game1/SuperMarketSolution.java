/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import it.cnr.istc.mw.mqtt.logic.mindgames.models.Solution;
import java.util.LinkedList;
import java.util.List;

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
    

    @Override
    public String toMQTTString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
