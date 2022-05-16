/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cognitives.game1;

import it.cnr.istc.mw.mqtt.db.Person;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.GameSuperMarket;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.Product;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

/**
 *
 * @author sommovir
 */
public class GameSuperMarketTest {
    
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
    public void generateProductsTest() {
        
        GameSuperMarket game = new GameSuperMarket();
        
        List<Product> list0 = game.generateProducts(0);
        List<Product> listMinus1 = game.generateProducts(-1);
        List<Product> listMinus40 = game.generateProducts(-40);
        List<Product> list1 = game.generateProducts(1);
//        List<Product> list3 = game.generateProducts(3);
//        List<Product> list10 = game.generateProducts(10);
        
        Assertions.assertNull(list0);
        Assertions.assertNull(listMinus1);
        Assertions.assertNull(listMinus40);
        Assertions.assertEquals(1,list1.size());
    
    }
}
