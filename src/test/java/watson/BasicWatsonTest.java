/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package watson;

import it.cnr.istc.mw.mqtt.MQTTClient;
import it.cnr.istc.mw.mqtt.MQTTServer;
import it.cnr.istc.mw.mqtt.db.DBManager;
import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Assumptions;

/**
 *
 * @author sommovir
 */
@Disabled
public class BasicWatsonTest {

    public BasicWatsonTest() {
    }

    private String message = "Test BasicWatsonTest"; //andrà cancellato alla creazione del primo metodo di test
    boolean ok = false;

    @BeforeAll
    public static void setUpClass() {
        System.out.println("---------------------------");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("---------------------------");
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
    public void testG() {
        //test di prova delle Assumption
        boolean connected = MQTTClient.getInstance().isConnected();
//        org.junit.jupiter.api.Assumptions.assumeTrue(connected, "Server non connesso");
        Assumptions.assumeThat(connected).withFailMessage("ciaaia").isTrue();
        assertTrue(MQTTClient.getInstance().getIP().startsWith("192"), "L'ip è strano");

    }

}
