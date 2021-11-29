/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.generals;

/**
 *
 * @author Federico
 */
public enum RobotMovements {
    MOVE_FORWARD("MOVE_FORWARD"), 
    MOVE_BACKWARD("MOVE_BACKWARD"), 
    TURN_LEFT("TURN_LEFT"), 
    TURN_RIGHT("TURN_RIGHT"),
    AGAIN("AGAIN"),
    UNKNOWN("UNKNOWN");
    
    
    private String movement;

    private RobotMovements(String moviment) {
        this.movement = moviment;
    }

    public String getMovement() {
        return movement;
    }
    
    public static RobotMovements of (String movement){
        
        switch(movement){
            case "MOVE_FORWARD": return RobotMovements.MOVE_FORWARD;
            case "MOVE_BACKWARD": return RobotMovements.MOVE_BACKWARD;
            case "TURN_LEFT": return RobotMovements.TURN_LEFT;
            case "TURN_RIGHT": return RobotMovements.TURN_RIGHT;
            case "AGAIN": return RobotMovements.AGAIN;
            default: return RobotMovements.UNKNOWN;
        }
    }
}
