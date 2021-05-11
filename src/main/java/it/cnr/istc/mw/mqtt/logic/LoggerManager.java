/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * init logging prova discord
 *
 * @author sommovir
 */
public class LoggerManager {

    private static LoggerManager _instance = null;
    private boolean logActive = false;
    private FileOutputStream currentLoggingFile = null;

    public static LoggerManager getInstance() {
        if (_instance == null) {
            _instance = new LoggerManager();

        }
        return _instance;
    }

    private LoggerManager() {
        super();
    }

    public void newLog(String logfile) {
        if (!logActive) {
            return;
        }

        try {
            if (currentLoggingFile != null) {
                this.currentLoggingFile.close();
            }
            File storedFile = new File("./logs/"+logfile + "-" + (new Date().getTime()) + ".log");
            storedFile.getParentFile().mkdirs();
            System.out.println("path: "+storedFile.getAbsolutePath());
            storedFile.createNewFile(); // if file already exists will do nothing
            currentLoggingFile = new FileOutputStream(storedFile, false);
            
        } catch (Exception ex) {
            Logger.getLogger(LoggerManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }

    public void log(String textToLog) {
        if (!logActive) {
            return;
        }

    }

    public void setLogActive(boolean logActive) {
        this.logActive = logActive;
    }

    public boolean isLogActive() {
        return logActive;
    }

}
