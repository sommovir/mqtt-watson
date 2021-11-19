/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameType;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.InitialState;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Luca
 */
public class SuperMarketInitialState extends InitialState<SuperMarketSolution> {

    private List<Product> products = null;
    

    public SuperMarketInitialState(List<Product> products) {
        super(GameType.LISTA_SPESA);
        this.products = products;
        this.solution = new SuperMarketSolution(products);
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

}
