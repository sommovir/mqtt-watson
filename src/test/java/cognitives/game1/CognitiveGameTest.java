/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitives.game1;

import it.cnr.istc.mw.mqtt.logic.logger.LoggerManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author sommovir
 */
public class CognitiveGameTest {
    
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
    public void test1(){
        //prova, da rimuovere. Luca
        //@TODO
        assertFalse(LoggerManager.getInstance().isLogActive(),"Dovrebbe essere falso la prima volta");
    }
    
}
