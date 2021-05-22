/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic;

import io.netty.buffer.ByteBufUtil;
import static com.hazelcast.client.impl.protocol.util.UnsafeBuffer.UTF_8;
import it.cnr.istc.mw.mqtt.ConsoleColors;
import it.cnr.istc.mw.mqtt.exceptions.InvalidAttemptToLogException;
import it.cnr.istc.mw.mqtt.exceptions.LogOffException;
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
    private String logName;
    public static final String LOG_FOLDER = "./logs";
    private boolean currentlyPaused = false;
    private boolean alreadyPaused = false;
    private String lastFileName = null;

    public static LoggerManager getInstance() {
        if (_instance == null) {
            _instance = new LoggerManager();

        }
        return _instance;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    private LoggerManager() {
        super();
    }

    public String getCurrentLogPath() {
        return currentLogPath;
    }

    public boolean isLogging() {
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
        if (notDumping) {
            cache.clear();
        }

        try {
            setLogName(logfile);
            this.startingLoggingTime = new Date().getTime();
            this.currentLogPath = LOG_FOLDER + "/" + logfile + "-" + startingLoggingTime + ".log";
            this.lastFileName = this.currentLogPath;
            File storedFile = new File(currentLogPath);
            storedFile.getParentFile().mkdirs();
            System.out.println("path: " + storedFile.getAbsolutePath());
            storedFile.createNewFile(); // if file already exists will do nothing
            FileOutputStream currentLoggingFile = new FileOutputStream(storedFile, false);
            String timestamp = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            log("[Server] START");
            log("del giorno " + timestamp);

        } catch (Exception ex) {
            Logger.getLogger(LoggerManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }

    public void openPath(String path) {
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
    public void stopLogging() throws LogOffException, InvalidAttemptToLogException {
        currentlyPaused = false;
        long elapsedTime = new Date().getTime() - this.startingLoggingTime;
        Duration elaps = Duration.of(elapsedTime, ChronoUnit.MILLIS);
        long seconds = elaps.get(ChronoUnit.SECONDS);
        long h = elaps.toHoursPart();
        long m = elaps.toMinutesPart();
        long s = elaps.toSecondsPart();
        LoggerManager.getInstance().log("----------------------------------------------------------------");
        LoggerManager.getInstance().log(LoggingTag.ELAPSED_TIME.getTag() + " " + h + "h "
                + m + "m "
                + s + "s ");
        LoggerManager.getInstance().log(" | " + LoggingTag.TOTAL_USER_TURNS.getUndecoratedTag() + ": " + userTurns + " | "
                + LoggingTag.TOTAL_SYSTEM_TURNS.getUndecoratedTag() + ": " + systemTurns + " | "
                + LoggingTag.TOTAL_TURNS.getUndecoratedTag() + ": " + totalTurns + " | ");
        LoggerManager.getInstance().log("----------------------------------------------------------------");
        currentLogPath = null;
        this.startingLoggingTime = -1;
        this.alreadyPaused = false;
        //System.out.println("GASHAHYAHAHAHAHAHAJSHSKJHSJHJKHSKJSH");
    }

    public boolean isAlreadyPaused(){
        return this.alreadyPaused;
    }
    
    public void pauseLogging() throws LogOffException, InvalidAttemptToLogException {
        long elapsedTime = new Date().getTime() - this.startingLoggingTime;
        Duration elaps = Duration.of(elapsedTime, ChronoUnit.MILLIS);
        long seconds = elaps.get(ChronoUnit.SECONDS);
        long h = elaps.toHoursPart();
        long m = elaps.toMinutesPart();
        long s = elaps.toSecondsPart();
        LoggerManager.getInstance().log(LoggingTag.END_PRETEST.getTag() + "\n------------------------------------------------------\n \t\tP R E T E S T   E N D E D\n------------------------------------------------------");
        LoggerManager.getInstance().log(LoggingTag.ELAPSED_TIME.getTag() + " " + h + "h "
                + m + "m "
                + s + "s ");
        LoggerManager.getInstance().log(" | " + LoggingTag.TOTAL_USER_TURNS.getUndecoratedTag() + ": " + userTurns + " | "
                + LoggingTag.TOTAL_SYSTEM_TURNS.getUndecoratedTag() + ": " + systemTurns + " | "
                + LoggingTag.TOTAL_TURNS.getUndecoratedTag() + ": " + totalTurns + " | ");
        this.currentlyPaused = true;
        this.alreadyPaused = true;
        this.startingLoggingTime = new Date().getTime();
        this.userTurns = 0;
        this.systemTurns = 0;
        this.totalTurns = 0;
    }

    public void resume() {
        this.startingLoggingTime = new Date().getTime();
        this.currentlyPaused = false;
        try {
            LoggerManager.getInstance().log(LoggingTag.END_PRETEST.getTag() + "\n------------------------------------------------------\n \t\tR E A L  T E S T   S T A R T E D\n------------------------------------------------------");
        } catch (LogOffException | InvalidAttemptToLogException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private boolean isLoggable(String textLog) {
        return textLog.contains(LoggingTag.NOTE.getUndecoratedTag()) || !currentlyPaused;
    }

    public boolean isPaused() {
        return currentlyPaused;
    }

    public void log(String textToLog) throws LogOffException, InvalidAttemptToLogException {
        if (!isLoggable(textToLog)) {
            //System.out.println("Loggin attempt detected, use command [log resume] to renable the logging module");
            throw new InvalidAttemptToLogException("Logging attempt detected, use command [log resume] to renable the logging module"); //seiunbufu
        }
        numberLine++;
        //System.out.println("into log EHYLA' SON DENTRO");
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        if (notDumping) {
            cache.add(textToLog);
        }
        if (!logActive) {
            //System.out.println("Throw LogOffException");
            throw new LogOffException();
        }

        if (currentLogPath == null) {
            //System.out.println("Throw InvalidAttemptToLogException");
            throw new InvalidAttemptToLogException();
        }
        try ( FileWriter fw = new FileWriter(currentLogPath, StandardCharsets.UTF_8, true);  BufferedWriter bw = new BufferedWriter(fw);  PrintWriter out = new PrintWriter(bw)) {
            out.println(numberLine + ") " + timestamp + " " + textToLog);
            if (textToLog.contains(LoggingTag.SYSTEM_TURNS.getTag()) || textToLog.contains(LoggingTag.REJECTS.getTag())) {
                systemTurns++;
                totalTurns++;
            } else if (textToLog.contains(LoggingTag.USER_TURNS.getTag())) {
                userTurns++;
                totalTurns++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void dump() throws LogOffException, InvalidAttemptToLogException {
        notDumping = false;
        newLog("dump");
        for (String logLine : cache) {
            log(logLine);
        }
        stopLogging();
        notDumping = true;
        cache.clear();
    }

    public void clear_logs() {
        File folder = new File(LoggerManager.LOG_FOLDER);
        File[] files = folder.listFiles();

        for (File file : files) {

            if (file.isFile()) {
                file.delete();
            }
        }

    }

    public void setLogActive(boolean logActive) {
        if (!logActive) {
            try {
                stopLogging();
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(ex.getMessage());            } 
            cache.clear();
        }
        this.logActive = logActive;
    }

    public boolean isLogActive() {
        return logActive;
    }

    /**
     * Restituisce il nome dell'ultimo file di log, completo di estensione 
     * @return 
     */
    public String getLastFile() {
        return this.lastFileName;
    }

}
