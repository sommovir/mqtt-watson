/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.ai.neuralnet;

import it.cnr.istc.mw.mqtt.ai.neuralnet.perceptron.Perceptron;
import it.cnr.istc.mw.mqtt.ai.neuralnet.databucket.DataBucket;
import it.cnr.istc.mw.mqtt.ai.neuralnet.databucket.Dataset;
import it.cnr.istc.mw.mqtt.ai.neuralnet.perceptron.Utilities;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author loren
 */
public class Main {
    public static void main(String[] args) {
    
         //creare un trainer passandogli dati da Utilites 
         
         //

    

    Perceptron perceptron1 = new Perceptron(6);

    for (Dataset datatrain : Utilities.getTrainerData().getDatasets()) {
        perceptron1.train(datatrain);
    }

    int result;
    int i=0; 
    for (Dataset datatest : dataTest.getDatasets()) {
        try {
            result=perceptron1.test(datatest.getInputs());
            if(result==datatest.getDesiredAnswer()){
                i++;
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    float percentuale=(i*100)/10;
        System.out.println("la percentuale di corretti è "+percentuale);
        
        
        
        
        
}
}
