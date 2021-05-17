/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author sommovir
 */
public class HistoryBook {

    private static HistoryBook _instance = null;
    private List<HistoryElement> history = new LinkedList<>();

    public static HistoryBook getInstance() {
        if (_instance == null) {
            _instance = new HistoryBook();
            return _instance;
        } else {
            return _instance;
        }
    }

    private HistoryBook() {
        super();
    }
    
    public void clear(){
        this.history.clear();
    }
    
    
    public void addHistoryElement(String input, String output){
        this.history.add(new HistoryElement(input, output, new Date()));
    }
    
    
    public HistoryElement [] getLast10Elements(){
        
        int size = history.size();
        size = size >= 10 ? 10 : size;
        HistoryElement [] elements = new HistoryElement[size];
        
        for (int i = 0; i < size; i++) {
            elements[elements.length - size] =  history.get(elements.length - size + i);
        }
        
        return elements;
    }
    
    
    public HistoryElement [] getLastElements(int n){
        
        int size = history.size();  
        int start = size-n;
        if(n>size){
            n = size;
            start = 0;
        }
        
        HistoryElement [] elements = new HistoryElement[n];
        
        for (int i = size-1,k=n ; k >0; i--,k--) {
            elements[k-1] =  history.get(i);
        }
        
        return elements;
    }

    public List<HistoryElement> getHistory() {
        return history;
    }
    
    
   
    

}
