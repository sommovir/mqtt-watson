/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

import it.cnr.istc.mw.mqtt.db.DBManager;
import it.cnr.istc.mw.mqtt.db.Person;
import it.cnr.istc.mw.mqtt.exceptions.MindGameException;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.GameSuperMarket;
import java.util.List;
import org.jboss.jandex.TypeTarget;

/**
 *
 * @author Luca
 */
public class GameEngine {

    private static GameEngine _instance = null;

    public static GameEngine getInstance() {
        if (_instance == null) {
            _instance = new GameEngine();
        }
        return _instance;
    }

    private GameEngine() {
        super();
    }

    public GameInstance newGame(Person user, MindGame mindGame) throws InvalidGameInstanceException, MindGameException {
        GameDifficultyPolicy policy = getPolicy(user, mindGame);
        switch (policy) {
            case AUTO:
                //utilizziamo il deepLearning per generare una Game Instance
                GameInstance instance = generateDeepLearningGI(user, mindGame);
                if (validateGameInstance(instance)) {
                    return instance;
                }else{
                    throw new InvalidGameInstanceException();
                }

            default:
                return null;
        }

    }

    /**
     * Genera un istanza di un nuovo gioco a seconda dell'utente che sta giocando
     * e del mindgame selezionato, tenendo conto dello storico della persona. 
     * @param user
     * @param mindgame
     * @return 
     */
    private GameInstance generateDeepLearningGI(Person user, MindGame mindgame) throws MindGameException {
        //generate il livello
        GameDifficulty difficulty = calculateDifficultyByAI(user, mindgame);
        
        InitialState initialState  = mindgame.generateInitialState(difficulty);
        Solution solution = initialState.getSolution();
        
        boolean  valid = mindgame.validate(initialState,solution); //qua deve sparare eccezioni nel caso che..
        
        
        return new GameInstance(initialState, solution);
    }
    
    /**
     * Genera la difficoltà del prossimo game a seconda del modulo di Machine
     * Learning che tiene traccia dello storico delle ultime 5 partite. 
     * @param user
     * @param mindgame
     * @return 
     */
    private <G extends MindGame>GameDifficulty calculateDifficultyByAI(Person user, G mindgame){
        List<GameInstance<G>> gameInstances = DBManager.getInstance().getLast5GameInstances(user,mindgame);
        //INSERT DEEP LEARNING
        return GameDifficulty.Facile;
    }

    private boolean validateGameInstance(GameInstance gameInstance) {
        return true;
    }

    /**
     * Ritorna la corrente policy dato un utente e il gioco selezionato
     *
     * @param user
     * @param mindGame
     * @return
     */
    private GameDifficultyPolicy getPolicy(Person user, MindGame mindGame) {
        return null;
    }

}
