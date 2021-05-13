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
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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
    private long startingLoggingTime = -1;
    private int userTurns = 0;
    private int systemTurns = 0;
    private int totalTurns = 0;

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
        
        userTurns = 0;
        systemTurns = 0;
        totalTurns = 0;
        
        try {
            this.startingLoggingTime = new Date().getTime();
            this.currentLogPath = "./logs/" + logfile + "-" + startingLoggingTime + ".log";
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
    
    /**
     * This method stop the current logging, disabling al further logging until
     * a new command 'new log' will be entered. This method also prints into the
     * log the elapsed time by the corresponding LoggingTag. 
     */
    public void stopLogging(){
        long elapsedTime = new Date().getTime() - this.startingLoggingTime;
        Duration elaps = Duration.of(elapsedTime, ChronoUnit.MILLIS);
        long seconds = elaps.get(ChronoUnit.SECONDS);
        long h = elaps.toHoursPart();
        long m = elaps.toMinutesPart();
        long s = elaps.toSecondsPart();
        LoggerManager.getInstance().log(LoggingTag.ELAPSED_TIME.getTag() + " "+h+"h "+
                m+"m "+
                s+"s ");
        LoggerManager.getInstance().log(" | " + LoggingTag.TOTAL_USER_TURNS.getBlandTag() + ": " + userTurns + " | " + 
                LoggingTag.TOTAL_SYSTEM_TURNS.getBlandTag() + ": " + systemTurns +  " | " + 
                LoggingTag.TOTAL_TURNS.getBlandTag() + ": " + totalTurns +  " | ");
        currentLogPath = null;
        this.startingLoggingTime = -1;
        
    }

    public void log(String textToLog) {
        System.out.println("into log");
        if (!logActive || currentLogPath == null) {
            return;
        }
        try ( FileWriter fw = new FileWriter(currentLogPath, true);
              BufferedWriter bw = new BufferedWriter(fw);
              PrintWriter out = new PrintWriter(bw)
             ) {
            String timestamp  =  new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date());
            out.println(timestamp+" "+textToLog);
            if(textToLog.contains(LoggingTag.SYSTEM_TURNS.getTag())){
                 systemTurns++;
                 totalTurns++;
            }
            else if(textToLog.contains(LoggingTag.USER_TURNS.getTag())){
                userTurns++;
                totalTurns++;
            }
            
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
