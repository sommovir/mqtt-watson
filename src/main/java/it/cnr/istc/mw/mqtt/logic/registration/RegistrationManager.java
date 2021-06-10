/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.registration;

/**
 *
 * @author sommovir
 */
public class RegistrationManager {
    private static RegistrationManager _instance = null;
    
    public static RegistrationManager getInstance() {
        if (_instance == null) {
            _instance = new RegistrationManager();
            return _instance;
        } else {
            return _instance;
        }
    }
    
    private RegistrationManager() {
        super();
    }
    
    
}
