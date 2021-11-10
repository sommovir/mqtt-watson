/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.game1;

import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameResult;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.Product;
import it.cnr.istc.mw.mqtt.db.Person;
import java.util.Date;
import java.util.List;

/**
 *
 * @author loren
 */
public class OLD_GameInstance {

    private String gameType;            //- tipo di gioco selezionato
    private Person user;                //- utente che l'ha richiesto
    private Date date;                  //- data di svolgimento
    private GameResult gameResult;      //- esito del gioco
    private List<Product>products;     //- pacchetto dati della sessione di gioco particolare
    private List<Product>pruducts;     //(Solution, lista dei prodotto con relativo reparto)"

    public OLD_GameInstance() {
    }

    public OLD_GameInstance(String gameType, Person user, Date date, GameResult gameResult, List<Product> products, List<Product> pruducts) {
        this.gameType = gameType;
        this.user = user;
        this.date = date;
        this.gameResult = gameResult;
        this.products = products;
        this.pruducts = pruducts;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getPruducts() {
        return pruducts;
    }

    public void setPruducts(List<Product> pruducts) {
        this.pruducts = pruducts;
    }
    
    
    
    

}
