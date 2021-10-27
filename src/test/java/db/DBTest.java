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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
//import org.junit.jupiter.api.Assumptions;

/**
 *
 * @author sommovir
 */
@Disabled
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class DBTest {

    Long id1 = -1l; //-1 L 
    Long id2 = -1l;
    Long id3 = -1l;
    Long id4 = -1l;
    Long id5 = -1l;
    Long id6 = -1l;
    Long id7 = -1l;
    Long id8 = -1l;
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
        System.out.println(message + (ok ? " [SUCCESS] " : " [FAILED] "));
        if (id1 != -1) {
            DBManager.getInstance().deleteLaboratory(id1);
            id1 = -1L;
        }
        if (id2 != -1) {
            DBManager.getInstance().deleteLaboratory(id2);
            id2 = -1L;
        }

        if (id3
                != -1) {
            DBManager.getInstance().deleteLaboratory(id3);
            id3 = -1L;
        }

        if (id4
                != -1) {
            DBManager.getInstance().deleteLaboratory(id4);
            id4 = -1L;
        }
        if (id5
                != -1) {
            DBManager.getInstance().deleteLaboratory(id5);
            id5 = -1L;
        }
        if (id6
                != -1) {
            DBManager.getInstance().deleteLaboratory(id6);
            id6 = -1L;
        }
        if (id7
                != -1) {
            DBManager.getInstance().deleteLaboratory(id7);
            id7 = -1L;
        }
        if (id8
                != -1) {
            DBManager.getInstance().deleteLaboratory(id8);
            id8 = -1L;
        }
        //DBManager.getInstance().getAllLaboratories().clear();

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
        return name.contains("Laboratorio");
    }

    public long findIdByName(Laboratory name) {
        List<Laboratory> allLaboratories = DBManager.getInstance().getAllLaboratories(); //posso mettere tipo if e se lancia eccezioni returna -1???
        if (name.getName() == null || name.getName().isEmpty() || !name.getName().contains("Laboratorio")) {
            return -1;
        }
        for (Laboratory allLab : allLaboratories) {
            if (allLab.getId() == name.getId()) {
                return allLab.getId();
            }
        }
        return -1;
    }

    public boolean isFindIdLabById(long id) {
        List<Laboratory> allLaboratories = DBManager.getInstance().getAllLaboratories();
        for (Laboratory allLab : allLaboratories) {
            if (allLab.getId() == id) {
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
        //org.junit.jupiter.api.Assumptions.assumeTrue(connected, "Server non connesso");
        Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
        //----------------------------------------------------------------------------------
        List<Laboratory> allLaboratories = DBManager.getInstance().getAllLaboratories();
        int oldSize = allLaboratories.size();
        DBUniqueViolationException assertThrows = assertThrows(
                DBUniqueViolationException.class,
                () -> {
                    id1 = DBManager.getInstance().createLab("Laboratorio di Cucina").getId();
                },
                "Il metodo createLab non ha lanciato l'eccezione di violazione di vincolo di unicità sul nome"
        );
        //----------------------------------------------------------------------------------
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
            //----------------------------------------------------------------------------------
            if (db_installed == false) {
                DatabaseMessageError();
            }
            //----------------------------------------------------------------------------------
            Assumptions.assumeThat(db_installed).isTrue();
            List<Laboratory> result = DBManager.getInstance().getAllLaboratories();
            int oldSize = result.size();
            //----------------------------------------------------------------------------------
            id1 = DBManager.getInstance().createLab("Prova 1").getId();
            id2 = DBManager.getInstance().createLab("Prova 2").getId();
            //----------------------------------------------------------------------------------
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
            //----------------------------------------------------------------------------------
            List<Laboratory> result = DBManager.getInstance().getAllLaboratories();
            DBManager.getInstance().createLab("Prova 1");
            id2 = DBManager.getInstance().createLab("Prova 2").getId();
            id3 = DBManager.getInstance().createLab("Laboratorio di cucina").getId();
            id3 = DBManager.getInstance().createLab("Laboratorio di informatica").getId();
            //----------------------------------------------------------------------------------
            boolean found = isFoundLab("Prova 1");
            boolean found2 = isFoundLab("Prova 2");
            boolean found3 = isFoundLab("Laboratorio di cucina");
            boolean found4 = isFoundLab("Laboratorio di informatica");
            //----------------------------------------------------------------------------------
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
        //----------------------------------------------------------------------------------
        boolean n1 = isLaboratoryPresent("Laboratorio di Cucito");
        boolean n2 = isLaboratoryPresent("Laboratorio di Arte");
        boolean n3 = isLaboratoryPresent("Laboratorio di Moda");
        boolean n4 = isLaboratoryPresent("Laboratorio di Sport");
        boolean n5 = isLaboratoryPresent("Laboratorio di Cucina");
        boolean n6 = isLaboratoryPresent("Laboratorio di Elettronica");
        //----------------------------------------------------------------------------------
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
        //----------------------------------------------------------------------------------
        assertThrows(
                DBBadParamaterException.class,
                () -> {
                    id1 = DBManager.getInstance().createLab("").getId();
                },
                "Il metodo createLab non ha lanciato l'eccezione di inserimento di stringa vuota"
        );
        assertThrows(
                DBBadParamaterException.class,
                () -> {
                    id2 = DBManager.getInstance().createLab(null).getId();
                },
                "Il metodo createLab non ha lanciato l'eccezione di inserimento nullo"
        );
        assertThrows(
                DBBadParamaterException.class,
                () -> {
                    id3 = DBManager.getInstance().createLab(" ").getId();
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
            //----------------------------------------------------------------------------------
            id1 = DBManager.getInstance().createLab("Laboratorio di scacchi").getId();
            id3 = DBManager.getInstance().createLab("Laboratorio di videogiochi").getId();
            id4 = DBManager.getInstance().createLab("Laboratorio di programmazione").getId();
            id5 = DBManager.getInstance().createLab("Laboratorio di Balze").getId();
            //----------------------------------------------------------------------------------
            assertThrows(
                    DBUniqueViolationException.class,
                    () -> {
                        id2 = DBManager.getInstance().createLab("Laboratorio di scacchi").getId();
                    },
                    "Il metodo create lab non ha lanciato l'eccezione di vincolo di unicità del nome"
            );

        } catch (DBBadParamaterException ex) {
            assertTrue(false, "Rilevata eccezione: DBBadParamaterException (parametro nullo o empty)");
        }
        ok = true;
    }

    @Test   //fake test
    @DisplayName("[isInstalled()] Test database isInstalled")
    public void test_Alfa7(TestInfo info) {
        message = info.getDisplayName();
        boolean db_installed = DBManager.getInstance().isInstalled();
        Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
        //assertTrue(db_installed, "Il database non è installato!!!!");
        ok = true;

    }

    @Test
    @DisplayName("[getAllLaboratories()] removeToDatabase check into list object removed Test")
    public void test_Alfa8(TestInfo info) {
        try {
            message = info.getDisplayName();
            boolean db_installed = DBManager.getInstance().isInstalled();
            Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
            //----------------------------------------------------------------------------------
            id1 = DBManager.getInstance().createLab("Laboratorio di scacchi").getId();
            id2 = DBManager.getInstance().createLab("Laboratorio di videogiochi").getId();
            id3 = DBManager.getInstance().createLab("Laboratorio di programmazione").getId();
            id4 = DBManager.getInstance().createLab("Laboratorio di Balze").getId();
            //----------------------------------------------------------------------------------
            List<Laboratory> allLaboratories = DBManager.getInstance().getAllLaboratories();
            removeToDatabase(id2);
            boolean foundLab = isFoundLab("Laboratorio di videogiochi");
            //----------------------------------------------------------------------------------
            assertFalse(foundLab, "Mi aspettavo che Laboratorio di videogiochi fosse stato eliminato!");
            id2 = DBManager.getInstance().createLab("Laboratorio di videogiochi").getId();
        } catch (DBUniqueViolationException ex) {
            assertTrue(false, "Rilevata eccezione: DBUniqueViolationException (nome lab uguale a un altro)");

        } catch (DBBadParamaterException ex) {
            assertTrue(false, "Rilevata eccezione: DBBadParamaterException (parametro nullo o empty)");
        }
        ok = true;
    }

    @Test
    @DisplayName("[createLab()] create x4 isLaboratoryValid Test")
    public void test_Alfa9(TestInfo info) {
        try {
            message = info.getDisplayName();
            boolean db_installed = DBManager.getInstance().isInstalled();
            Assumptions.assumeThat(db_installed).withFailMessage("Database non installato").isTrue();
            //----------------------------------------------------------------------------------
            Laboratory lab1 = DBManager.getInstance().createLab("Cucina");
            id1 = lab1.getId();
            Laboratory lab2 = DBManager.getInstance().createLab("Laboratorio di Pittura");
            id2 = lab2.getId();
            Laboratory lab3 = DBManager.getInstance().createLab("Balze");
            id3 = lab3.getId();
            Laboratory lab4 = DBManager.getInstance().createLab("laboratorio di scacchi");
            id4 = lab4.getId();
            Laboratory lab5 = DBManager.getInstance().createLab("laboratorio di cucina");
            id5 = lab5.getId();
            Laboratory lab6 = DBManager.getInstance().createLab("cucina di la boratorio");
            id6 = lab6.getId();
            //---------------------------------------------------------------------------------
            boolean labValid = isLaboratoryValid(lab1.getName());
            boolean labValid2 = isLaboratoryValid(lab2.getName());
            boolean labValid3 = isLaboratoryValid(lab3.getName());
            boolean labValid4 = isLaboratoryValid(lab4.getName());
            boolean labValid5 = isLaboratoryValid(lab5.getName());
            boolean labValid6 = isLaboratoryValid(lab6.getName());
            //----------------------------------------------------------------------------------
            assertFalse(labValid, "Ho inserito un laboratorio sbagliato e me lo ha accettato");
            assertTrue(labValid2, "Ho inserito un laboratorio valido e non me lo ha accettato");
            assertFalse(labValid3, "Ho inserito un laboratorio sbagliato e me lo ha accettato");
            assertFalse(labValid4, "Ho inserito un laboratorio sbagliato e me lo ha accettato");
            assertFalse(labValid5, "Ho inserito un laboratorio sbagliato e me lo ha accettato");
            assertFalse(labValid6, "Ho inserito un laboratorio sbagliato e me lo ha accettato");

        } catch (DBUniqueViolationException ex) {
            assertTrue(false, "Rilevata eccezione: DBUniqueViolationException (nome lab uguale a un altro)");
        } catch (DBBadParamaterException ex) {
            assertTrue(false, "Rilevata eccezione: DBBadParamaterException (parametro nullo o empty)");
        }
        ok = true;

    }

    @Test
    @DisplayName("[getLabIdByName()] create x4 Test idLab")
    public void test_Alfa10(TestInfo info) {
        try {
            message = info.getDisplayName();
            boolean db_install = DBManager.getInstance().isInstalled();
            Assumptions.assumeThat(db_install).withFailMessage("Database non installato").isTrue();
            //----------------------------------------------------------------------------------------
            Laboratory lab1 = DBManager.getInstance().createLab("Laboratorio di scacchi");
            Laboratory lab2 = DBManager.getInstance().createLab("Laboratorio di balze");
            Laboratory lab3 = DBManager.getInstance().createLab("Laboratorio di cucina");
            Laboratory lab4 = DBManager.getInstance().createLab("Laboratorio di arte");
            //-----------------------------------------------------------------------------
            id1 = lab1.getId();
            id2 = lab2.getId();
            id3 = lab3.getId();
            id4 = lab4.getId();
            //-----------------------------------------------------------------------------

            long idLabA = DBManager.getInstance().getLabIdByName("Laboratorio di scacchi");
            long idLabB = DBManager.getInstance().getLabIdByName("Laboratorio di balze");
            long idLabC = DBManager.getInstance().getLabIdByName("Laboratorio di cucina");
            long idLabD = DBManager.getInstance().getLabIdByName("Laboratorio di arte");

            //---------------------------------------------------------------------------------------
            assertEquals(idLabA, id1, "Mi aspettavo che il metodo mi returnasse l'id del laboratorio");
            assertEquals(idLabB, id2, "Mi aspettavo che il metodo mi returnasse l'id del laboratorio");
            assertEquals(idLabC, id3, "Mi aspettavo che il metodo mi returnasse l'id del laboratorio");
            assertEquals(idLabD, id4, "Mi aspettavo che il metodo mi returnasse l'id del laboratorio");

        } catch (DBUniqueViolationException ex) {
            assertTrue(false, "Rilevata eccezione: DBUniqueViolationException (nome lab uguale a un altro)");
        } catch (DBBadParamaterException ex) {
            assertTrue(false, "Rilevata eccezione: DBBadParamaterException (parametro nullo o empty)");
        }
        ok = true;
    }

    @Test
    @DisplayName("[getLabIdByName()] create x4 Test idLab")
    public void test_Alfa11(TestInfo info) {

        try {
            message = info.getDisplayName();
            boolean db_install = DBManager.getInstance().isInstalled();
            Assumptions.assumeThat(db_install).withFailMessage("Database non installato").isTrue();
            //----------------------------------------------------------------------------------------
            Laboratory lab1 = DBManager.getInstance().createLab("Laboratorio di scacchi");
            Laboratory lab2 = DBManager.getInstance().createLab("Laboratorio di balze");
            Laboratory lab3 = DBManager.getInstance().createLab("Laboratorio di cucina");
            Laboratory lab4 = DBManager.getInstance().createLab("Laboratorio di arte");

            //-----------------------------------------------------------------------------
            id1 = DBManager.getInstance().getLabIdByName("Laboratorio di scacchi");
            id2 = DBManager.getInstance().getLabIdByName("Laboratorio di balze");
            id3 = DBManager.getInstance().getLabIdByName("Laboratorio di cucina");
            id4 = DBManager.getInstance().getLabIdByName("Laboratorio di arte");
            id5 = DBManager.getInstance().getLabIdByName("Laboratorio di videogiochi");
            id6 = DBManager.getInstance().getLabIdByName("laboratorio di informatica"); //parametro non valido, dovrebbe returnare -1
            //-----------------------------------------------------------------------------
            boolean findIdLabById = isFindIdLabById(id1);
            boolean findIdLabById2 = isFindIdLabById(id2);
            boolean findIdLabById3 = isFindIdLabById(id3);
            boolean findIdLabById4 = isFindIdLabById(id4);

            //-----------------------------------------------------------------------------
            assertEquals(id1, findIdLabById, "Mi aspettavo che il metodo getLabByIdName mi returnasse l'id corretto");
            assertEquals(id2, findIdLabById2, "Mi aspettavo che il metodo getLabByIdName mi returnasse l'id corretto");
            assertEquals(id3, findIdLabById3, "Mi aspettavo che il metodo getLabByIdName mi returnasse l'id corretto");
            assertEquals(id4, findIdLabById4, "Mi aspettavo che il metodo getLabByIdName mi returnasse l'id corretto");
            assertEquals(id5, -1, "Mi aspettavo che il metodo getLabByIdName mi returnasse -1");
            assertEquals(id6, -1, "Mi aspettavo che il metodo getLabByIdName mi returnasse -1");

            DBManager.getInstance().deleteLaboratory(lab1.getId());
        } catch (DBUniqueViolationException ex) {
            assertTrue(false, "Rilevata eccezione: DBUniqueViolationException (nome lab uguale a un altro)");
        } catch (DBBadParamaterException ex) {
            assertTrue(false, "Rilevata eccezione: DBBadParamaterException (parametro nullo o empty)");
        }
        ok = true;
    }

    @Test
    @DisplayName("[editLab()] create x4 Test editLab correctly ")
    public void test_Alfa12(TestInfo info) {
        try {
            final Laboratory lab1 = DBManager.getInstance().createLab("Laboratorio di scacchi");
            final Laboratory lab2 = DBManager.getInstance().createLab("Laboratorio di balze");
            final Laboratory lab3 = DBManager.getInstance().createLab("Laboratorio di cucina");
            final Laboratory lab4 = DBManager.getInstance().createLab("Laboratorio di arte");
            final Laboratory lab5 = DBManager.getInstance().createLab("Laboratorio di calcio");
            final Laboratory lab6 = DBManager.getInstance().createLab("Laboratorio di sport");
            final Laboratory lab7 = DBManager.getInstance().createLab("Laboratorio di medicina");
            final Laboratory lab8 = DBManager.getInstance().createLab("Laboratorio di testate al muro");
            try {
                message = info.getDisplayName();
                boolean db_install = DBManager.getInstance().isInstalled();
                Assumptions.assumeThat(db_install).withFailMessage("Database non installato").isTrue();
                //----------------------------------------------------------------------------------------               
                Assumptions.assumeThat(lab1).isNotNull();
                Assumptions.assumeThat(lab2).isNotNull();
                Assumptions.assumeThat(lab3).isNotNull();
                Assumptions.assumeThat(lab4).isNotNull();
                Assumptions.assumeThat(lab5).isNotNull();
                Assumptions.assumeThat(lab6).isNotNull();
                Assumptions.assumeThat(lab7).isNotNull();
                Assumptions.assumeThat(lab8).isNotNull();
                //---------------------------------------------------------------------------------------
                id1 = DBManager.getInstance().getLabIdByName("Laboratorio di scacchi");
                id2 = DBManager.getInstance().getLabIdByName("Laboratorio di balze");
                id3 = DBManager.getInstance().getLabIdByName("Laboratorio di cucina");
                id4 = DBManager.getInstance().getLabIdByName("Laboratorio di arte");
                id5 = DBManager.getInstance().getLabIdByName("Laboratorio di calcio");
                id6 = DBManager.getInstance().getLabIdByName("Laboratorio di sport");
                id7 = DBManager.getInstance().getLabIdByName("Laboratorio di medicina");
                id8 = DBManager.getInstance().getLabIdByName("Laboratorio di testate al muro");
                //----------------------------------------------------------------------------------------

                lab1.setName("Laboratorio di nazismo");
                lab2.setName("Laboratorio di sport");
                lab3.setName("Laboratorio di storia");
                lab4.setName("lab di nazibalze");
                lab5.setName("lab di informatica");
                lab6.setName("");
                lab7.setName(null);
                lab8.setName("Laboratorio di sport");
                DBManager.getInstance().editLab(lab1);               
                DBManager.getInstance().editLab(lab8);
                //------------------------------------------------------------
                assertNotEquals(id1, lab1.getName(), "Mi aspettavo che il nome del lab fosse cambiato");
                assertThrows(DBUniqueViolationException.class, () -> {
                    DBManager.getInstance().editLab(lab2);
                });
                assertNotEquals(id3, lab3.getName(), "Mi aspettavo che il nome del lab fosse cambiato");
                assertThrows(DBBadParamaterException.class, () -> {
                    DBManager.getInstance().editLab(lab3);
                });
                assertThrows(DBBadParamaterException.class, () -> {
                    DBManager.getInstance().editLab(lab4);
                });
                assertThrows(DBBadParamaterException.class, () -> {
                    DBManager.getInstance().editLab(lab5);
                });
                assertThrows(DBBadParamaterException.class, () -> {
                    DBManager.getInstance().editLab(lab6);
                });
                assertThrows(DBBadParamaterException.class, () -> {
                    DBManager.getInstance().editLab(lab7);
                });
                assertNotEquals(id8, lab8.getName(), "Mi aspettavo che il nome del lab fosse cambiato");
            } catch (DBUniqueViolationException ex) {
                assertTrue(false, "Rilevata eccezione: DBUniqueViolationException (nome lab uguale a un altro)");
            } catch (DBBadParamaterException ex) {
                assertTrue(false, "Rilevata eccezione: DBBadParamaterException (parametro nullo o empty)");
            } finally {    //il codice all'interno verrà eseguito anche in caso di eccezione
                DBManager.getInstance().deleteLaboratory(lab1.getId());
                DBManager.getInstance().deleteLaboratory(lab2.getId());
                DBManager.getInstance().deleteLaboratory(lab3.getId());
                DBManager.getInstance().deleteLaboratory(lab4.getId());
                DBManager.getInstance().deleteLaboratory(lab5.getId());
                DBManager.getInstance().deleteLaboratory(lab6.getId());
                DBManager.getInstance().deleteLaboratory(lab7.getId());
                DBManager.getInstance().deleteLaboratory(lab8.getId());
            }
            ok = true;

        } catch (DBUniqueViolationException ex) {
            assertTrue(false, "Rilevata eccezione: DBUniqueViolationException (nome lab uguale a un altro)");
        } catch (DBBadParamaterException ex) {
            assertTrue(false, "Rilevata eccezione: DBBadParamaterException (parametro nullo o empty)");
        }
    }
    //cambio nome con null, empty o nome gia in uso;
    //prendere un lab con l'id, modificarlo e riprenderlo con getLabIdById e controllo se il nome è cambiato;

    //metodi fatti a caso pensando fossero utili :(
    public void changeNameLaboratory(Laboratory lab, String newName) throws DBBadParamaterException, DBUniqueViolationException {
        String replaced = lab.getName().replace(lab.getName(), newName);
        if (newName == null || newName.isEmpty() || newName.contains("Laboratorio")) {
            throw new DBBadParamaterException();
        }
        List<Laboratory> allLab = DBManager.getInstance().getAllLaboratories();
        for (Laboratory laboratory : allLab) {
            if (laboratory.getName() == newName) {
                throw new DBBadParamaterException();
            }
        }
        lab.setName(newName);
    }

    public boolean isNameChanged(Laboratory lab, long id) {
        List<Laboratory> allLab = DBManager.getInstance().getAllLaboratories();
        for (Laboratory laboratory : allLab) {
            if (laboratory.getId() == id) {
                if (laboratory.getName() != lab.getName()) {
                    return true;
                }
            }
        }
        return false;
    }

}
