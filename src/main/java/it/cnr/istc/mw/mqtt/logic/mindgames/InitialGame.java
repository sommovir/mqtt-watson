/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Luca
 */
public class InitialGame {
    
    private String initialText;
    private List<Product> productAndDepartmentList = new ArrayList<Product>();

    public InitialGame() {
    }

    public InitialGame(String initialtext) {
        this.initialText = initialtext;
    }

    public String getInitialText() {
        return initialText;
    }

    public List<Product> productAndDepartmentList () {
        return productAndDepartmentList ;
    }

    public void setInitialText(String initialtext) {
        this.initialText = initialtext;
    }

    public void setProductAndDepartmentList(List<Product> productAndDepartmentList ) {
        this.productAndDepartmentList  = productAndDepartmentList ;
    }
    
 
}
