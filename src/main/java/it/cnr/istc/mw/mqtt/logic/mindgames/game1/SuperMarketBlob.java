/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Luca
 */
public class SuperMarketBlob {

    private String initialMessage; //watson text
    private List<Product> solutionProducts;
    private String request;
    private String vocalDescription;
    private String textualDescription;

    public SuperMarketBlob() {
    }

    public String getInitialMessage() {
        return initialMessage;
    }

    public void setInitialMessage(String initialMessage) {
        this.initialMessage = initialMessage;
    }

    public List<Product> getSolutionProducts() {
        return solutionProducts;
    }

    public void setSolutionProducts(List<Product> solutionProducts) {
        this.solutionProducts = solutionProducts;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getVocalDescription() {
        return vocalDescription;
    }

    public void setVocalDescription(String vocalDescription) {
        this.vocalDescription = vocalDescription;
    }

    public String getTextualDescription() {
        return textualDescription;
    }

    public void setTextualDescription(String textualDescription) {
        this.textualDescription = textualDescription;
    }

    public SuperMarketBlob(String initialMessage, List<Product> solutionProducts, String request, String vocalDescription, String textualDescription) {
        this.initialMessage = initialMessage;
        this.solutionProducts = solutionProducts;
        this.request = request;
        this.vocalDescription = vocalDescription;
        this.textualDescription = textualDescription;
    }


    public String toJson() {
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ObjectMapper obj = new ObjectMapper();
        String jsonStr = null;
        try {

            jsonStr = obj.writerWithDefaultPrettyPrinter().writeValueAsString(this);

        } catch (IOException e) {
            e.printStackTrace();

        }
        return jsonStr;

    }

}
