/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package logger;

import it.cnr.istc.mw.mqtt.gui.LogSupportFrame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author sommovir
 */
public class LoggerTest {

    public LoggerTest() {
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
    public void test1() {
//        LogSupportFrame loggerSupportFrameMOCK = mock(LogSupportFrame.class);
        //loggerSupportFrameMOCK.isValidFileName("asdasd");
        LogSupportFrame frame = new LogSupportFrame();
        
        assertTrue(frame.isValidFileName("asdasd"));
        assertTrue(frame.isValidFileName("asda223sd"));
        assertFalse(frame.isValidFileName("1asdasd"));
        assertFalse(frame.isValidFileName(""));
        assertFalse(frame.isValidFileName(null));
        //when(loggerSupportFrameMOCK.getName()).thenReturn("Carlo");

    }
}
