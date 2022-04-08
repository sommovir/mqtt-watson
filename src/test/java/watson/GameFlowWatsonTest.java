/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package watson;

import it.cnr.istc.mw.mqtt.MQTTClient;
import it.cnr.istc.mw.mqtt.WatsonManager;
import it.cnr.istc.mw.mqtt.db.Person;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.SuperMarketInitialState;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author Luca
 */
public class GameFlowWatsonTest {

    public GameFlowWatsonTest() {
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

    private void hackSingleton(Object singletoneMocked, Class<?> clazz) {
        try {
            Field instance = clazz.getDeclaredField("_instance");

            instance.setAccessible(true);
            instance.set(instance, singletoneMocked);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//     private void setMock(DbManager mock) {
//        try {
//            Field instance = DbManager.class.getDeclaredField("_instance");
//            instance.setAccessible(true);
//            instance.set(instance, mock);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    public void testMock() {
//        DbManager manager = mock(DbManager.class);
//        setMock(manager);
//        when(manager.getAllMovies()).thenReturn(null);
//        List<MovieEntity> prova = DbManager.getInstance().getAllMovies();
//        assertNull(prova);
//        //https://stackoverflow.com/questions/38914433/mocking-a-singleton-with-mockito
//    }

    @Test
    @DisplayName("[Gameinstance]")
    public void appText4GameTest() {

        String commandTest = "*face<:>fun,3000<COMMAND>text<:>Perfetto, giochiamo a lista della spesa<COMMAND><GAME>START_GAME<:>CGX001</GAME>";

        Person mockPerson = mock(Person.class);
        try {
            WatsonManager.class.getConstructor().setAccessible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        WatsonManager spyWatsonManager = spy(WatsonManager.class);
        MQTTClient mockMQTTClient = mock(MQTTClient.class);
        //hackSingleton(spyWatsonManager, WatsonManager.class);
        hackSingleton(mockMQTTClient, MQTTClient.class);

        String parsedTest = spyWatsonManager.parseAppText(commandTest, "123");

        ArgumentCaptor<SuperMarketInitialState> captor = ArgumentCaptor.forClass(SuperMarketInitialState.class);

        verify(mockMQTTClient).sendGameData(any(), captor.capture());

        SuperMarketInitialState value = captor.getValue();

        assertEquals("CGX001", value.getGameType().getCode());

        assertEquals("Perfetto, giochiamo a lista della spesa", parsedTest);

    }
}
