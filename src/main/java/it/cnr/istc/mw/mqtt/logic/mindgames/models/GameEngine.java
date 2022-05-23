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
    }

    /**
     * Genera un istanza di un nuovo gioco a seconda della policy utilizzata
     *
     * @param <G>
     * @param user utente che sta iniziando la partita
     * @param mindGame il tipo di gioco selezionato
     * @return L'istanza di gioco comprensiva della sua soluzione
     * @throws MindGameException Eccezione che viene lanciata quando subentra un
     * errore durante la creazione del MindGame
     */
    public <G extends MindGame> GameInstance<G> newGame(Person user, G mindGame) throws MindGameException {
        GameDifficultyPolicy policy = getPolicy(user, mindGame);
        if(user == null || mindGame == null){
            System.out.println("NULLIFORMI :");
            System.out.println("user: "+user);
            System.out.println("mindgame: "+mindGame);
            throw new MindGameException() {
                @Override
                public String errorMessage() {
                    return "I DATI SONO NULL";
                }
            };
        }
        switch (policy) {
            case AUTO:
            {
                //utilizziamo il deepLearning per generare una Game Instance
                GameInstance instance = generateDeepLearningGI(user, mindGame);
                if (validateGameInstance(instance)) {
                    return instance;
                } else {
                    throw new InvalidGameInstanceException();
                }
            }
            case DECREASING:
            {
                return null;
            }
            case RAISING:
            {
                return null;
            }
            case RANDOM:
            {
                return null;
            }               
            default:
                return null;
        }

    }

    /**
     * Genera un istanza di un nuovo gioco secondo la policy della difficoltà automatica(AUTO)
     *
     * @param user utente che sta iniziando la partita
     * @param mindGame il tipo di gioco selezionato
     * @return un istanza di gioco
     */
    private GameInstance generateDeepLearningGI(Person user, MindGame mindgame) throws MindGameException {
        //generate il livello
        GameDifficulty difficulty = calculateDifficultyByAI(user, mindgame);
        InitialState initialState = mindgame.generateInitialState(difficulty);
        Solution solution = initialState.getSolution();
        
        boolean valid = mindgame.validate(initialState); //qua deve sparare eccezioni nel caso che..
        return new GameInstance(initialState, user, mindgame, GameResult.NOT_FINISHED);
    }

    /**
     * Genera la difficoltà del prossimo game a seconda del modulo di Machine
     * Learning che tiene traccia dello storico delle ultime 5 partite.
     *
     * @param user utente che sta iniziando la partita
     * @param mindGame il tipo di gioco selezionato
     * @return la difficoltà calcolata dal deeplearning
     */
    private <G extends MindGame> GameDifficulty calculateDifficultyByAI(Person user, G mindgame) {
        List<GameInstance<G>> gameInstances = DBManager.getInstance().getLast5GameInstances(user, mindgame);
        //INSERT DEEP LEARNING
        return GameDifficulty.Facile;
    }

    /**
     * valida l'istanza di gioco
     * @param gameInstance instanza di gioco
     * @return true se l'istanza è valida false viceversa
     */
    private boolean validateGameInstance(GameInstance gameInstance) {
        return true;
    }

    /**
     * Ritorna la corrente policy dato un utente e il gioco selezionato
     *
     * @param user utente di gioco
     * @param mindGame il tipo di gioco selezionato
     * @return la modalità con cui viene cambiata la difficoltà del gioco
     * selezionato
     */
    private GameDifficultyPolicy getPolicy(Person user, MindGame mindGame) {
        return GameDifficultyPolicy.AUTO;
    }

}
