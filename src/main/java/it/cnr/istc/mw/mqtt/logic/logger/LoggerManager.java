/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.logger;

import io.netty.buffer.ByteBufUtil;
import static com.hazelcast.client.impl.protocol.util.UnsafeBuffer.UTF_8;
import it.cnr.istc.mw.mqtt.logic.generals.ConsoleColors;
import it.cnr.istc.mw.mqtt.WatsonManager;
import it.cnr.istc.mw.mqtt.exceptions.InvalidAttemptToLogException;
import it.cnr.istc.mw.mqtt.exceptions.LogOffException;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameResult;
import it.cnr.istc.mw.mqtt.logic.events.LoggerEventListener;
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
    private String adminName = "";
    public static final String LOG_FOLDER = "./logs";
    private boolean currentlyPaused = false;
    private boolean alreadyPaused = false;
    private String lastFileName = null;
    private double sumMediaIntents = 0;
    private double sumMediaEntities = 0;
    private double sumMediaFailedIntents = 0;
    private double totalIntents = 0;
    private double totalEntities = 0;
    private double totalIntentsWithFails = 0;
    private List<LoggerEventListener> loggerListeners = new LinkedList<LoggerEventListener>();
    private boolean adminSetByGui = false; //è true quando il logger admin è settato dalla gui e non dalla console

    public static LoggerManager getInstance() {
        if (_instance == null) {
            _instance = new LoggerManager();

        }
        return _instance;
    }

    public void addLoggerEventListener(LoggerEventListener listener) {
        this.loggerListeners.add(listener);
    }

    public void removeLoggerEventListener(LoggerEventListener listener) {
        this.loggerListeners.remove(listener);
    }

    public void newIntentDetected(double confidence) {
        totalIntents++;
        totalIntentsWithFails++;
        sumMediaIntents += confidence;
        sumMediaFailedIntents += confidence;
    }

    public void newEntitiesDetected(double confidence) {
        totalEntities++;
        sumMediaEntities += confidence;
    }

    public void newFailedIntentDetected(double confidence) {
        totalIntentsWithFails++;
        sumMediaFailedIntents += confidence;
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

    public boolean setLoggerAdmin() {

        BufferedReader reader
                = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(LogTitles.LOGGER.getTitle() + "Inserire il nome del responsabile del log: ");
        try {
            adminName = reader.readLine();
        } catch (IOException ex) {
            System.out.println(LogTitles.SERVER.getTitle() + ex.getMessage());
        }
        if (adminName.isEmpty()) {
            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_RED + "Il nome del responsabile del log non può essere vuoto." + ConsoleColors.ANSI_RESET);
            return false;
        } else {
            try {
                log(LoggingTag.LOGGER_ADMIN.getTag() + adminName);
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.SERVER.getTitle() + ex.getMessage());
            }
            System.out.println(LogTitles.LOGGER.getTitle() + "Responsabile del log registrato a nome di: " + adminName);
            return true;
        }
    }

    public void newLog(String logfile) {

        if (!logActive) {
            return;
        }

        clearAvg();
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
            System.out.println(LogTitles.LOGGER.getTitle() + "path: " + storedFile.getAbsolutePath());
            storedFile.createNewFile(); // if file already exists will do nothing
            FileOutputStream currentLoggingFile = new FileOutputStream(storedFile, false);
            String timestamp = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            log("[Server] START");
            log("del giorno " + timestamp);
            if (!adminSetByGui) {
                boolean nameValid = false;
                do {
                    nameValid = setLoggerAdmin();
                } while (!nameValid);
            }else{
                log(LoggingTag.LOGGER_ADMIN.getTag() + adminName);
                System.out.println(LogTitles.LOGGER.getTitle() + "Responsabile del log registrato a nome di: " + adminName);
            }
            LoggerManager.getInstance().logConfigs();

        } catch (Exception ex) {
            Logger.getLogger(LoggerManager.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }

    public void setAdminSetByGui(boolean adminSetByGui) {
        this.adminSetByGui = adminSetByGui;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminName() {
        return adminName;
    }
    
    
    
    

    public void openPath(String path) {
        
        File fileLog = new File(path);

        Desktop desktop = Desktop.getDesktop();

        //first check if Desktop is supported by Platform or not
        if (!Desktop.isDesktopSupported()) {
            System.out.println(LogTitles.LOGGER.getTitle() + "Desktop is not supported");
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
                + LoggingTag.TOTAL_TURNS.getUndecoratedTag() + ": " + totalTurns + " | \n"
                + LoggingTag.PRECISION_INTENTS.getTag() + ": " + (LoggerManager.getInstance().sumMediaIntents / LoggerManager.getInstance().totalIntents) + " | \n"
                + LoggingTag.PRECISION_FAILED_INTENTS.getTag() + ": " + (LoggerManager.getInstance().sumMediaFailedIntents / LoggerManager.getInstance().totalIntentsWithFails) + " | \n"
                + LoggingTag.PRECISION_ENTITIES.getTag() + ": " + (LoggerManager.getInstance().sumMediaEntities / LoggerManager.getInstance().totalEntities));
        LoggerManager.getInstance().log("----------------------------------------------------------------");
        currentLogPath = null;
        this.startingLoggingTime = -1;
        this.alreadyPaused = false;
        clearAvg();
        logActive = false;
        for (LoggerEventListener loggerListener : loggerListeners) {
            loggerListener.loggingModeChanged(logActive);
        }
    }

    public boolean isAlreadyPaused() {
        return this.alreadyPaused;
    }

    public void clearAvg() {
        sumMediaEntities = 0;
        sumMediaFailedIntents = 0;
        sumMediaIntents = 0;
        totalEntities = 0;
        totalIntents = 0;
        totalIntentsWithFails = 0;
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
                + LoggingTag.TOTAL_TURNS.getUndecoratedTag() + ": " + totalTurns + " | \n"
                + LoggingTag.PRECISION_INTENTS.getTag() + ": " + (LoggerManager.getInstance().sumMediaIntents / LoggerManager.getInstance().totalIntents) + " | \n"
                + LoggingTag.PRECISION_FAILED_INTENTS.getTag() + ": " + (LoggerManager.getInstance().sumMediaFailedIntents / LoggerManager.getInstance().totalIntentsWithFails) + " | \n"
                + LoggingTag.PRECISION_ENTITIES.getTag() + ": " + (LoggerManager.getInstance().sumMediaEntities / LoggerManager.getInstance().totalEntities));
        this.currentlyPaused = true;
        this.alreadyPaused = true;
        this.startingLoggingTime = new Date().getTime();
        this.userTurns = 0;
        this.systemTurns = 0;
        this.totalTurns = 0;
        clearAvg();
    }

    public void resume() {
        this.startingLoggingTime = new Date().getTime();
        this.currentlyPaused = false;
        try {
            LoggerManager.getInstance().log(LoggingTag.END_PRETEST.getTag() + "\n------------------------------------------------------\n \t\tR E A L  T E S T   S T A R T E D\n------------------------------------------------------");
            LoggerManager.getInstance().logConfigs();
        } catch (LogOffException | InvalidAttemptToLogException ex) {
            System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
        }
    }

    private boolean isLoggable(String textLog) {
        return textLog.contains(LoggingTag.NOTE.getUndecoratedTag()) || !currentlyPaused;
    }

    public boolean isPaused() {
        return currentlyPaused;
    }

    //WARNING, any modify to log method must be manually duplicated here
    public void logByGUi(String textToLog, Date initTypingTime) throws LogOffException, InvalidAttemptToLogException {
        if (!isLoggable(textToLog)) {
            throw new InvalidAttemptToLogException("Logging attempt detected, use command [log resume] to renable the logging module"); //seiunbufu
        }
        numberLine++;
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(initTypingTime);

        if (!logActive) {
            throw new LogOffException();
        }

        if (currentLogPath == null) {
            throw new InvalidAttemptToLogException();
        }
        try ( FileWriter fw = new FileWriter(currentLogPath, StandardCharsets.UTF_8, true);  BufferedWriter bw = new BufferedWriter(fw);  PrintWriter out = new PrintWriter(bw)) {

            if (notDumping) {
                cache.add(numberLine + ") " + timestamp + " " + textToLog);
                out.println(numberLine + ") " + timestamp + " " + textToLog);
            } else {
                out.println(textToLog);
            }
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
    
    public void log(String textToLog) throws LogOffException, InvalidAttemptToLogException {
        if (!isLoggable(textToLog)) {
            throw new InvalidAttemptToLogException("Logging attempt detected, use command [log resume] to renable the logging module"); //seiunbufu
        }
        numberLine++;
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());

        if (!logActive) {
            throw new LogOffException();
        }

        if (currentLogPath == null) {
            throw new InvalidAttemptToLogException();
        }
        try ( FileWriter fw = new FileWriter(currentLogPath, StandardCharsets.UTF_8, true);  BufferedWriter bw = new BufferedWriter(fw);  PrintWriter out = new PrintWriter(bw)) {

            if (notDumping) {
                cache.add(numberLine + ") " + timestamp + " " + textToLog);
                out.println(numberLine + ") " + timestamp + " " + textToLog);
            } else {
                out.println(textToLog);
            }
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

    public void logConfigs() {
        try {
            LoggerManager.getInstance().log(LoggingTag.ALPHA.getUndecoratedTag() + ": " + WatsonManager.getInstance().getMinSingleDeltaThreshold()
                    + "       " + LoggingTag.BETA.getUndecoratedTag() + ": " + WatsonManager.getInstance().getMinDeltaThreshold()
                    + "       " + LoggingTag.GAMMA.getUndecoratedTag() + ": " + WatsonManager.getInstance().getMaxDeadlocks());
        } catch (LogOffException | InvalidAttemptToLogException ex) {
            System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
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

    public void fireTestModeChanged(boolean mode) {
        for (LoggerEventListener loggerListener : loggerListeners) {
            loggerListener.testModeChanged(mode);
        }
    }

    public void setLogActive(boolean logActive) {
        if (!logActive) {
            try {
                stopLogging();
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
            }
            cache.clear();
        }
        this.logActive = logActive;
        for (LoggerEventListener loggerListener : loggerListeners) {
            loggerListener.loggingModeChanged(logActive);
        }
    }

    public boolean isLogActive() {
        return logActive;
    }

    /**
     * Restituisce il nome dell'ultimo file di log, completo di estensione
     *
     * @return
     */
    public String getLastFile() {
        return this.lastFileName;
    }
    
    
    public void endGame(GameResult result) throws LogOffException, InvalidAttemptToLogException{
        
        log(LoggingTag.GAME_END.getTag()+" motivation: "+ result.getGameResult());
        
    }
}
