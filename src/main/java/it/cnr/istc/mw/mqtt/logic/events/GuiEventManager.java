/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.events;

/**
 *
 * @author sommovir
 */
public class GuiEventManager {

    private static GuiEventManager _instance = null;

    public static GuiEventManager getInstance() {
        if (_instance == null) {
            _instance = new GuiEventManager();
        }
        return _instance;
    }

    private GuiEventManager() {
        super();
    }

}
