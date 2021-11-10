/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

import it.cnr.istc.mw.mqtt.db.Person;

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

    public GameInstance newGame(Person user, MindGame mindGame) throws InvalidGameInstanceException {
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

    private GameInstance generateDeepLearningGI(Person user, MindGame mindgame) {
        //generate il livello
        GameDifficulty difficulty = calculateDifficulty(user, mindgame);
        
        InitialState initialState  = mindgame.generateInitialState(difficulty);
        Solution solution = initialState.getSolution();
        boolean  valid = mindgame.validate(initialState,solution);
        
        return new GameInstance(initialState, solution);
    }
    
    private GameDifficulty calculateDifficulty(Person user, MindGame mindgame){
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
