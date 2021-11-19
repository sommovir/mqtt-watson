/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.ai.neuralnet;

/**
 *
 * @author Lorenzo Gilli
 */
public class Dataset {

    private float[] inputs;
    private int desiredAnswer;
    private int givenAnswer = 0;

    public Dataset(float[] inputs, int desiredAnswer) {
        this.inputs = inputs;
        this.desiredAnswer = desiredAnswer;
    }

    public float[] getInputs() {
        return inputs;
    }

    public void setInputs(float[] inputs) {
        this.inputs = inputs;
    }

    public int getDesiredAnswer() {
        return desiredAnswer;
    }

    public void setDesiredAnswer(int desiredAnswer) {
        this.desiredAnswer = desiredAnswer;
    }

    public void setGivenAnswer(int givenAnswer) {
        this.givenAnswer = givenAnswer;
    }

    public int getGivenAnswer() {
        return givenAnswer;
    }

    public int getError() {
        return this.desiredAnswer - this.givenAnswer;
    }

    @Override
    public String toString() {
        String r = "value={";
        for (int i = 0; i < inputs.length-1; i++) {
            r+=inputs[i]+", ";
        }
        r+=inputs[inputs.length-1]+"}";
        return r;
    }

}
