/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.ai.neuralnet.perceptron;

import it.cnr.istc.mw.mqtt.ai.neuralnet.Main;
import it.cnr.istc.mw.mqtt.ai.neuralnet.databucket.DataBucket;
import it.cnr.istc.mw.mqtt.ai.neuralnet.databucket.Dataset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author loren
 */
public class Trainer {
    
    private Perceptron perceptron;
    private DataBucket dataTest;
    private DataBucket dataTrain;

    public Trainer(int size, DataBucket dataTrain ,DataBucket dataTest) {
        this.perceptron =new Perceptron(size);
        this.dataTest = dataTest;
        this.dataTrain= dataTrain;
    }
    
    public void Train(){
        
        for (Dataset datatrain : this.dataTrain.getDatasets()) {
        perceptron.train(datatrain);
        }
    }
        
    public float Test(){
    
        int result;
        int i=0; 
        for (Dataset datatest : this.dataTest.getDatasets()) {
            try {
                result=perceptron.test(datatest.getInputs());
                if(result==datatest.getDesiredAnswer()){
                    i++;
                }
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        float percentuale=(i*100)/10;
        return percentuale;
    }
    
    public void TryTest(){
        
    }
   
    
    
}
        
