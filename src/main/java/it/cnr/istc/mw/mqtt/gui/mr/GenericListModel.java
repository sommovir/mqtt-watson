/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.gui.mr;

import java.util.LinkedList;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author sommovir
 */
public class GenericListModel<T> implements ListModel{
    
    private LinkedList<T> data;
    private LinkedList<ListDataListener> listeners;

    public GenericListModel() {
        data = new LinkedList();
        listeners = new LinkedList();
    }
    
    public void addElement(T element){
        data.add(element);
    }
    
    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public T getElementAt(int index) {
        return data.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
    
}
