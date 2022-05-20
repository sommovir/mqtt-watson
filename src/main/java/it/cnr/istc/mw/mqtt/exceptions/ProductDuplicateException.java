/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.exceptions;

import it.cnr.istc.mw.mqtt.logic.mindgames.game1.Product;

/**
 *
 * @author Luca
 */
public class ProductDuplicateException extends MindGameException{

    private Product product;

    public ProductDuplicateException() {
    }

    public ProductDuplicateException(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
   

    @Override
    public String errorMessage() {
       return "non puoi inserire duplicati";
    }
    
    
    
    
}
