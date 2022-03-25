/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.generals;

/**
 *
 * @author sommovir
 */
public enum RobotMovement
{
    UP("up"),
    DOWN("down"),
    LEFT("left"),
    RIGHT("right"),
    STOP("stop"),
    TURN_LEFT("turn_left"),
    TURN_RIGHT("turn_right"),
    UNKNOWN("unknown");

    public static RobotMovement of(String movement){
        switch(movement){
            case "up": return RobotMovement.UP;
            case "down": return RobotMovement.DOWN;
            case "left": return RobotMovement.LEFT;
            case "right": return RobotMovement.RIGHT;
            case "stop": return RobotMovement.STOP;
            case "turn:left": return RobotMovement.TURN_LEFT;
            case "turn_right":return RobotMovement.TURN_RIGHT;
            default: return RobotMovement.UNKNOWN;
        }
    }

    private String robotMovement = "";

    private RobotMovement(String robotMovement) {
        this.robotMovement = robotMovement;
    }

}
