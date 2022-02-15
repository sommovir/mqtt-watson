/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitives.game1;

import io.moquette.spi.impl.security.ACLFileParser;
import it.cnr.istc.mw.mqtt.exceptions.ProductDuplicateException;
import it.cnr.istc.mw.mqtt.exceptions.TooFewRepartsExceptions;
import it.cnr.istc.mw.mqtt.logic.logger.LoggerManager;
import it.cnr.istc.mw.mqtt.logic.mindgames.OLD_MindGameEngine;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.Department;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.Product;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.SuperMarketSolution;
import java.lang.ProcessHandle.Info;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.function.Executable;

/**
 *
 * @author sommovir
 */
//@Disabled
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
    @DisplayName("[checkEncapsulationDepartment]")
    public void task170() {
        Department department1 = new Department(3L, null);
        Department department2 = new Department(3L, "");
        Department department3 = new Department(3L, "Gianni");

        assertEquals("unknown", department1.getName(), "mi aspettavo che il nome fosse unknown");
        assertEquals(3L, department1.getId(), "mi aspettavo che l'id fosse uguale a 3L");
        assertEquals("unknown", department2.getName(), "mi aspettavo che il nome fosse unknown ");
        assertEquals("Gianni", department3.getName(), "mi aspettavo che il nome fosse Gianni");

    }

    @Test
    @DisplayName("[checkEncapsulationProduct]")
    public void task167() {
        Product product1 = new Product(3L, null);
        Product product2 = new Product(3L, "");
        Product product3 = new Product(3L, "Gianni");

        assertEquals("unknown", product1.getName(), "mi aspettavo che il nome fosse unknown");
        assertEquals(3L, product1.getId(), "mi aspettavo che l'id fosse uguale a 3L");
        assertEquals("unknown", product2.getName(), "mi aspettavo che il nome fosse unknown ");
        assertEquals("Gianni", product3.getName(), "mi aspettavo che il nome fosse Gianni");

    }

    @Test
    @DisplayName("[checkEqualsProduct]")
    public void task168() {
        Product product1 = new Product(5L, "Giorgio");
        Product product2 = new Product(5L, "Giorgio");
        Product product3 = new Product(6L, "Giorgio");
        Product product4 = new Product(5L, "Giorgiogr");

        assertTrue(product1.equals(product2), "mi aspettavo true ma l'Equals mi ha ritornato false");
        assertFalse(product1.equals(product3), "mi aspettavo false ma l'Equals mi ha ritornato true");
        assertFalse(product1.equals(product4), "mi aspettavo false ma l'Equals mi ha ritornato true");
        assertFalse(product1.equals(null), "mi aspettavo false ma l'Equals mi ha ritornato true (ti ho passato null)");
        assertFalse(product1.equals(new Object()), "mi aspettavo false ma l'Equals mi ha ritornato true (ti ho passato new Object())");

    }

    @Test
    @DisplayName("[checkEqualsDepartment]")
    public void task171() {
        Department department1 = new Department(5L, "Giorgio");
        Department department2 = new Department(5L, "Giorgio");
        Department department3 = new Department(6L, "Giorgio");
        Department department4 = new Department(5L, "Giorgiogr");

        assertTrue(department1.equals(department2), "mi aspettavo true ma l'Equals mi ha ritornato false");
        assertFalse(department1.equals(department3), "mi aspettavo false ma l'Equals mi ha ritornato true");
        assertFalse(department1.equals(department4), "mi aspettavo false ma l'Equals mi ha ritornato true");
        assertFalse(department1.equals(null), "mi aspettavo false ma l'Equals mi ha ritornato true (ti ho passato null)");
        assertFalse(department1.equals(new Object()), "mi aspettavo false ma l'Equals mi ha ritornato true (ti ho passato new Object())");
    }

    @Test
    @Tag("MIND-G1-#98")
    public void task98() {
        List<Product> listaSbudellata = new LinkedList<Product>();
        listaSbudellata.add(new Product(1, "ciao", new Department(2, "libri"), "casa"));
        listaSbudellata.add(new Product(3, "no", new Department(4, "cassa"), "mamma"));
        listaSbudellata.add(new Product(1, "ciao", new Department(2, "libri"), "casa"));
        listaSbudellata.add(new Product(5, "lol", new Department(6, "sippe"), "dove"));
        List<Product> listaGiusta = new LinkedList<>();
        listaGiusta.add(new Product(1, "ciao", new Department(2, "libri"), "casa"));
        listaGiusta.add(new Product(3, "no", new Department(4, "cassa"), "mamma"));
        listaGiusta.add(new Product(5, "lol", new Department(6, "sippe"), "dove"));
        listaGiusta.add(new Product(7, "dimmi", new Department(6, "sippe"), "ecco"));
        List<Product> listaVuota = new LinkedList<>();
        List<Product> listaNulla = new LinkedList<>();
        listaNulla.add(new Product(3, null, new Department(2, null), null));
        listaNulla.add(new Product(3, null, new Department(2, null), null));

        SuperMarketSolution solution = new SuperMarketSolution(listaSbudellata);
        SuperMarketSolution solution1 = new SuperMarketSolution(listaGiusta);
        SuperMarketSolution solution2 = new SuperMarketSolution(listaVuota);
//        SuperMarketSolution solution3 = new SuperMarketSolution(listaNulla);

        assertThrows(ProductDuplicateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                solution.checkDuplicate();
            }
        });

        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                solution1.checkDuplicate();
            }
        });
        
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                solution2.checkDuplicate();
            }
        });
//        assertDoesNotThrow(new Executable() {
//            @Override
//            public void execute() throws Throwable {
//                solution3.checkDuplicate();
//            }
//        });


    }
    @Test
    @DisplayName("[checkReparts]")
    public void task101(){
        
        assertThrows(TooFewRepartsExceptions.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                SuperMarketSolution s =new SuperMarketSolution(null);
                s.checkReparts();
            }
        }, "mi aspettavo che lanciasse un'eccezione");
        assertThrows(TooFewRepartsExceptions.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                SuperMarketSolution s =new SuperMarketSolution(new LinkedList<>());
                s.checkReparts();
            }
        }, "mi aspettavo che lanciasse un'eccezione");
        assertThrows(TooFewRepartsExceptions.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                List<Product> lista = new LinkedList<Product>();
                lista.add(new Product(1,"cipolle"));
                lista.add(new Product(2,"ciaociao"));
                
                
                SuperMarketSolution s =new SuperMarketSolution(lista);
                
                s.checkReparts();
            }
        }, "mi aspettavo che lanciasse un'eccezione");
        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                List<Product> lista = new LinkedList<Product>();
                lista.add(new Product(1,"cipolle"));
                lista.add(new Product(2,"ciaociao"));
                lista.add(new Product(3,"quellodelbug"));
                
                
                
                
                SuperMarketSolution s =new SuperMarketSolution(lista);
                
                s.checkReparts();
            }
        }, "mi aspettavo che non lanciasse un'eccezione");
        
        
        
       
    }
    

}
