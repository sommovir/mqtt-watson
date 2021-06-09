/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import watson.*;
import it.cnr.istc.mw.mqtt.MQTTClient;
import it.cnr.istc.mw.mqtt.MQTTServer;
import it.cnr.istc.mw.mqtt.db.DBManager;
import it.cnr.istc.mw.mqtt.db.Laboratory;
import it.cnr.istc.mw.mqtt.exceptions.DBUniqueViolationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Assumptions;

/**
 *
 * @author sommovir
 */
public class DBTest {
    
    public DBTest() {
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
    public void test_Alfa1(){
        //test di prova delle Assumption
        boolean db_installed  = DBManager.getInstance().isInstalled();
//        org.junit.jupiter.api.Assumptions.assumeTrue(connected, "Server non connesso");
        Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
        List<Laboratory> allLaboratories = DBManager.getInstance().getAllLaboratories();
        int oldSize = allLaboratories.size();
        DBUniqueViolationException assertThrows = assertThrows(
                DBUniqueViolationException.class,
                () -> {DBManager.getInstance().createLab("Laboratorio di Cucina");},
                "Il metodo createLab non ha lanciato l'eccezione di violazione di vincolo di unicità sul nome"
        );
        List<Laboratory> allLaboratories2 = DBManager.getInstance().getAllLaboratories();
        assertEquals(oldSize,allLaboratories2.size(),"Non puoi aggiungere due laboratori con lo stesso nome!");
        
    }
    
}
