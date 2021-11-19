/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.ai.neuralnet.databucket;

import it.cnr.istc.mw.mqtt.ai.neuralnet.perceptron.Utilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Lorenzo Gilli
 */
public class DataBucket {
    
    private int size;
    private List<Dataset> datasets;
    private int packSize = 50;
    private int globalIteration = 3;

    public DataBucket(int size) {
        this.packSize = size;
        this.datasets = new ArrayList<>();
    }
    
    public void addDataset(Dataset data){
        this.datasets.add(data);
    }
    
    public float getErrorDeviationStandard(){
        float [] errors  = new float[size];
        
        for (int i=0;i<size;i++) {
            errors[i] = datasets.get(i).getError();
            System.out.println("error in dataset: "+errors[i]);
        }
        return Utilities.calculateStandarDeviation(errors);
    }

    public List<Dataset> getDatasets() {
        return datasets;
    }

    public int getPackSize() {
        return packSize;
    }

    public void setPackSize(int packSize) {
        this.packSize = packSize;
    }
    
    
    public void shuffle(){
        Collections.shuffle(datasets);
    }

    public void setGlobalIteration(int globalIteration) {
        this.globalIteration = globalIteration;
    }

    public int getGlobalIteration() {
        return globalIteration;
    }
    
    
    
    
}
