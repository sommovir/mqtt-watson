/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * init logging prova discord 2 Ciao Luca!
 *
 * @author sommovir
 */
public class LoggerManager {

    private static LoggerManager _instance = null;
    private boolean logActive = false;
    private String currentLogPath = null;

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
            this.currentLogPath = "./logs/" + logfile + "-" + (new Date().getTime()) + ".log";
            File storedFile = new File(currentLogPath);
            storedFile.getParentFile().mkdirs();
            System.out.println("path: " + storedFile.getAbsolutePath());
            storedFile.createNewFile(); // if file already exists will do nothing
            FileOutputStream currentLoggingFile = new FileOutputStream(storedFile, false);
            log("[Server] START");

        } catch (Exception ex) {
            Logger.getLogger(LoggerManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }

    public void log(String textToLog) {
        if (!logActive || currentLogPath == null) {
            return;
        }
        try ( FileWriter fw = new FileWriter(currentLogPath, true);
              BufferedWriter bw = new BufferedWriter(fw);
              PrintWriter out = new PrintWriter(bw)
             ) {
            String timestamp  =  new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date());
            out.println(timestamp+" "+textToLog);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setLogActive(boolean logActive) {
        this.logActive = logActive;
    }

    public boolean isLogActive() {
        return logActive;
    }

}
