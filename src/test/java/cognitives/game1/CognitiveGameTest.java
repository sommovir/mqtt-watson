/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitives.game1;

import io.moquette.spi.impl.security.ACLFileParser;
import it.cnr.istc.mw.mqtt.logic.logger.LoggerManager;
import it.cnr.istc.mw.mqtt.logic.mindgames.OLD_MindGameEngine;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.Department;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.Product;
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
        boolean check = OLD_MindGameEngine.getInstance().checkDuplicate(prodotti); 
        //assertFalse();
    }
    
   
    @Test
    @DisplayName("[checkEncapsulationDepartment]")
    public void task170(){
        Department department1= new Department(3L,null);
        Department department2= new Department(3L,"");
        Department department3= new Department(3L,"Gianni");
        
        assertEquals("unknown",department1.getName(),"mi aspettavo che il nome fosse unknown");
        assertEquals(3L,department1.getId(),"mi aspettavo che l'id fosse uguale a 3L");
        assertEquals("unknown",department2.getName(),"mi aspettavo che il nome fosse unknown ");
        assertEquals("Gianni",department3.getName(),"mi aspettavo che il nome fosse Gianni");
        
        
        
        
    }
    @Test
    @DisplayName("[checkEncapsulationProduct]")
    public void task167(){
        Product product1= new Product(3L,null);
        Product product2= new Product(3L,"");
        Product product3= new Product(3L,"Gianni");
        
        assertEquals("unknown",product1.getName(),"mi aspettavo che il nome fosse unknown");
        assertEquals(3L,product1.getId(),"mi aspettavo che l'id fosse uguale a 3L");
        assertEquals("unknown",product2.getName(),"mi aspettavo che il nome fosse unknown ");
        assertEquals("Gianni",product3.getName(),"mi aspettavo che il nome fosse Gianni");
        
        
        
        
    }
    
    
}
