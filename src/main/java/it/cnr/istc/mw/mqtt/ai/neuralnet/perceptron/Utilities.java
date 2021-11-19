/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.ai.neuralnet.perceptron;

import it.cnr.istc.mw.mqtt.ai.neuralnet.databucket.DataBucket;
import it.cnr.istc.mw.mqtt.ai.neuralnet.databucket.Dataset;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author loren
 */
public class Utilities {
    
    public static float randomNegativePositive() {

        boolean positive = ThreadLocalRandom.current().nextFloat() >= 0.5f;

        float result = ThreadLocalRandom.current().nextFloat();

        return positive ? result : -result;

    }
        public static float calculateStandarDeviation(float[] values) {

        if (values.length == 0) {
            return 0;
        }

        float sum = 0;
        for (float value : values) {
            sum += value;
        }
        float average = sum / values.length;
        System.out.println("average: " + average);

        float deviation = 0;
        for (float value : values) {
            deviation += ((value - average) * (value - average));
        }

        return (float) Math.sqrt(deviation / ((float) values.length - 1));

    }
        
    public static DataBucket getTrainerData(){
    //DataSet per il training
    Dataset datatrain1 = new Dataset(new float[]{1,0,0,0,0,1},-1);
    Dataset datatrain2 = new Dataset(new float[]{1,0,0,0,1,0},1);
    Dataset datatrain3 = new Dataset(new float[]{0,0,0,0,1,1},-1);
    Dataset datatrain4 = new Dataset(new float[]{0,0,0,1,0,0},1);
    Dataset datatrain5 = new Dataset(new float[]{1,0,0,1,0,1},-1);
    Dataset datatrain6 = new Dataset(new float[]{1,0,0,1,1,0},1);
    Dataset datatrain7 = new Dataset(new float[]{0,0,0,1,1,1},-1);
    Dataset datatrain8 = new Dataset(new float[]{0,0,1,0,0,0},1);
    Dataset datatrain9 = new Dataset(new float[]{1,0,1,0,0,1},-1);
    Dataset datatrain10 = new Dataset(new float[]{1,0,1,0,1,0},1);
    Dataset datatrain11 = new Dataset(new float[]{0,0,1,0,1,1},-1);
    Dataset datatrain12 = new Dataset(new float[]{0,0,1,1,0,0},1);
    Dataset datatrain13 = new Dataset(new float[]{1,0,1,1,0,1},-1);
    Dataset datatrain14 = new Dataset(new float[]{1,0,1,1,1,0},1);
    Dataset datatrain15 = new Dataset(new float[]{0,0,1,1,1,1},-1);
    Dataset datatrain16 = new Dataset(new float[]{0,1,0,0,0,0},1);
    Dataset datatrain17 = new Dataset(new float[]{1,1,0,0,0,1},-1);
    Dataset datatrain18 = new Dataset(new float[]{1,1,0,0,1,0},1);
    Dataset datatrain19 = new Dataset(new float[]{0,1,0,0,1,1},-1);
    Dataset datatrain20 = new Dataset(new float[]{0,1,0,1,0,0},1);
    //DataBucket per il train
    DataBucket dataTrain = new DataBucket(20);
    //Riempimento DataBucket
    dataTrain.addDataset(datatrain1);
    dataTrain.addDataset(datatrain2);
    dataTrain.addDataset(datatrain3);
    dataTrain.addDataset(datatrain4);
    dataTrain.addDataset(datatrain5);
    dataTrain.addDataset(datatrain6);
    dataTrain.addDataset(datatrain7);
    dataTrain.addDataset(datatrain8);
    dataTrain.addDataset(datatrain9);
    dataTrain.addDataset(datatrain10);
    dataTrain.addDataset(datatrain11);
    dataTrain.addDataset(datatrain12);
    dataTrain.addDataset(datatrain13);
    dataTrain.addDataset(datatrain14);
    dataTrain.addDataset(datatrain15);
    dataTrain.addDataset(datatrain16);
    dataTrain.addDataset(datatrain17);
    dataTrain.addDataset(datatrain18);
    dataTrain.addDataset(datatrain19);
    dataTrain.addDataset(datatrain20);
    
    return dataTrain;
    }
    
    public static DataBucket getTestData(){
    //TEST
    Dataset datatest1 = new Dataset(new float[]{0,1,0,1,1,0}, 1);
    Dataset datatest2 = new Dataset(new float[]{0,1,0,1,1,1}, -1);
    Dataset datatest3 = new Dataset(new float[]{1,1,1,0,0,0}, 1);
    Dataset datatest4 = new Dataset(new float[]{1,1,1,0,0,1}, -1);
    Dataset datatest5 = new Dataset(new float[]{0,1,1,0,1,0}, 1);
    Dataset datatest6 = new Dataset(new float[]{0,1,1,0,1,1}, -1);
    Dataset datatest7 = new Dataset(new float[]{1,1,1,1,0,0}, 1);
    Dataset datatest8 = new Dataset(new float[]{1,1,1,1,0,1}, -1);
    Dataset datatest9 = new Dataset(new float[]{0,1,1,1,1,0}, 1);
    Dataset datatest10 = new Dataset(new float[]{0,1,1,1,1,1}, -1);

    DataBucket dataTest = new DataBucket(10);
    dataTest.addDataset(datatest1);
    dataTest.addDataset(datatest2);
    dataTest.addDataset(datatest3);
    dataTest.addDataset(datatest4);
    dataTest.addDataset(datatest5);
    dataTest.addDataset(datatest6);
    dataTest.addDataset(datatest7);
    dataTest.addDataset(datatest8);
    dataTest.addDataset(datatest9);
    dataTest.addDataset(datatest10);
    
    return dataTest;
    }
    
}
