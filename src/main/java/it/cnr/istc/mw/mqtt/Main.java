/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt;

import it.cnr.istc.mw.mqtt.logic.generals.ConsoleColors;
import it.cnr.istc.mw.mqtt.logic.logger.InfoUser;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;
import it.cnr.istc.mw.mqtt.db.DBManager;
import it.cnr.istc.mw.mqtt.db.Person;
import it.cnr.istc.mw.mqtt.exceptions.DBNotExistingException;
import it.cnr.istc.mw.mqtt.exceptions.InvalidAttemptToLogException;
import it.cnr.istc.mw.mqtt.exceptions.LogOffException;
import it.cnr.istc.mw.mqtt.gui.Icons;
import it.cnr.istc.mw.mqtt.gui.LogSupportFrame;
import it.cnr.istc.mw.mqtt.gui.MainFrame;
import it.cnr.istc.mw.mqtt.logic.config.ConfigurationManager;
import it.cnr.istc.mw.mqtt.gui.LoggerAdminDialog;
import it.cnr.istc.mw.mqtt.logic.chad.ChadManager;
import it.cnr.istc.mw.mqtt.logic.google.GoogleDriveManager;
import it.cnr.istc.mw.mqtt.logic.logger.HistoryBook;
import it.cnr.istc.mw.mqtt.logic.logger.HistoryElement;
import it.cnr.istc.mw.mqtt.logic.logger.LogTitles;
import it.cnr.istc.mw.mqtt.logic.logger.LoggerManager;
import it.cnr.istc.mw.mqtt.logic.logger.LoggingTag;
import it.cnr.istc.mw.mqtt.logic.mindgames.game1.GameSuperMarket;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameEngine;
import it.cnr.istc.mw.mqtt.logic.mindgames.models.GameInstance;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.mortbay.util.Scanner;

/**
 *
 * @author Luca
 */
public class Main {

    static MQTTServer server = new MQTTServer();
    public static final String version = "1.1.2"; //refactored log
    private static LogSupportFrame logSupportFrame = null;
    private static MainFrame mainFrame = null;

    public static void suppressLogSupportGUI() {
        logSupportFrame.setVisible(false);
        logSupportFrame.dispose();
        logSupportFrame = null;
    }

    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        FlatDarkLaf.installLafInfo();
        FlatLightLaf.installLafInfo();
        FlatIntelliJLaf.installLafInfo();
        ConfigurationManager.getInstance();
        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        String originalString = "Ciao a tutti come va?";
//
//                        String encryptedString = CryptoManager.getInstance().encrypt(originalString, "1");
//                        String decryptedString = CryptoManager.getInstance().decrypt(encryptedString, "2");

//                        System.out.println(originalString);
//                        System.out.println(encryptedString);
//                        System.out.println(decryptedString);
                        System.out.println(ChadManager.getInstance().getChad());
                        System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "Welcome to Appia Server " + version + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_GREEN + "[Server IP] " + ConsoleColors.CYAN_BRIGHT + MQTTClient.getInstance().getIP() + ConsoleColors.ANSI_RESET);
                        server.start();
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        //server.stop();
                    }
                }
            });

            t.start();
            //Enter data using BufferReader 
            while (true) {
                BufferedReader reader
                        = new BufferedReader(new InputStreamReader(System.in));

                try {
                    // Reading data using readLine 
                    String line = reader.readLine();
                    String[] scripts = line.split("<SCRIPT>");

                    if (scripts.length == 0) {
                        scripts = new String[1];
                        scripts[0] = line;
                    }

                    for (String script : scripts) {
                        line = script;
                        if (line.equals("quit")) {

                            try {
                                LoggerManager.getInstance().stopLogging();
                                LoggerManager.getInstance().log(LogTitles.SERVER.getDecoloredTitle() + "QUIT");
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                            System.out.println(LogTitles.SERVER.getTitle() + "Quitting..");
                            t.interrupt();
                            System.exit(0);
                        } else if (line.startsWith("quit ")) {
                            try {
                                String[] split = line.split(" ");
                                int seconds = Integer.parseInt(split[1]);
                                MQTTClient.getInstance().publish(Topics.EMERGENCY.getTopic(), "Q:" + seconds);

                                for (int i = seconds; i > 0; i--) {
                                    System.out.println(ConsoleColors.ANSI_RED + LogTitles.SERVER.getTitle() + "Server will shut down in " + ConsoleColors.ANSI_YELLOW + i + ConsoleColors.ANSI_RED + " seconds .." + ConsoleColors.ANSI_RESET);
                                    Thread.sleep(1000);
                                }
                                System.out.println(" -- THE FINAL ACT --");
                                System.out.println(LogTitles.SERVER.getTitle() + "Quitting..");
                                try {
                                    LoggerManager.getInstance().stopLogging();
                                    LoggerManager.getInstance().log(LogTitles.SERVER.getDecoloredTitle() + "QUIT");
                                } catch (Exception ex) {
                                    System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                                }
                                t.interrupt();
                                System.exit(0);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println(LogTitles.SERVER.getTitle() + "Quit failed");
                            }
                        } else if (line.equals("list")) {
                            List<InfoUser> on_line = server.getON_LINE();

                            System.out.println(ConsoleColors.ANSI_GREEN + "#  |id\t\t\t\t| timestamp \t\t\t\t| expiration " + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_GREEN + "----------------------------------------------------------------------------------------------" + ConsoleColors.ANSI_RESET);
                            int i = 1;
                            for (InfoUser infoUser : on_line) {
                                String sessionId = WatsonManager.getInstance().getSessionId(infoUser.getId());
                                String expireStatus = WatsonManager.getInstance().getExpireStatus(infoUser.getId());
                                if (infoUser.getId().equals("Server")) {
                                    System.out.println(ConsoleColors.GREEN_BRIGHT + i + ") " + ConsoleColors.ANSI_RED + " " + infoUser.getId() + "\t\t\t" + ConsoleColors.ANSI_GREEN + "| " + ConsoleColors.ANSI_CYAN + infoUser.getTimestamp() + "\t" + ConsoleColors.ANSI_GREEN + "\t" + "| " + ConsoleColors.ANSI_CYAN + expireStatus + ConsoleColors.ANSI_GREEN + ConsoleColors.ANSI_RESET);
                                } else {
                                    System.out.println(ConsoleColors.GREEN_BRIGHT + i + ") " + ConsoleColors.ANSI_CYAN + " " + infoUser.getId() + "[" + MQTTServer.getDeviceType(infoUser.getId()) + "]" + "\t" + ConsoleColors.ANSI_GREEN + "| " + ConsoleColors.ANSI_CYAN + infoUser.getTimestamp() + "\t" + ConsoleColors.ANSI_GREEN + "\t" + "| " + ConsoleColors.ANSI_CYAN + expireStatus + ConsoleColors.ANSI_GREEN + ConsoleColors.ANSI_RESET);
                                }
                                i++;
                            }
                            System.out.println(ConsoleColors.ANSI_GREEN + "---------------------------------------------------------------------------------------------" + ConsoleColors.ANSI_RESET);

                        } else if (line.equals("test")) {
                            WatsonManager.getInstance().sendMessage("ciao", "110");
                        } else if (line.startsWith("!")) {
                            String chattino = line.substring(1, line.length());
                            WatsonManager.getInstance().sendMessage(chattino, "110");
                        } else if (line.equals("test db")) {
                            System.out.println(LogTitles.DATABASE.getTitle() + "testing db");
                            DBManager.getInstance().test();
                        } else if (line.equals("chad")) {
                            String chadFace = ChadManager.getInstance().getChadFace();
                            for (int i = 0; i < chadFace.length(); i++) {
                                System.out.print(chadFace.charAt(i));
                            }
                        } else if (line.equals("log on")) {
                            System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_GREEN + "Logging module is now ACTIVE" + ConsoleColors.ANSI_RESET);
                            LoggerManager.getInstance().setLogActive(true);
                        } else if (line.equals("test on")) {
                            WatsonManager.getInstance().setTestMode(true);
                        } else if (line.equals("test off")) {
                            if (LoggerManager.getInstance().isLogging()) {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "Impossibile interrompere la modalità di test, se è in corso un logging" + ConsoleColors.ANSI_RESET);
                            } else {
                                WatsonManager.getInstance().setTestMode(false);
                            }
                        } else if (line.equals("print context")) {
                            WatsonManager.getInstance().printContext();
                        } else if (line.equals("log off")) {
                            if (LoggerManager.getInstance().isPaused()) {
                                System.out.println(LogTitles.LOGGER.getTitle() + "Il log è momentaneamente in pausa, scrivi chi è che comanda qua se vuoi davvero stoppare durante la pausa: ");
                                String risposta = reader.readLine();
                                if (risposta.equals("Balzdof")) {
                                    System.out.println(LogTitles.LOGGER.getTitle() + "Logging has been deactivated");
                                    LoggerManager.getInstance().setLogActive(false);
                                } else {
                                    System.out.println(LogTitles.LOGGER.getTitle() + "Mi spiace, non sei suddito abbastanza.");
                                }
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + "Vuoi eseguire un dump dei dati di log?");
                                System.out.println(LogTitles.LOGGER.getTitle() + "Digita y per eseguire");
                                System.out.println(LogTitles.LOGGER.getTitle() + "Digita n per continuare");
                                String risposta = reader.readLine();
                                if (risposta.equals("y")) {
                                    System.out.println(LogTitles.LOGGER.getTitle() + "Dumping...");
                                    LoggerManager.getInstance().dump();
                                } else if (risposta.equals("n")) {
                                    System.out.println(LogTitles.LOGGER.getTitle() + "Clearing cache...");
                                } else {
                                    System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_RED + "Errore durante l'interpretazione di un comando (verificare la sintassi)" + ConsoleColors.ANSI_RESET);
                                }
                                System.out.println(LogTitles.LOGGER.getTitle() + "Logging has been deactivated");
                                LoggerManager.getInstance().setLogActive(false);
                            }
                        } else if(line.equals("set topic")){
                            System.out.println("Inserisci Topic:");
                            String topic = reader.readLine();
                            System.out.println("Inserire Messaggio: ");
                            String message = reader.readLine();
                            MQTTClient.getInstance().publish(topic, message);
                            System.out.println("HO PUBBLICATO AL TOPIC: "+topic);
                            System.out.println("HO PUBBLICATO IL MESSAGGIO: "+message);
                        
                        }else if (line.startsWith("new log ")) {
                            String nomeFile = line.split(" ")[2];
                            if (LoggerManager.getInstance().isLogging()) {
                                try {
                                    System.out.println(LogTitles.LOGGER.getTitle() + "Un log è già in esecuzione al momento, vuoi davvero avviare un nuovo log e concludere il precedente? (y/n) ");
                                    String risposta = reader.readLine();
                                    if (risposta.equals("y")) {
                                        System.out.println(LogTitles.LOGGER.getTitle() + "Log precedente concluso. Avvio nuovo log...");
                                        LoggerManager.getInstance().stopLogging();
                                    } else if (risposta.equals("n")) {
                                        System.out.println(LogTitles.LOGGER.getTitle() + "Operazione annullata, log non sovrascritto.(Il log è ancora utilizzabile)");
                                        continue;
                                    } else {
                                        System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "Errore durante l'interpretazione di un comando (verificare la sintassi)" + ConsoleColors.ANSI_RESET);
                                        continue;
                                    }
                                } catch (IOException ex) {
                                    System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + ex.getMessage() + ConsoleColors.ANSI_RESET);
                                }

                            }
                            if (LoggerManager.getInstance().isLogActive()) {
                                LoggerManager.getInstance().newLog(nomeFile);
                                System.out.println(LogTitles.LOGGER.getTitle() + "New log file with name [" + nomeFile + "] has been created");
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + "Vuoi attivare la procedura di logging?");
                                System.out.println(LogTitles.LOGGER.getTitle() + "Digitare y per attivare");
                                System.out.println(LogTitles.LOGGER.getTitle() + "Digitare n per continuare");
                                String answere = reader.readLine();
                                if (answere.equals("y")) {
                                    System.out.println(LogTitles.LOGGER.getTitle() + "Activating logging..");
                                    LoggerManager.getInstance().setLogActive(true);
                                    LoggerManager.getInstance().newLog(nomeFile);
                                    System.out.println(LogTitles.LOGGER.getTitle() + "New log file with name [" + nomeFile + "] has been created");
                                } else if (answere.equals("n")) {
                                    System.out.println(LogTitles.LOGGER.getTitle() + "Impossibile creare il file [" + nomeFile + "]");
                                } else {
                                    System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "Errore durante l'interpretazione di un comando (verificare la sintassi)" + ConsoleColors.ANSI_RESET);
                                }
                            }
                        } else if (line.equals("stop log")) {
                            if (!LoggerManager.getInstance().isLogActive()) {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "Impossibile eseguire quando il log è OFF" + ConsoleColors.ANSI_RESET);
                            } else {
                                String path = LoggerManager.getInstance().getCurrentLogPath();
                                LoggerManager.getInstance().stopLogging();
                                LoggerManager.getInstance().openPath(path);
                                System.out.println(LogTitles.LOGGER.getTitle() + "The current logging file has been closed, no further log will be accepted on such file");
                            }
                        } else if (line.equals("log end pretest")) {
                            if (!LoggerManager.getInstance().isLogActive()) {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "Impossibile eseguire quando il log è OFF" + ConsoleColors.ANSI_RESET);
                            } else if (!LoggerManager.getInstance().isAlreadyPaused()) {
                                LoggerManager.getInstance().pauseLogging();
                                System.out.println(LogTitles.LOGGER.getTitle() + "The pretest marker has been added, no further pretest will be accepted on such file");
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "Impossibile da eseguire di nuovo, pretest già eseguito" + ConsoleColors.ANSI_RESET);
                            }
                        } else if (line.equals("log resume")) {
                            if (LoggerManager.getInstance().isLogActive() && LoggerManager.getInstance().isPaused()) {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_GREEN + "RESUME DONE, THE OFFICIAL TEST IS STARTED" + ConsoleColors.ANSI_RESET);
                                LoggerManager.getInstance().resume();
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "Impossibile eseguire quando il logger non è stato correttamente messo in pausa" + ConsoleColors.ANSI_RESET);
                            }
                        } else if (line.equals("ip") || line.equals("ipconfig") || line.equals("getip")) {
                            System.out.println(LogTitles.SERVER.getTitle() + "The machine's current IP is: " + ConsoleColors.ANSI_CYAN + MQTTClient.getInstance().getIP() + ConsoleColors.ANSI_RESET);
                        } else if (line.equals("watson reset") || line.equals("Watson reset")) {
                            System.out.println(LogTitles.LOGGER.getTitle() + "Inserire RESET per confermare la scelta");
                            String line_ = reader.readLine();
                            if (line_.equals("RESET")) {
                                WatsonManager.getInstance().hardReset();
                                LoggerManager.getInstance().log(LoggingTag.WATSON_HARD_RESET.getTag());
                                System.out.println(LogTitles.LOGGER.getTitle() + "operazione completata");
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + "operazione annullata");
                            }
                        } else if (line.equals("test emotion")) {
                            System.out.println(LogTitles.SERVER.getTitle() + "testing sentiment API");
                            List<String> targets = new LinkedList<>();
                            targets.add("Putin");
                            targets.add("Ukranian president");
                            WatsonManager.getInstance().analyzeEmotionByTarget("we can be disagree if Putin actions are justified or not, but I hope that we all agree that the Ukranian president needs to be wrapped in the main Moscow square by his Russian people anyway this story end, right ? ", targets);
                        } else if (line.equals("log all tags")) {
                            LoggingTag.printAlphabeticOrder();
                        } else if (line.equals("test sentiment")) {
                            System.out.println(LogTitles.SERVER.getTitle() + "testing sentiment API");
                            List<String> targets = new LinkedList<>();
                            targets.add("Putin");
                            targets.add("Ukranian president");
//                            targets.add("Io");
                            WatsonManager.getInstance().analyzeSentimentByTarget("we can be disagree if Putin actions are justified or not, but I hope that we all agree that the Ukranian president needs to be wrapped in the main Moscow square by his Russian people anyway this story end, right ? ", targets);
                        } else if (line.equals("test translate")) {
                            String en = WatsonManager.getInstance().toEnglish("Oggi è stata una giornata cominciata male e finita peggio, e in più mia suocera non mi risponde al telefono");
                            System.out.println(LogTitles.SERVER.getTitle() + "ENGLISH TEXT: " + en);
                        } else if (line.startsWith("face -")) {
                            String[] split = line.split("-");
                            String commandFace = split[1];
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "face command [" + commandFace + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/face", commandFace);

                        } else if (line.equals("test table")) {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "face command [" + "table standard" + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/table", "A<CELL>B<ROW>C<CELL>D");

                        } else if (line.equals("db install")) {

                            if (!DBManager.getInstance().isInstalled()) {
                                try {
                                    DBManager.getInstance().install();
                                } catch (DBNotExistingException e) {
                                    System.out.println(LogTitles.LOGGER.getTitle() + e.getMessage());
                                }
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + "db già installato");
                            }

                        } else if (line.startsWith("test table ")) {
                            String[] split = line.split(" ");
                            String message = split[2];
                            for (int i = 3; i < split.length; i++) {
                                message = message + " " + split[i];
                            }
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "topic: [" + message + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command/table", message);

                        } else if (line.startsWith("secret topics")) {
                            List<String> tid = MQTTServer.topicids;

                            for (String tir : tid) {
                                System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "face command [" + ConsoleColors.ANSI_RED + tir + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            }

                        } else if (line.startsWith("test vtable ")) {
                            String[] split = line.split(" ");
                            String message = split[2];
                            for (int i = 3; i < split.length; i++) {
                                message = message + " " + split[i];
                            }
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "face command [" + message + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command/vtable", message);

                        } else if (line.equals("c -video")) {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "video" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command", "video");

                        } else if (line.equals("c -timg")) {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "test image" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command", "test image");

                        } else if (line.equals("version")) {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_PURPLE + version + ConsoleColors.ANSI_RESET);

                        } else if (line.startsWith("c -img ")) {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "test image " + line.split(" ")[2] + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command", "test image " + line.split(" ")[2]);

                        } else if (line.equals("repeat")) {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "repeat" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command", "repeat");

                        } else if (line.equals("multichoice")) {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "multichoice" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command", "multichoice<:>cosa ti piace di più ?<LIST>piazza,pasta,carne");

                        } else if (line.equals("youtube")) {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "youtube" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command/youtube", "test");

                        } else if (line.startsWith("youtube ")) {
                            String link = line.split(" ")[1];
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "repeat" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command/youtube", link);

                        } else if (line.startsWith("link ")) {
                            String link = line.split(" ")[1];
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "link" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/link", link);

                        } else if (line.equals("history -clear")) {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "history -clear" + ConsoleColors.ANSI_GREEN + "] has been detected" + ConsoleColors.ANSI_RESET);
                            HistoryBook.getInstance().clear();

                        } else if (line.equals("mute")) {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_RED + "WARNING:  all client are now " + ConsoleColors.ANSI_YELLOW + "MUTED" + ConsoleColors.ANSI_RESET);
                            WatsonManager.getInstance().mute();
                        } else if (line.equals("unmute")) {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_RED + "WARNING:  all client are now " + ConsoleColors.ANSI_GREEN + "UNMUTED" + ConsoleColors.ANSI_RESET);
                            WatsonManager.getInstance().unmute();
                        } else if (line.equals("history -all")) {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "history -all" + ConsoleColors.ANSI_GREEN + "] has been detected" + ConsoleColors.ANSI_RESET);
                            List<HistoryElement> history = HistoryBook.getInstance().getHistory();
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            int i = 0;
                            System.out.println(ConsoleColors.ANSI_YELLOW + "-------------- H I S T O R Y --------------" + ConsoleColors.ANSI_RESET);
                            for (HistoryElement historyElement : history) {
                                System.out.println(ConsoleColors.ANSI_RED + i + ")");
                                System.out.println(ConsoleColors.ANSI_GREEN + "time: " + ConsoleColors.ANSI_CYAN + format.format(historyElement.getTimestamp()));
                                System.out.println(ConsoleColors.ANSI_GREEN + "input was: " + ConsoleColors.ANSI_CYAN + historyElement.getInput());
                                System.out.println(ConsoleColors.ANSI_GREEN + "output was: " + ConsoleColors.ANSI_CYAN + historyElement.getOutput());
                                i++;
                            }
                            System.out.println(ConsoleColors.ANSI_YELLOW + "-------------------------------------------" + ConsoleColors.ANSI_RESET);

                        } else if (line.startsWith("history ") && (!line.contains("-")) && (line.split(" ")).length == 2) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "history n" + ConsoleColors.ANSI_GREEN + "] has been detected" + ConsoleColors.ANSI_RESET);
                            String[] split = line.split(" ");
                            int n = Integer.parseInt(split[1]);
                            System.out.println("n= " + n);
                            HistoryElement[] history = HistoryBook.getInstance().getLastElements(n);
                            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                            int i = 0;
                            System.out.println(ConsoleColors.ANSI_YELLOW + "-------------- H I S T O R Y --------------" + ConsoleColors.ANSI_RESET);
                            for (HistoryElement historyElement : history) {
                                System.out.println(ConsoleColors.ANSI_RED + i + ")");
                                System.out.println(ConsoleColors.ANSI_GREEN + "time: " + ConsoleColors.ANSI_CYAN + format.format(historyElement.getTimestamp()));
                                System.out.println(ConsoleColors.ANSI_GREEN + "input was: " + ConsoleColors.ANSI_CYAN + historyElement.getInput());
                                System.out.println(ConsoleColors.ANSI_GREEN + "output was: " + ConsoleColors.ANSI_CYAN + historyElement.getOutput());
                                i++;
                            }
                            System.out.println(ConsoleColors.ANSI_YELLOW + "-------------------------------------------" + ConsoleColors.ANSI_RESET);

                        } else if (line.equals("history")) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "history -10" + ConsoleColors.ANSI_GREEN + "] has been detected" + ConsoleColors.ANSI_RESET);
                            HistoryElement[] history = HistoryBook.getInstance().getLastElements(10);
                            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                            int i = 0;
                            System.out.println(ConsoleColors.ANSI_YELLOW + "-------------- H I S T O R Y --------------" + ConsoleColors.ANSI_RESET);
                            for (HistoryElement historyElement : history) {
                                System.out.println(ConsoleColors.ANSI_RED + i + ")");
                                System.out.println(ConsoleColors.ANSI_GREEN + "time: " + ConsoleColors.ANSI_CYAN + format.format(historyElement.getTimestamp()));
                                System.out.println(ConsoleColors.ANSI_GREEN + "input was: " + ConsoleColors.ANSI_CYAN + historyElement.getInput());
                                System.out.println(ConsoleColors.ANSI_GREEN + "output was: " + ConsoleColors.ANSI_CYAN + historyElement.getOutput());
                                i++;
                            }
                            System.out.println(ConsoleColors.ANSI_YELLOW + "-------------------------------------------" + ConsoleColors.ANSI_RESET);

                        } else if (line.equals("log?")) {
                            if (LoggerManager.getInstance().isLogActive()) {
                                System.out.println(LogTitles.LOGGER.getTitle() + "Logger is currently " + ConsoleColors.ANSI_GREEN + "ON." + ConsoleColors.ANSI_RESET);
                                System.out.println(LogTitles.LOGGER.getTitle() + "Log name: " + ConsoleColors.ANSI_YELLOW + LoggerManager.getInstance().getLogName());
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + "Logger is currently " + ConsoleColors.ANSI_RED + "OFF." + ConsoleColors.ANSI_RESET);
                            }
                            if (LoggerManager.getInstance().isLogging()) {
                                System.out.println(LogTitles.LOGGER.getTitle() + "Logging writing on " + ConsoleColors.ANSI_YELLOW + LoggerManager.getInstance().getLogName() + ConsoleColors.ANSI_RESET + " is currently " + ConsoleColors.ANSI_GREEN + "ON." + ConsoleColors.ANSI_RESET);
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + "Logging writing is currently " + ConsoleColors.ANSI_RED + "OFF." + ConsoleColors.ANSI_RESET);
                            }
                            if (WatsonManager.getInstance().isTestMode()) {
                                System.out.println(LogTitles.LOGGER.getTitle() + "Test mode is currently " + ConsoleColors.ANSI_GREEN + "ON." + ConsoleColors.ANSI_RESET);
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + "Test mode is currently " + ConsoleColors.ANSI_RED + "OFF." + ConsoleColors.ANSI_RESET);
                            }
                            System.out.println(LogTitles.LOGGER.getTitle() + "Alpha: " + ConsoleColors.ANSI_YELLOW + WatsonManager.getInstance().getMinSingleDeltaThreshold() + ConsoleColors.ANSI_RESET + "\nBeta: " + ConsoleColors.ANSI_YELLOW + WatsonManager.getInstance().getMinDeltaThreshold() + ConsoleColors.ANSI_RESET + "\nGamma: " + ConsoleColors.ANSI_YELLOW + WatsonManager.getInstance().getMaxDeadlocks() + ConsoleColors.ANSI_RESET);
                        } else if (line.startsWith("log note ") && !line.replace("log note ", "").isEmpty()) {
                            if (!LoggerManager.getInstance().isLogActive()) {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "Impossibile eseguire quando il log è OFF (per maggiori informazioni consulta help log)" + ConsoleColors.ANSI_RESET);
                            } else {
                                String free_text = line.substring(9, line.length());
                                LoggerManager.getInstance().log(LoggingTag.NOTE.getTag() + " " + free_text);
                                System.out.println(LogTitles.LOGGER.getTitle() + "Note: " + ConsoleColors.ANSI_GREEN_BACKGROUND + "\"" + free_text + "\"");
                                System.out.println(LogTitles.LOGGER.getTitle() + "Note has been added");
                            }
                        } else if (line.equals("log wrong") || line.equals("log w")) {
                            try {
                                LoggerManager.getInstance().log(LoggingTag.WRONG_ANSWER.getTag());
                                System.out.println(LogTitles.LOGGER.getTitle() + "Wrong answer has been logged");
                            } catch (Exception ex) {
                                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                            }

                        } else if (line.startsWith("t -#") && line.split(" ").length > 2) {
                            String[] split = line.split(" ");
                            int idi = Integer.parseInt(split[1].substring(2, split[1].length())) - 1;
                            System.out.println(LogTitles.SERVER.getTitle() + "idi= " + idi);
                            String id = server.getON_LINE().get(idi).getId();
                            String message = split[2];
                            for (int i = 3; i < split.length; i++) {
                                message = message + " " + split[i];
                            }
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_GREEN + "publishing message " + "[" + ConsoleColors.ANSI_PURPLE + message + ConsoleColors.ANSI_GREEN + "] to user-id: " + ConsoleColors.ANSI_GREEN + id + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish(Topics.RESPONSES.getTopic() + "/" + id, message);

                        } else if (line.equals("locate log") || line.equals("log locate") || line.equals("locate logs")) {
                            //String path = StringUtils.substring(LoggerManager.getInstance().getCurrentLogPath(),0,6);
                            LoggerManager.getInstance().openPath(LoggerManager.LOG_FOLDER);
                        } else if (line.equals("log reprompt") || line.equals("log r")) {
                            if (!LoggerManager.getInstance().isLogActive()) {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "Impossibile eseguire quando il log è OFF (per maggiori informazioni consulta help log)" + ConsoleColors.ANSI_RESET);
                            } else {
                                try {
                                    LoggerManager.getInstance().log(LoggingTag.REPROMPT.getTag());
                                    System.out.println(LogTitles.LOGGER.getTitle() + "reprompt eseguito");
                                } catch (Exception e) {
                                    System.out.println(LogTitles.LOGGER.getTitle() + e.getMessage());
                                }
                            }
                        } else if (line.equals("log ws") || line.equals("log wall-speak")) {
                            if (!LoggerManager.getInstance().isLogActive()) {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "Impossibile eseguire quando il log è OFF (per maggiori informazioni consulta help log)" + ConsoleColors.ANSI_RESET);
                            } else {
                                try {
                                    LoggerManager.getInstance().log(LoggingTag.WALL_SPEAK.getTag());
                                    System.out.println(LogTitles.LOGGER.getTitle() + "Wall Speak eseguito");
                                } catch (Exception e) {
                                    System.out.println(LogTitles.LOGGER.getTitle() + e.getMessage());
                                }
                            }
                        } else if (line.equals("log dump") || line.equals("log d")) {
                            if (!LoggerManager.getInstance().isLogActive()) {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "Impossibile eseguire quando il log è OFF" + ConsoleColors.ANSI_RESET);
                            } else {
                                LoggerManager.getInstance().dump();
                                System.out.println(LogTitles.LOGGER.getTitle() + "Dump eseguito correttamente");
                            }
                        } else if (line.equals("upload current log")) {
                            GoogleDriveManager.getInstance().uploadFile(LoggerManager.getInstance().getLastFile());
                            System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_GREEN + "Upload eseguito " + ConsoleColors.ANSI_RESET);
                        } else if (line.equals("clear logs")) {
                            System.out.println(LogTitles.LOGGER.getTitle() + "Inserire DELETE per confermare la scelta");
                            String line_ = reader.readLine();
                            if (line_.equals("DELETE")) {
                                LoggerManager.getInstance().clear_logs();
                                System.out.println(LogTitles.LOGGER.getTitle() + "operazione completata");
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + "operazione annullata");
                            }

                        } else if (line.equals("get alpha")) {
                            System.out.println(LogTitles.LOGGER.getTitle() + "Alpha: MinSingleDeltaTreshold = " + WatsonManager.getInstance().getMinSingleDeltaThreshold());

                        } else if (line.equals("get beta")) {
                            System.out.println(LogTitles.LOGGER.getTitle() + "Beta: MinDeltaTreshold = " + WatsonManager.getInstance().getMinDeltaThreshold());

                        } else if (line.startsWith("set alpha ")) {
                            String[] split = line.split(" ");
                            if (split.length == 3 && split[2].length() > 0 && split[2].matches("[0-1](.[0-9]*)?")) {
                                double alpha = Double.parseDouble(split[2]);
                                WatsonManager.getInstance().setMinSingleDeltaThreshold(alpha);
                                LoggerManager.getInstance().log(LoggingTag.ALPHA.getTag() + " " + alpha);
                                System.out.println(LogTitles.LOGGER.getTitle() + "alpha settata a: " + alpha);
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "controllare sintasssi comando set alpha" + ConsoleColors.ANSI_RESET);
                            }
                        } else if (line.startsWith("set beta ")) {
                            String[] split = line.split(" ");
                            if (split.length == 3 && split[2].length() > 0 && split[2].matches("[0-1](.[0-9]*)?")) {
                                double beta = Double.parseDouble(split[2]);
                                WatsonManager.getInstance().setMinDeltaThreshold(beta);
                                LoggerManager.getInstance().log(LoggingTag.BETA.getTag() + " " + beta);
                                System.out.println(LogTitles.LOGGER.getTitle() + "beta settato a: " + beta);
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "controllare sintasssi comando set beta" + ConsoleColors.ANSI_RESET);
                            }
                        } else if (line.equals("reset config")) {
                            WatsonManager.getInstance().setMinSingleDeltaThreshold(0.6);
                            LoggerManager.getInstance().log(LoggingTag.ALPHA.getTag() + " " + 0.6);
                            WatsonManager.getInstance().setMinDeltaThreshold(0.2);
                            LoggerManager.getInstance().log(LoggingTag.BETA.getTag() + " " + 0.2);
                            WatsonManager.getInstance().setMaxDeadlocks(1);
                            LoggerManager.getInstance().log(LoggingTag.GAMMA.getTag() + " " + 1);
                            System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_GREEN + "Reset eseguito" + ConsoleColors.ANSI_RESET);
                        } else if (line.equals("log gui")) {

                            try {
                                UIManager.setLookAndFeel(new FlatDarkLaf());
                            } catch (Exception ex) {
                                System.out.println(LogTitles.GUI.getTitle() + "Errore, tema FlatDarkLaf non trovato");
                            }

                            String admin = null;

                            do {
                                JFrame frame = new JFrame();
                                frame.setIconImage(Icons.YELLOW_DOT.getIcon().getImage());
                                LoggerAdminDialog dialog = new LoggerAdminDialog(frame, true);
                                dialog.pack();
                                dialog.requestFocus();
                                dialog.setVisible(true);
                                dialog.requestFocusInWindow();
                                dialog.setAlwaysOnTop(true);
                                admin = dialog.getAdmin();
//                                JOptionPane jo = new JOptionPane("Logger Administrator:", JOptionPane.INFORMATION_MESSAGE);
//                                jo.requestFocusInWindow();
//                                JDialog dialog = jo.cre(frame, "Config Admin");
//                                dialog.setVisible(true);
                                //admin = (String) JOptionPane.showInputDialog(frame, "Config Admin", "Logger Administrator:", JOptionPane.INFORMATION_MESSAGE);

                            } while (admin == null || admin.isEmpty());
                            LoggerManager.getInstance().setAdminSetByGui(true);
                            LoggerManager.getInstance().setAdminName(admin);

                            //</editor-fold>

                            /* Create and display the form */
                            java.awt.EventQueue.invokeLater(new Runnable() {
                                public void run() {
                                    if (logSupportFrame == null) {
                                        logSupportFrame = new LogSupportFrame();
                                        logSupportFrame.setVisible(true);
                                    } else {
                                        System.out.println(LogTitles.GUI.getTitle() + ConsoleColors.RED_BRIGHT + "Errore. Log Support Gui è già attiva" + ConsoleColors.ANSI_RESET);
                                    }

                                }
                            });
                        }else if (line.equals("test game1")) {
                            System.out.println("[TEST GAME1] sto per testare l'amore");
                                MQTTClient.getInstance().publish(Topics.MINDGAME.getTopic()+"/demo", "{\n" +
"  \"initialMessage\" : \"Buongiorno, se passi al supermercato mi puoi comprare:  una Bistecca, un pacco di Spaghetti, del  Petto di pollo, una Ciabattina, e infine un barattolo di Ceci\",\n" +
"  \"solutionProducts\" : [ {\n" +
"    \"id\" : 6,\n" +
"    \"name\" : \"Bistecca\",\n" +
"    \"department\" : {\n" +
"      \"id\" : 1,\n" +
"      \"name\" : \"Carne\"\n" +
"    },\n" +
"    \"alternatives\" : \"una\",\n" +
"    \"separatore\" : \",\",\n" +
"    \"UNKNOWN\" : \"unknown\"\n" +
"  }, {\n" +
"    \"id\" : 3,\n" +
"    \"name\" : \"Spaghetti\",\n" +
"    \"department\" : {\n" +
"      \"id\" : 2,\n" +
"      \"name\" : \"Pasta\"\n" +
"    },\n" +
"    \"alternatives\" : \"un pacco di,3 pacchi di\",\n" +
"    \"separatore\" : \",\",\n" +
"    \"UNKNOWN\" : \"unknown\"\n" +
"  }, {\n" +
"    \"id\" : 10,\n" +
"    \"name\" : \"Petto di pollo\",\n" +
"    \"department\" : {\n" +
"      \"id\" : 5,\n" +
"      \"name\" : \"Carne\"\n" +
"    },\n" +
"    \"alternatives\" : \"del \",\n" +
"    \"separatore\" : \",\",\n" +
"    \"UNKNOWN\" : \"unknown\"\n" +
"  }, {\n" +
"    \"id\" : 4,\n" +
"    \"name\" : \"Ciabattina\",\n" +
"    \"department\" : {\n" +
"      \"id\" : 3,\n" +
"      \"name\" : \"Pane\"\n" +
"    },\n" +
"    \"alternatives\" : \"una,una piccola\",\n" +
"    \"separatore\" : \",\",\n" +
"    \"UNKNOWN\" : \"unknown\"\n" +
"  }, {\n" +
"    \"id\" : 5,\n" +
"    \"name\" : \"Ceci\",\n" +
"    \"department\" : {\n" +
"      \"id\" : 4,\n" +
"      \"name\" : \"Legumi \"\n" +
"    },\n" +
"    \"alternatives\" : \"un barattolo di, un po' di\",\n" +
"    \"separatore\" : \",\",\n" +
"    \"UNKNOWN\" : \"unknown\"\n" +
"  } ],\n" +
"  \"request\" : \"Sono nel reparto Carne, che cosa devo prendere ? \",\n" +
"  \"vocalDescription\" : \"Dopo aver elencato, una lista di cose da acquistare al supermercato ,e lasciato il tempo di memorizzarla, ti verrà chiesto di ricordareh cosa bisogna acquistare in base ad uno specifico repartoh\",\n" +
"  \"textualDescription\" : \"Dopo aver elencato una lista di cose da acquistare al supermercato e\\nlasciato il tempo di memorizzarla, chiedere di ricordare cosa bisogna comprare in base ad uno specifico\\nreparto\"\n" +
"}");
                                
                        }         
                         else if (line.equals("test game1 -random")) {
                                GameInstance<GameSuperMarket> instance = GameEngine.getInstance().newGame(new Person(),new GameSuperMarket());
                                MQTTClient.getInstance().sendGameData(new Person(), instance.getInitialState());
                         }
                        else if (line.equals("log gui -white")) {

                            try {
                                UIManager.setLookAndFeel(new FlatIntelliJLaf());
                            } catch (Exception ex) {
                                System.out.println(LogTitles.GUI.getTitle() + "Errore, tema FlatLightLaf non trovato");
                            }

                            //</editor-fold>

                            /* Create and display the form */
                            java.awt.EventQueue.invokeLater(new Runnable() {
                                public void run() {
                                    if (logSupportFrame == null) {
                                        logSupportFrame = new LogSupportFrame();
                                        logSupportFrame.setVisible(true);
                                    } else {
                                        System.out.println(LogTitles.GUI.getTitle() + ConsoleColors.RED_BRIGHT + "Errore. Log Support Gui è già attiva" + ConsoleColors.ANSI_RESET);
                                    }

                                }
                            });
                        } else if (line.startsWith("set gamma ")) {
                            String[] split = line.split(" ");
                            if (split.length == 3 && split[2].length() > 0 && split[2].matches("[0-9]?")) {
                                int gamma = Integer.parseInt(split[2]);
                                WatsonManager.getInstance().setMaxDeadlocks(gamma);
                                System.out.println(LogTitles.LOGGER.getTitle() + "gamma settato a: " + gamma);
                                LoggerManager.getInstance().log(LoggingTag.GAMMA.getTag() + " " + gamma);
                            } else {
                                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "controllare sintasssi comando set gamma" + ConsoleColors.ANSI_RESET);
                            }

                        } else if (line.equals("get gamma")) {
                            System.out.println(LogTitles.LOGGER.getTitle() + "Gamma: MaxDeadlocks = " + WatsonManager.getInstance().getMaxDeadlocks());

                        }else if(line.equals("repeat")){
                            WatsonManager.getInstance().repeat();
                        }else if (line.startsWith("log ws ") && !line.replace("log ws ", "").isEmpty()) {
                            String free_text = line.substring(7, line.length());
                            LoggerManager.getInstance().log(LoggingTag.WALL_SPEAK.getTag() + " " + free_text);
                            System.out.println(LogTitles.LOGGER.getTitle() + "Note has been added");

                        } else if (line.equals("log extra")) {
                            LoggerManager.getInstance().log(LoggingTag.EXTRA_INPUT.getTag());
                            System.out.println(LogTitles.LOGGER.getTitle() + "extra has been added");
                        } else if (line.startsWith("log extra ") && !line.replace("log extra ", "").isEmpty()) {
                            String free_text = line.substring(10, line.length());
                            LoggerManager.getInstance().log(LoggingTag.EXTRA_INPUT.getTag() + " " + free_text);
                            System.out.println(LogTitles.LOGGER.getTitle() + "Extra has been added");

                        } else if (line.equals("log wrong input") || line.equals("log wi")) {
                            LoggerManager.getInstance().log(LoggingTag.WRONG_INPUT.getTag());
                            System.out.println(LogTitles.LOGGER.getTitle() + "Wrong input tag has been added");

                        } else if ((line.startsWith("log wrong input ") && !line.replace("log wrong input ", "").isEmpty()) || (line.startsWith("log wi ") && !line.replace("log wi ", "").isEmpty())) {

                            String free_text = line.startsWith("log wrong input ") ? line.split(" ")[3] : line.split(" ")[2];
                            LoggerManager.getInstance().log(LoggingTag.WRONG_INPUT.getTag() + " " + free_text);
                            System.out.println(LogTitles.LOGGER.getTitle() + "Wrong input tag has been added");
                        } else if (line.equals("main gui")) {

                            try {
                                UIManager.setLookAndFeel(new FlatDarkLaf());
                            } catch (Exception ex) {
                                System.out.println(LogTitles.GUI.getTitle() + "Errore, tema FlatDarkLaf non trovato");
                            }

                            java.awt.EventQueue.invokeLater(new Runnable() {
                                public void run() {
                                    new MainFrame().setVisible(true);
                                }
                            });

                        } else if (line.equals("main gui -white")) {

                            try {
                                UIManager.setLookAndFeel(new FlatIntelliJLaf());
                            } catch (Exception ex) {
                                System.out.println(LogTitles.GUI.getTitle() + "Errore, tema FlatLightLaf non trovato");
                            }

                            java.awt.EventQueue.invokeLater(new Runnable() {
                                public void run() {
                                    new MainFrame().setVisible(true);
                                }
                            });

                        } else if (line.equals("help")) {

                            //</editor-fold>

                            /* Create and display the form */
                            java.awt.EventQueue.invokeLater(new Runnable() {
                                public void run() {
                                    if (mainFrame == null) {
                                        mainFrame = new MainFrame();
                                        mainFrame.setVisible(true);
                                    } else {
                                        System.out.println(LogTitles.GUI.getTitle() + ConsoleColors.RED_BRIGHT + "Errore. La MainFrame GUI è già attiva" + ConsoleColors.ANSI_RESET);
                                    }

                                }
                            });
                        } else if (line.equals("help")) {

                            System.out.println(ConsoleColors.ANSI_GREEN + "------------------------- H E L P -----------------------------" + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_WHITE + "List of commands:" + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_YELLOW + "1) " + ConsoleColors.ANSI_CYAN + "quit");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tForza la chiusura del server");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "2) " + ConsoleColors.ANSI_CYAN + "quit [seconds]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tForza la chiusura del server impostando un timeout in secondi");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "\tEs. " + ConsoleColors.ANSI_WHITE + "quit 10 (dopo 10 secondi il server verrà disconnesso)");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "3) " + ConsoleColors.ANSI_CYAN + "list");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tVisualzza la lista degli utenti attualmente connessi");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "4) " + ConsoleColors.ANSI_CYAN + "test");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tManda al servizio Watson il testo 'ciao' e visualizza la risposta");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "5) " + ConsoleColors.ANSI_CYAN + "test db");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tSi connette al database, esegue due inserimenti e visualizza il contenuto della tabella Persona");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "6) " + ConsoleColors.ANSI_CYAN + "face -[fun|love|sad|cry|question|rage]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tcomanda l'esecuzione di una determinata animazione face.");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "\tEs. " + ConsoleColors.ANSI_WHITE + "face -cry (comanda l'esecuzione della face 'cry')");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "7) " + ConsoleColors.ANSI_CYAN + "test table");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tcomanda l'esecuzione di una tabella standard 2x2: A<CELL>B<ROW>C<CELL>D ");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tin alternativa si può far seguire dopo il comando il testo di una tabella da far eseguire");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "\tEs. " + ConsoleColors.ANSI_WHITE + "test table Laboraratorio di cucina<CELL>10:30<ROW>Laboratorio di Filatelia<CELL>19:00");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "7) " + ConsoleColors.ANSI_CYAN + "test vtable [table]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tcomanda l'esecuzione di una tabella standard generica ad esempio: A<CELL>B<CELL>C<ROW>D<CELL>D<CELL>E<ROW>F<CELL>G<CELL>H");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tNota: la prima riga verrà presa come header");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "8) " + ConsoleColors.ANSI_CYAN + "![testo libero]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tinvia come input il testo che segue il !");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "\tEs. " + ConsoleColors.ANSI_WHITE + "!che ore sono? (invia il testo 'che ore sono?'");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "9) " + ConsoleColors.ANSI_CYAN + "c -video");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tesegue un video di test interno alla app");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "10) " + ConsoleColors.ANSI_CYAN + "c -timg");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tesegue un caricamento dal drive di un immagine di test con link embeddato");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "11) " + ConsoleColors.ANSI_CYAN + "c -img [link]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tinvia al cellulare il comando per visualizzare a schermo un immagine esterna con il link dato in argomento");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "12) " + ConsoleColors.ANSI_CYAN + "repeat");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tfa ripetere l'ultimo messaggio ricevuto");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "\tEs. " + ConsoleColors.ANSI_WHITE + "c -img https://drive.google.com/uc?export=view&id=10xKwmTRVEVyi_b3jpgLvx1ovGf9qhnX8");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "13) " + ConsoleColors.ANSI_CYAN + "t -#[id] [message]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tinvia un messagge all'utente con id = numero intero che appare a sinistra dell'utente dal comando list.");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "\tEs. " + ConsoleColors.ANSI_WHITE + "t -#2 ciao come stai ? (manda all'utente 2 il messaggo 'ciao come stai ?'");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "14) " + ConsoleColors.ANSI_CYAN + "youtube [link]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tvisualizza a schermo il video linkato (link puro generato dal pulsante share di youtube)");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "\tEs. " + ConsoleColors.ANSI_WHITE + "youtube https://youtu.be/krBsto5nUps");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "15) " + ConsoleColors.ANSI_CYAN + "history [n]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tstampa la lista degli ultimi n comandi eseguiti");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "\tNota: " + ConsoleColors.ANSI_WHITE + "se viene eseguito il comando senza parametri verranno listate le ultime 10");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "\tNota: " + ConsoleColors.ANSI_WHITE + "per eseguire la lista completa si può eseguire il comando 'history -all'");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "\tNota: " + ConsoleColors.ANSI_WHITE + "per cancellare la cronologia digitare il comando: 'history -clear'");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "16) " + ConsoleColors.ANSI_CYAN + "link [link]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tvisualizza il link passato in argomento");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "17) " + ConsoleColors.ANSI_CYAN + "test on");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tviene attivata la modalità di test");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "17) " + ConsoleColors.ANSI_CYAN + "test off");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tviene disattivata la modalità di test");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "18) " + ConsoleColors.ANSI_CYAN + "mute");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tmuta il client");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "19) " + ConsoleColors.ANSI_CYAN + "unmute");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tsmuta il client");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "20) " + ConsoleColors.ANSI_CYAN + "ip / ipconfig / getip ");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tmostra l'ip della macchina corrente");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "21) " + ConsoleColors.ANSI_CYAN + "watson reset ");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tuna volta eseguito questo comando tutte le variabili di contesto nella chatbot saranno completamente resettate per tutti gli utenti connessi");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "22) " + ConsoleColors.ANSI_CYAN + "set alpha");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tInserire un valore compreso tra 0 e 1");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "23) " + ConsoleColors.ANSI_CYAN + "get alpha");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tMostra il valore di alpha");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "24) " + ConsoleColors.ANSI_CYAN + "set beta");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tInserire un valore compreso tra 0 e 1");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "25) " + ConsoleColors.ANSI_CYAN + "get beta");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tMostra il valore di beta");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "26) " + ConsoleColors.ANSI_CYAN + "reset config");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tporta a valore di default alpha, beta e gamma");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "27) " + ConsoleColors.ANSI_CYAN + "set gamma");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tInserire n volte che un oytput può essere ripetuto");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "28) " + ConsoleColors.ANSI_CYAN + "get gamma");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tMostra il valore di gamma");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "29) " + ConsoleColors.ANSI_CYAN + "db install");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tInstalla il db");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "30) " + ConsoleColors.ANSI_CYAN + "repeat");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tRipete la risposta precedente");
                            System.out.println(ConsoleColors.ANSI_GREEN + "----------------------------------------------------------------" + ConsoleColors.ANSI_RESET);
                        } else if (line.equals("help log")) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "------------------------- H E L P  L O G-----------------------------" + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_WHITE + "List of log commands:" + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_YELLOW + "1) " + ConsoleColors.ANSI_CYAN + "log on");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tAbilita il sistema di logging");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "2) " + ConsoleColors.ANSI_CYAN + "log off");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tDisabilita il sistema di logging");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "3) " + ConsoleColors.ANSI_CYAN + "log?");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tFornisce informazioni sul sistema di logging");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "4) " + ConsoleColors.ANSI_CYAN + "new log [nomefile]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tScriverà un file [nomefile].log dentro la cartella ./logs");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "5) " + ConsoleColors.ANSI_CYAN + "log reprompt / log r");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tScriverà nel file [nomefile].log dentro la cartella ./logs <REPROMPT>");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "6) " + ConsoleColors.ANSI_CYAN + "log wrong / log w");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tScriverà nel file [nomefile].log dentro la cartella ./logs il tag <WRONG ANSWER>");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "7) " + ConsoleColors.ANSI_CYAN + "log note [text]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tsalva il [text] dentro il file nomefile.log con il tag <NOTE>");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "8) " + ConsoleColors.ANSI_CYAN + "log dump / log d");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tDa usare in caso di problemi con il nomefile.log, crea un nuovo filenome.log con tutte le azioni effettuate");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "9) " + ConsoleColors.ANSI_CYAN + "stop log ");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tchiuderà il file nomefile.log con il tempo e il totale delle azioni eseguite dall'applicazione e dall'utente");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "10) " + ConsoleColors.ANSI_CYAN + "upload current log ");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tcaricherà su Google Drive il log corrente");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "11) " + ConsoleColors.ANSI_CYAN + "locate log ");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\taprirà la cartella dove si trovano i log");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "12) " + ConsoleColors.ANSI_CYAN + "clear logs ");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\teliminerà tutti i log nella cartella locale");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "13) " + ConsoleColors.ANSI_CYAN + "log end pretest ");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\ttermina la fase di pretest, azzerando il time elapsed, i system/user/total turns, inserendo un divisore nel log e mettendo in pausa il log");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "14) " + ConsoleColors.ANSI_CYAN + "log resume ");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\triprende il funzionamento del log dopo la fine del pretest");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "15) " + ConsoleColors.ANSI_CYAN + "log ws / log wall-speak / log ws [text]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tLogga quando l'utente parla con l'assistente senza premere il tasto, per appuntare una nota scrivere log ws [nota da aggiungere]");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "16) " + ConsoleColors.ANSI_CYAN + "log gui");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tAvvia la gui per l'annotazione dei log tramite un'altra finestra. Con l'opzione " + ConsoleColors.ANSI_YELLOW + "-white" + ConsoleColors.ANSI_WHITE + " si può avere il thema chiaro");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "17) " + ConsoleColors.ANSI_CYAN + "log extra / log extra [note]");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tl'utente chiede dele cosa extra rispetto alla frase stabilita, possibile aggiungere cosa extra log extra [extra]");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "17) " + ConsoleColors.ANSI_CYAN + "server gui");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tApre la finestra relativa alla gestione degli utenti.Con l'opzione " + ConsoleColors.ANSI_YELLOW + "-white" + ConsoleColors.ANSI_WHITE + " si può avere il thema chiaro");
                            System.out.println(ConsoleColors.ANSI_GREEN + "----------------------------------------------------------------" + ConsoleColors.ANSI_RESET);
                        } else if (line.equals("help log tag")) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "------------------------- H E L P    L O G    T A G-----------------------------" + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_WHITE + "List and description of log tags:" + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_YELLOW + "1) " + ConsoleColors.ANSI_CYAN + "ELAPSED TIME:");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica il tempo intercorso fra l'inizio e la fine del logging");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "2) " + ConsoleColors.ANSI_CYAN + "SYSTEM TURNS:");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica il momento in cui sta rispondendo l'assistente virtuale Watson");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "3) " + ConsoleColors.ANSI_CYAN + "TOTAL SYSTEM TURNS");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tFornisce il numero di interventi totali dell'assistente virtuale Watson dall'inizio alla fine del logging");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "4) " + ConsoleColors.ANSI_CYAN + "USERN TURNS");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica il momento in cui sta rispondendo l'utente");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "5) " + ConsoleColors.ANSI_CYAN + "TOTAL USER TURNS");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tFornisce il numero di interventi totali dell'utente dall'inizio alla fine del logging");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "6) " + ConsoleColors.ANSI_CYAN + "TOTAL TURNS");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tFornisce il numero di interventi totali dell'utente e dell'assistente dall'inizio alla fine del logging");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "7) " + ConsoleColors.ANSI_CYAN + "TIMEOUT");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale l'utente non risponde nel tempo atteso dopo aver iniziato la registrazione");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "8) " + ConsoleColors.ANSI_CYAN + "REJECTS");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale l'assistente Watson risponde 'non ho capito'");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "9) " + ConsoleColors.ANSI_CYAN + "REPROMPT");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale, a parità di campo semantico, non si ottiene una risposta immediata e l'utente è costretto a riformulare la richiesta con una diversa forma");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "10) " + ConsoleColors.ANSI_CYAN + "NOANSWER");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale l'assistente Watson non risponde in alcun modo alla richiesta dell'utente");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "11) " + ConsoleColors.ANSI_CYAN + "NO USER ANSWER");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale quando l'assistente Watson chiede all'utente di rispondere e attiva automaticamente l'ascolto ma l'utente non risponde");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "12) " + ConsoleColors.ANSI_CYAN + "CANCEL");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale l'utente interrompe la richiesta");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "13) " + ConsoleColors.ANSI_CYAN + "NOTE");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'inserimento di una nota all'interno del log");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "14) " + ConsoleColors.ANSI_CYAN + "BARGEINS");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale viene interrotta manualmente la sintesi vocale dell'assistente Watson");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "15) " + ConsoleColors.ANSI_CYAN + "USER CONNECTED");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica la connessione dell'utente al server");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "16) " + ConsoleColors.ANSI_CYAN + "USER DISCONNECTED");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica la disconnessione dell'utente dal server");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "17) " + ConsoleColors.ANSI_CYAN + "FACE");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale viene comandato un cambio emotivo dell'interfaccia utente tramite l'assistente Watson");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "18) " + ConsoleColors.ANSI_CYAN + "TABLE");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale viene mostrata una tabella all'utente tramite l'assistente Watson");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "19) " + ConsoleColors.ANSI_CYAN + "VIDEO");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale viene mostrato un video all'utente tramite l'assistente Watson");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "20) " + ConsoleColors.ANSI_CYAN + "IMG");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale viene mostrata un'immagine all'utente tramite l'assistente Watson");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "21) " + ConsoleColors.ANSI_CYAN + "LINK");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale viene associato un link alla risposta dell'assistente Watson");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "22) " + ConsoleColors.ANSI_CYAN + "CHANGE USERNAME");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale l'utente cambia username");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "23) " + ConsoleColors.ANSI_CYAN + "WRONG ANSWER");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale il sistema risponde in maniera semanticamente errata rispoetto all'input in arrivo");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "24) " + ConsoleColors.ANSI_CYAN + "REC BUTTON PRESSED BY USER");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tQuanto l'utente preme il bottone per iniziare la registrazione audio viene generato questo evento.");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "25) " + ConsoleColors.ANSI_CYAN + "POSITIVE ANSWER");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale l'utente risponde in maniera affermativa");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "26) " + ConsoleColors.ANSI_CYAN + "NEGATIVE ANSWER");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale l'utente risponde in maniera negativa");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "27) " + ConsoleColors.ANSI_CYAN + "BYPASS");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica quando viene bypassato il sistema di risposta di watson per via di un sovraccarico di variabili di contesto");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "28) " + ConsoleColors.ANSI_CYAN + "REPEAT");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale l'utente preme il pulsante sull'applicazione per far ripetere la risposta dell'assistente Watson.");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "29) " + ConsoleColors.ANSI_CYAN + "END PRETEST");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale durante il log finisce il pretest e viene messo in pausa");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "30) " + ConsoleColors.ANSI_CYAN + "SPEAK");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica quando l’utente preme il bottone per dialogare con l’assistente Watson");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "31) " + ConsoleColors.ANSI_CYAN + "WATSON HARD RESET");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica quando viene effettuato un reset  manuale di tutte le variabili di contesto per tutti gli utenti connessi");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "32) " + ConsoleColors.ANSI_CYAN + "CONFIDENCE INTENTS");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tRappresenta la lista degli intenti trovati da Watson Assistant e il loro rispettivo grado di confidence");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "33) " + ConsoleColors.ANSI_CYAN + "CONFIDENCE ENTITIES");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tRappresenta la lista delle entities trovate da Watson Assistant e il loro rispettivo grado di confidence");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "34) " + ConsoleColors.ANSI_CYAN + "PRECISION INTENTS");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tOffre due valori, il primo rappresenta la media di tutte le confidence degli intents selezionati per rispondere all'utenti, il secondo valore è come il primo ma che tiene conto anche di tutti i casi dove non si è riuscito a rispondere correttamente, usando il valore 0 per i <REJECTS> e il valori bassi trovati nei casi di <BYPASS> e <LOW DELTA>");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "35) " + ConsoleColors.ANSI_CYAN + "ALPHA");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tDefinisce la soglia minima dell’intent con il più alto valore di confidence per essere accettato dalla chatbot");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "36) " + ConsoleColors.ANSI_CYAN + "BETA");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tDefinisce la differenza minima tra gli intents a più alta confidence");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "37) " + ConsoleColors.ANSI_CYAN + "LOW DELTA");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica ogni qualvolta arrivano vari intents con un delta (differenza tra le rispettive confidence) tra di loro inferiore a una soglia decisa da parametro.");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "38) " + ConsoleColors.ANSI_CYAN + "WALL SPEAK");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica l'evento nel quale l'utente parla con l'assistente senza premere il tasto.");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "39) " + ConsoleColors.ANSI_CYAN + "GAMMA");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIndica il numero massimo di deadlocks nei nodi di Watson, superato questo valore il server attuerà un hard reset.");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "40) " + ConsoleColors.ANSI_CYAN + "LOGGER ADMIN");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIl tag esplicita il nome del responsabile del server al momento del logging.");
                            System.out.println(ConsoleColors.ANSI_YELLOW + "40) " + ConsoleColors.ANSI_CYAN + "DEVICE");
                            System.out.println(ConsoleColors.ANSI_WHITE + "\tIl tag mostra il dipo di dispositivo connesso che può essere MOBILE, TV, ROBOT o sconosciuto");

                            System.out.println(ConsoleColors.ANSI_GREEN + "----------------------------------------------------------------" + ConsoleColors.ANSI_RESET);
                        } else {
                            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_RED + "Errore, comando sconosciuto. (digita help per conoscere i comandi in uso)" + ConsoleColors.ANSI_RESET);
                        }
                    }
                } catch (LogOffException | InvalidAttemptToLogException ex) {
//                    ex.printStackTrace();
                    System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + ex.getMessage() + ConsoleColors.ANSI_RESET);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_RED + "Errore durante l'interpretazione di un comando (verificare la sintassi)" + ConsoleColors.ANSI_RESET);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
