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
import it.cnr.istc.mw.mqtt.exceptions.DBBadParamaterException;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
//import org.junit.jupiter.api.Assumptions;

/**
 *
 * @author sommovir
 */
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class DBTest {

    long id1 = -1;
    long id2 = -1;
    Laboratory saved;

    public DBTest() {
    }

    private String message;
    private boolean ok;

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
        if (id1 != -1) {
            DBManager.getInstance().deleteLaboratory(id1);
            id1 = -1;
        }
        if (id2 != -1) {
            DBManager.getInstance().deleteLaboratory(id2);
            id2 = -1;
        }

    }

    private boolean isFoundLab(String lab) {
        for (Laboratory l : DBManager.getInstance().getAllLaboratories()) {
            if (l.getName().equals(lab)) {
                return true;
            }
        }
        return false;

    }

    public void removeToList(int num) {   //
        List<Laboratory> result = DBManager.getInstance().getAllLaboratories();
        if (result.size() > 0 || num < result.size()) {
            saved = result.get(num);
            result.remove(num);
        }

    }

    public boolean isLaboratoryPresent(String name) {
        List<Laboratory> allLaboratories = DBManager.getInstance().getAllLaboratories();
        for (Laboratory allLaboratory : allLaboratories) {
            if (allLaboratory.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Test
    @DisplayName("[getAllLaboratories()] create, oldsize, newsize, test size")
    public void test_Alfa1(TestInfo info) {
        message = info.getDisplayName();
        //test di prova delle Assumption
        boolean db_installed = DBManager.getInstance().isInstalled();
//        org.junit.jupiter.api.Assumptions.assumeTrue(connected, "Server non connesso");
        Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
        List<Laboratory> allLaboratories = DBManager.getInstance().getAllLaboratories();
        int oldSize = allLaboratories.size();
        DBUniqueViolationException assertThrows = assertThrows(
                DBUniqueViolationException.class,
                 () -> {
                    DBManager.getInstance()
                            .createLab("Laboratorio di Cucina");
                },
                "Il metodo createLab non ha lanciato l'eccezione di violazione di vincolo di unicità sul nome"
        );
        List<Laboratory> allLaboratories2 = DBManager.getInstance().getAllLaboratories();
        assertEquals(oldSize, allLaboratories2.size(), "Non puoi aggiungere due laboratori con lo stesso nome!");
        ok = true;
    }

    @Test
    @DisplayName("[getAllLaboratories()] create, create, test size")
    public void test_Alfa2(TestInfo info) {
        try {
            message = info.getDisplayName();
            boolean db_installed = DBManager.getInstance().isInstalled();
            Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
            List<Laboratory> result = DBManager.getInstance().getAllLaboratories();
            int oldSize = result.size();
            id1 = DBManager.getInstance().createLab("Prova 1");
            id2 = DBManager.getInstance().createLab("Prova 2");
            int newSize = DBManager.getInstance().getAllLaboratories().size();
            assertEquals(oldSize + 2, newSize, "Mi aspettavo che il size fosse incrementato di 2");
            ok = true;

        } catch (DBUniqueViolationException ex) {
            Logger.getLogger(DBTest.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (DBBadParamaterException ex) {
            Logger.getLogger(DBTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    @DisplayName("[getAllLaboratories()] create, create, Test find laboratories created")
    public void test_Alfa3(TestInfo info) {
        try {
            message = info.getDisplayName();
            boolean db_installed = DBManager.getInstance().isInstalled();
            Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
            List<Laboratory> result = DBManager.getInstance().getAllLaboratories();
            id1 = DBManager.getInstance().createLab("Prova 1");
            id2 = DBManager.getInstance().createLab("Prova 2");
            boolean found = isFoundLab("Prova 1");
            boolean found2 = isFoundLab("Prova 2");
            assertTrue(found, "Mi aspettavo che Prova 1 fosse inserito correttamente");
            assertTrue(found2, "Mi aspettavo che Prova 2 fosse inserito correttamente");
            ok = true;
        } catch (DBUniqueViolationException ex) {
            Logger.getLogger(DBTest.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (DBBadParamaterException ex) {
            Logger.getLogger(DBTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    @DisplayName("Test alfa_4")
    public void test_Alfa4(TestInfo info) {
        try {
            message = info.getDisplayName();
            boolean db_installed = DBManager.getInstance().isInstalled();
            Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
            List<Laboratory> result = DBManager.getInstance().getAllLaboratories();
            id1 = DBManager.getInstance().createLab("Prova 1");
            id2 = DBManager.getInstance().createLab("Prova 2");
            boolean found = isFoundLab("Prova 1");
            boolean found2 = isFoundLab("Prova 2");
            assertTrue(found, "Mi aspettavo che Prova 1 fosse inserito correttamente");
            assertTrue(found2, "Mi aspettavo che Prova 2 fosse inserito correttamente");
            ok = true;
        } catch (DBUniqueViolationException ex) {
            Logger.getLogger(DBTest.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (DBBadParamaterException ex) {
            Logger.getLogger(DBTest.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    @DisplayName("[getAllLaboratories()] test isLaboratoryPresent ")
    public void test_Alfa5(TestInfo info) {
        message = info.getDisplayName();
        boolean db_installed = DBManager.getInstance().isInstalled();
        Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
        boolean n1 = isLaboratoryPresent("Laboratorio di Cucito");
        boolean n2 = isLaboratoryPresent("Laboratorio di Arte");
        boolean n3 = isLaboratoryPresent("Laboratorio di Moda");
        boolean n4 = isLaboratoryPresent("Laboratorio di Sport");
        boolean n5 = isLaboratoryPresent("Laboratorio di Cucina");
        boolean n6 = isLaboratoryPresent("Laboratorio di Elettronica");
        assertFalse(n1, "Non mi aspettavo che Lab di Cucito fosse inserito nel database");
        assertFalse(n2, "Non mi aspettavo che Lab di Arte fosse inserito nel database");
        assertFalse(n3, "Non mi aspettavo che Lab di Moda fosse inserito nel database");
        assertFalse(n4, "Non mi aspettavo che Lab di Sport fosse inserito nel database");
        assertTrue(n5, "Mi aspettavo che Lab di Cucina fosse inserito nel database");
        assertFalse(n6, "Non mi aspettavo che Lab di Eletronicao fosse inserito nel database");
        ok = true;
    }
}
