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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author sommovir
 */
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class CognitiveGameTest {

    boolean ok = false;
    private String message;

    public CognitiveGameTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        System.out.println("---------------------------");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("----------------------------");
    }

    @BeforeEach
    public void setUp() {

        ok = false;
    }

    @AfterEach
    public void tearDown() {
        System.out.println(message + (ok ? " SUCCESS" : " FAILED"));
    }

    @Test
    public void test1() {
        //prova, da rimuovere. Luca
        //@TODO
        assertFalse(LoggerManager.getInstance().isLogActive(), "Dovrebbe essere falso la prima volta");
    }

}
