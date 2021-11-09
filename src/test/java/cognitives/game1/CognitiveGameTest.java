/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitives.game1;

import it.cnr.istc.mw.mqtt.logic.logger.LoggerManager;
import it.cnr.istc.mw.mqtt.logic.mindgames.MindGameEngine;
import it.cnr.istc.mw.mqtt.logic.mindgames.Product;
import java.lang.ProcessHandle.Info;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author sommovir
 */
@Disabled
public class CognitiveGameTest {
    
    String message;
    List<Product> prodotti = new LinkedList<Product>();
    
    public CognitiveGameTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    @DisplayName("[checkDuplicate]")
    public void test1(Info info){
        //prova, da rimuovere. Luca
        //@TODO
        //assertFalse(LoggerManager.getInstance().isLogActive(),"Dovrebbe essere falso la prima volta");
        prodotti.add(new Product(8, "carota"));
        boolean check = MindGameEngine.getInstance().checkDuplicate(prodotti); 
        //assertFalse();
    }
    
}
