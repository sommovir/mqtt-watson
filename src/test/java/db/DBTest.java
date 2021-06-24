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
    long id3 = -1;
    long id4 = -1;
    long id5 = -1;
    Laboratory saved;

    private String DatabaseNotInstalledMsg = "Il database non è installlato";

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

        if (id3
                != -1) {
            DBManager.getInstance().deleteLaboratory(id3);
            id3 = -1;
        }

        if (id4
                != -1) {
            DBManager.getInstance().deleteLaboratory(id4);
            id4 = -1;
        }
        if (id5
                != -1) {
            DBManager.getInstance().deleteLaboratory(id5);
            id5 = -1;
        }

    }

    private String DatabaseMessageError() {  //luca lascia stare, volevo provare una cosa ma ho failato
        return DatabaseNotInstalledMsg;
    }

    private boolean isFoundLab(String lab) {
        for (Laboratory l : DBManager.getInstance().getAllLaboratories()) {
            if (l.getName().equals(lab)) {
                return true;
            }
        }
        return false;

    }

    public void removeToList(long num) {    //da revisionare con luca );
        List<Laboratory> result = DBManager.getInstance().getAllLaboratories();
        if (result.size() > 0 || num < result.size()) {
            saved = result.get((int) num);
            result.remove(num);
        }

    }

    public void removeToDatabase(long num) {
        DBManager.getInstance().deleteLaboratory(num);

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

    public boolean isLaboratoryValid(String name) {
        if (name.contains("Laboratorio")) {
            return true;
        }
        return false;
    }

    @Test
    @DisplayName("[getAllLaboratories()] create, oldsize, newsize, test size")
    public void test_Alfa1(TestInfo info) {
        message = info.getDisplayName();
        //test di prova delle Assumption
        boolean db_installed = DBManager.getInstance().isInstalled();
        //org.junit.jupiter.api.Assumptions.assumeTrue(connected, "Server non connesso");
        Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
        List<Laboratory> allLaboratories = DBManager.getInstance().getAllLaboratories();
        int oldSize = allLaboratories.size();
        DBUniqueViolationException assertThrows = assertThrows(
                DBUniqueViolationException.class,
                 () -> {
                    id1 = DBManager.getInstance()
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
            if (db_installed == false) {
                DatabaseMessageError();
            }
            Assumptions.assumeThat(db_installed).isTrue();
            List<Laboratory> result = DBManager.getInstance().getAllLaboratories();
            int oldSize = result.size();
            id1 = DBManager.getInstance().createLab("Prova 1");
            id2 = DBManager.getInstance().createLab("Prova 2");
            int newSize = DBManager.getInstance().getAllLaboratories().size();
            assertEquals(oldSize + 2, newSize, "Mi aspettavo che il size fosse incrementato di 2");

        } catch (DBUniqueViolationException ex) {
            assertTrue(false, "Rilevata eccezione: DBUniqueViolationException (nome lab uguale a un altro)");

        } catch (DBBadParamaterException ex) {
            assertTrue(false, "Rilevata eccezione: DBBadParamaterException (parametro nullo o empty)");
        }
        ok = true;
    }

    @Test
    @DisplayName("[getAllLaboratories()] create x4, Test isFoundLab")
    public void test_Alfa3(TestInfo info) {
        try {
            message = info.getDisplayName();
            boolean db_installed = DBManager.getInstance().isInstalled();
            Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
            List<Laboratory> result = DBManager.getInstance().getAllLaboratories();
            DBManager.getInstance().createLab("Prova 1");
            id2 = DBManager.getInstance().createLab("Prova 2");
            id3 = DBManager.getInstance().createLab("Laboratorio di cucina");
            id3 = DBManager.getInstance().createLab("Laboratorio di informatica");
            boolean found = isFoundLab("Prova 1");
            boolean found2 = isFoundLab("Prova 2");
            boolean found3 = isFoundLab("Laboratorio di cucina");
            boolean found4 = isFoundLab("Laboratorio di informatica");
            assertTrue(found, "Mi aspettavo che Prova 1 fosse inserito correttamente");
            assertTrue(found2, "Mi aspettavo che Prova 2 fosse inserito correttamente");
            assertTrue(found3, "Mi aspettavo che Laboratorio di cucina fosse inserito correttamente");
            assertTrue(found4, "Mi aspettavo che Laboratorio di informatica fosse inserito correttamente");
            

        } catch (DBUniqueViolationException ex) {
            assertTrue(false, "Rilevata eccezione: DBUniqueViolationException (nome lab uguale a un altro)");

        } catch (DBBadParamaterException ex) {
            assertTrue(false, "Rilevata eccezione: DBBadParamaterException (parametro nullo o empty)");
        }
        ok = true;
    }

    @Test
    @DisplayName("[getAllLaboratories()] test isLaboratoryPresent ")
    public void test_Alfa4(TestInfo info) {
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
        assertFalse(n6, "Non mi aspettavo che Lab di Eletronica fosse inserito nel database");
        ok = true;
    }

    @Test
    @DisplayName("[createLab()] Test paramater valid")
    public void test_Alfa5(TestInfo info) {

        message = info.getDisplayName();
        boolean db_installed = DBManager.getInstance().isInstalled();
        Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
        assertThrows(
                DBBadParamaterException.class,
                 () -> {
                    id1 = DBManager.getInstance().createLab("");
                },
                 "Il metodo createLab non ha lanciato l'eccezione di inserimento di stringa vuota"
        );
        assertThrows(
                DBBadParamaterException.class,
                 () -> {
                    id2 = DBManager.getInstance().createLab(null);
                },
                 "Il metodo createLab non ha lanciato l'eccezione di inserimento nullo"
        );
        assertThrows(
                DBBadParamaterException.class,
                 () -> {
                    id3 = DBManager.getInstance().createLab(" ");
                },
                 "Il metodo createLab non ha lanciato l'eccezione di inserimento di parametri errati"
        );
        ok = true;

    }

    @Test
    @DisplayName("[createLab()] create x5 Test createLab DBUniqueViolationException")
    public void test_Alfa6(TestInfo info) throws DBUniqueViolationException {

        try {
            message = info.getDisplayName();
            boolean db_installed = DBManager.getInstance().isInstalled();
            Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
            id1 = DBManager.getInstance().createLab("Laboratorio di scacchi");
            id3 = DBManager.getInstance().createLab("Laboratorio di videogiochi");
            id4 = DBManager.getInstance().createLab("Laboratorio di programmazione");
            id5 = DBManager.getInstance().createLab("Laboratorio di Balze");
            assertThrows(
                    DBUniqueViolationException.class,
                     () -> {
                        id2 = DBManager.getInstance().createLab("Laboratorio di scacchi");
                    },
                     "Il metodo create lab non ha lanciato l'eccezione di vincolo di unicità del nome"
            );
            
        } catch (DBBadParamaterException ex) {
            assertTrue(false, "Rilevata eccezione: DBBadParamaterException (parametro nullo o empty)");
        }
        ok = true;
    }

    @Test
    @DisplayName("[isInstalled()] Test database isInstalled")
    public void test_Alfa7(TestInfo info) {
        message = info.getDisplayName();
        boolean db_installed = DBManager.getInstance().isInstalled();
        assertTrue(db_installed, "Il database non è installato!!!!");
        ok = true;

    }

    @Test
    @DisplayName("[getAllLaboratories()] removeToDatabase check into list object removed Test")
    public void test_Alfa8(TestInfo info) {
        try {
            message = info.getDisplayName();
            boolean db_installed = DBManager.getInstance().isInstalled();
            Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
            id1 = DBManager.getInstance().createLab("Laboratorio di scacchi");
            id2 = DBManager.getInstance().createLab("Laboratorio di videogiochi");
            id3 = DBManager.getInstance().createLab("Laboratorio di programmazione");
            id4 = DBManager.getInstance().createLab("Laboratorio di Balze");
            List<Laboratory> allLaboratories = DBManager.getInstance().getAllLaboratories();
            removeToDatabase(id2);
            boolean foundLab = isFoundLab("Laboratorio di videogiochi");
            assertFalse(foundLab, "Mi aspettavo che Laboratorio di videogiochi fosse stato eliminato!");
            id2 = DBManager.getInstance().createLab("Laboratorio di videogiochi");
        } catch (DBUniqueViolationException ex) {
            assertTrue(false, "Rilevata eccezione: DBUniqueViolationException (nome lab uguale a un altro)");

        } catch (DBBadParamaterException ex) {
            assertTrue(false, "Rilevata eccezione: DBBadParamaterException (parametro nullo o empty)");
        }
        ok = true;
    }
}
