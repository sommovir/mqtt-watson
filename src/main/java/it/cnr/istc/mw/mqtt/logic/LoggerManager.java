/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic;

import io.netty.buffer.ByteBufUtil;
import static com.hazelcast.client.impl.protocol.util.UnsafeBuffer.UTF_8;
import it.cnr.istc.mw.mqtt.ConsoleColors;
import java.io.BufferedReader;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
    private boolean notDumping = true;
    private String currentLogPath = null;
    private int numberLine = 0;
    private List<String> cache = new LinkedList<>();
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

    public String getCurrentLogPath() {
        return currentLogPath;
    }
    public boolean isLogging(){
        return currentLogPath != null;
    }
    
    public void newLog(String logfile) {
        
        if (!logActive) {
            return;
        }
        
        userTurns = 0;
        systemTurns = 0;
        totalTurns = 0;
        numberLine = 0;
        if(notDumping){
            cache.clear();
        }
        
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
    
    
    public void openPath(String path){
        File fileLog = new File(path);
        
        Desktop desktop = Desktop.getDesktop();
        
        //first check if Desktop is supported by Platform or not
        if (!Desktop.isDesktopSupported()) {
            System.out.println("Desktop is not supported");
            return;
        }
        
        if (fileLog.exists()) {
            try {
                desktop.open(fileLog);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
        LoggerManager.getInstance().log(" | " + LoggingTag.TOTAL_USER_TURNS.getUndecoratedTag() + ": " + userTurns + " | " + 
                LoggingTag.TOTAL_SYSTEM_TURNS.getUndecoratedTag() + ": " + systemTurns +  " | " + 
                LoggingTag.TOTAL_TURNS.getUndecoratedTag() + ": " + totalTurns +  " | ");
        currentLogPath = null;
        this.startingLoggingTime = -1;
        
    }

    public void log(String textToLog) {
        numberLine++;
        //System.out.println("into log EHYLA' SON DENTRO");
        String timestamp  =  new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date());
        if(notDumping){
            cache.add(numberLine+") "+timestamp + " " + textToLog);
        }
        if (!logActive || currentLogPath == null) {
            return;
        }
        try ( FileWriter fw = new FileWriter(currentLogPath,StandardCharsets.UTF_8, true);
              BufferedWriter bw = new BufferedWriter(fw);
              PrintWriter out = new PrintWriter(bw)
             ) {
            out.println(numberLine+") "+timestamp+" "+textToLog);
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
    
    public void dump(){
        notDumping = false;
        newLog("dump");
        for (String logLine : cache) {
            log(logLine);
        }
        stopLogging();
        notDumping = true;
        cache.clear();
    }

    public void setLogActive(boolean logActive) {
        this.logActive = logActive;
        if(!this.logActive){
            cache.clear();
        }
    }

    public boolean isLogActive() {
        return logActive;
    }

}
