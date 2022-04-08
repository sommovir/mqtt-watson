/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.istc.mw.mqtt.exceptions.InvalidProductException;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameType;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.InitialState;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Luca
 */
public class SuperMarketInitialState extends InitialState<SuperMarketSolution> {

    private List<Product> products = null;
    

    public SuperMarketInitialState(List<Product> products) throws InvalidProductException {
        super(GameType.LISTA_SPESA);
        this.products = products;
        this.solution = new SuperMarketSolution(products);
                
        
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String getWatsonText() {
       String istruzioni = "Buongiorno, se passi al supermercato mi puoi comprare: ";
        for (int i = 0; i < products.size()-1; i++) {
            istruzioni+=" "+products.get(i).toString()+",";
        }
        
        istruzioni+=" e infine "+products.get(products.size()-1);

       return istruzioni;
    }

    @Override
    public String getDescriptionText() {
        return GameType.LISTA_SPESA.getDescrizioneTestuale();
    }


    @Override
    public SuperMarketSolution getSolution() {
        
         return this.solution;
    }

    @Override
    public String getDescriptionVocal() {
        return GameType.LISTA_SPESA.getDescrizioneVocale();
    }

    @Override
    public String toJson() {
         ObjectMapper obj = new ObjectMapper();
          String jsonStr = null;
        try {

             jsonStr = obj.writeValueAsString(this);

        }
 
        catch (IOException e) {
            e.printStackTrace();
  
        }
        return jsonStr;
    
    }

}
