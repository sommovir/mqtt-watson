/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.ai.neuralnet;

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
    
}
