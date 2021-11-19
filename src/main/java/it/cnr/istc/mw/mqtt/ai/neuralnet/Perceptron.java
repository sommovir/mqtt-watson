/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.ai.neuralnet;

/**
 *
 * @author Luca
 */
public class Perceptron {
    
    private int size;
    private float []weights;
    private float []inputs;
    private int lastActivationResult;
    private float learningRate= 0.25f;

    
    public Perceptron(int size) {
        this.size = size;
        this.weights= new float[size];
        for(int i=0; i<size; i++){
           this.weights[i]= Utilities.randomNegativePositive();
        }
    }

    public float[] getWeights() {
        return weights;
    }

    public float[] getInput() {
        //float [] inn = new float[4];
        return inputs;
    }

    public void setInput(float[] input) throws Exception{
        if(input.length!=this.size){
            throw new Exception("Lenghts Mismatch!");
        }else{
            for(int i=0; i<size ; i++){
                this.inputs[i]= input[i];
            }
        }
    }
    
    public int sign(float x){
        return x >= 0 ? 1 : -1;
    }
    
    public int activate() {

        float potenzialeAttivazione = 0;
        for (int i = 0; i < size; i++) {
            potenzialeAttivazione += (this.inputs[i] * this.weights[i]);
        }
        this.lastActivationResult = sign(potenzialeAttivazione);
        return this.lastActivationResult;

    }
    
    public int test(float[] ins) throws Exception{
        setInput(ins);
        return activate();
    }
    public void train(Dataset dataset){
        this.inputs = dataset.getInputs();
        dataset.setGivenAnswer(this.activate());
        int error = dataset.getError();
        for (int i = 0; i < this.size; i++) {
             this.weights[i] +=  (error * this.inputs[i] * this.learningRate);
        }
    }
    public void wrong(){
        int error = -this.lastActivationResult - this.lastActivationResult;
        for (int i = 0; i < this.size ; i++) {
             this.weights[i] +=  (error * this.inputs[i] * this.learningRate);
        }
    }
    
    
    
}
