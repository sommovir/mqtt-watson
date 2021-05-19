/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt;

import it.cnr.istc.mw.mqtt.db.DBManager;
import it.cnr.istc.mw.mqtt.logic.GoogleDriveManager;
import it.cnr.istc.mw.mqtt.logic.HistoryBook;
import it.cnr.istc.mw.mqtt.logic.HistoryElement;
import it.cnr.istc.mw.mqtt.logic.LoggerManager;
import it.cnr.istc.mw.mqtt.logic.LoggingTag;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.AnsiConsole;

/**
 *
 * @author Luca
 */
public class Main {

    static MQTTServer server = new MQTTServer();
    public static final String version = "1.0.1 LOG VERSION";

    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String originalString = "Ciao a tutti come va?";

                        String encryptedString = CryptoManager.getInstance().encrypt(originalString, "1");
                        String decryptedString = CryptoManager.getInstance().decrypt(encryptedString, "2");

                        System.out.println(originalString);
                        System.out.println(encryptedString);
                        System.out.println(decryptedString);
                        System.out.println(ConsoleColors.ANSI_RED + "[Server]" + ConsoleColors.ANSI_GREEN + "Welcome to Appia Server " + version + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_GREEN + "[Server IP] " + ConsoleColors.ANSI_PURPLE + MQTTClient.getInstance().getIP() + ConsoleColors.ANSI_RESET);
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
                            LoggerManager.getInstance().stopLogging();
                            LoggerManager.getInstance().log("[Server] QUIT");
                            System.out.println("[Server] Quitting..");
                            t.interrupt();
                            System.exit(0);
                        } else if (line.startsWith("quit ")) {
                            try {
                                String[] split = line.split(" ");
                                int seconds = Integer.parseInt(split[1]);
                                MQTTClient.getInstance().publish(Topics.EMERGENCY.getTopic(), "Q:" + seconds);

                                for (int i = seconds; i > 0; i--) {
                                    System.out.println(ConsoleColors.ANSI_RED + "[Server] Server will shut down in " + ConsoleColors.ANSI_YELLOW + i + ConsoleColors.ANSI_RED + " seconds .." + ConsoleColors.ANSI_RESET);
                                    Thread.sleep(1000);
                                }
                                System.out.println(" -- THE FINAL ACT --");
                                System.out.println("[Server] Quitting..");
                                LoggerManager.getInstance().stopLogging();
                                LoggerManager.getInstance().log("[Server] QUIT");
                                t.interrupt();
                                System.exit(0);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println("[server] quit failed");
                            }
                        } else if (line.equals("list")) {
                            List<InfoUser> on_line = server.getON_LINE();

                            System.out.println(ConsoleColors.ANSI_GREEN + "#  |id\t\t\t| timestamp \t\t\t\t| expiration " + ConsoleColors.ANSI_RESET);
                            System.out.println(ConsoleColors.ANSI_GREEN + "--------------------------------------------------------------------------" + ConsoleColors.ANSI_RESET);
                            int i = 1;
                            for (InfoUser infoUser : on_line) {
                                String sessionId = WatsonManager.getInstance().getSessionId(infoUser.getId());
                                String expireStatus = WatsonManager.getInstance().getExpireStatus(infoUser.getId());
                                if (infoUser.getId().equals("Server")) {
                                    System.out.println(ConsoleColors.GREEN_BRIGHT + i + ") " + ConsoleColors.ANSI_RED + " " + infoUser.getId() + "\t\t" + ConsoleColors.ANSI_GREEN + "| " + ConsoleColors.ANSI_CYAN + infoUser.getTimestamp() + "\t" + ConsoleColors.ANSI_GREEN + "\t" + "| " + ConsoleColors.ANSI_CYAN + expireStatus + ConsoleColors.ANSI_GREEN + ConsoleColors.ANSI_RESET);
                                } else {
                                    System.out.println(ConsoleColors.GREEN_BRIGHT + i + ") " + ConsoleColors.ANSI_CYAN + " " + infoUser.getId() + "\t" + ConsoleColors.ANSI_GREEN + "| " + ConsoleColors.ANSI_CYAN + infoUser.getTimestamp() + "\t" + ConsoleColors.ANSI_GREEN + "\t" + "| " + ConsoleColors.ANSI_CYAN + expireStatus + ConsoleColors.ANSI_GREEN + ConsoleColors.ANSI_RESET);
                                }
                                i++;
                            }
                            System.out.println(ConsoleColors.ANSI_GREEN + "--------------------------------------------------------------------------" + ConsoleColors.ANSI_RESET);

                        } else if (line.equals("test")) {
                            WatsonManager.getInstance().sendMessage("ciao", "110");
                        } else if (line.startsWith("!")) {
                            String chattino = line.substring(1, line.length());
                            WatsonManager.getInstance().sendMessage(chattino, "110");
                        } else if (line.equals("test db")) {
                            System.out.println("testing db");
                            DBManager.getInstance().test();
                        } else if (line.equals("log on")) {
                            System.out.println(ConsoleColors.ANSI_GREEN +"Logging module is now ACTIVE"+ConsoleColors.ANSI_RESET);
                            LoggerManager.getInstance().setLogActive(true);
                        } else if (line.equals("test on")) {
                            System.out.println(ConsoleColors.ANSI_GREEN +"Testing procedure is now ACTIVE, it will be allowed only "+ConsoleColors.ANSI_RED +"1"+ConsoleColors.ANSI_GREEN +" connection"+ConsoleColors.ANSI_RESET);
                            WatsonManager.getInstance().setTestMode(true);
                        }else if (line.equals("test off")) {
                            System.out.println(ConsoleColors.ANSI_GREEN +"Testing procedure is now OFF"+ConsoleColors.ANSI_RESET);
                            WatsonManager.getInstance().setTestMode(false);
                        }else if (line.equals("log off")) {
                            System.out.println("Vuoi eseguire un dump dei dati di log?");
                            System.out.println("Digita y per eseguire");
                            System.out.println("Digita n per continuare");
                            String risposta = reader.readLine();
                            if (risposta.equals("y")) {
                                System.out.println("Dumping...");
                                LoggerManager.getInstance().dump();
                            } else if (risposta.equals("n")) {
                                System.out.println("Clearing cache...");
                            } else {
                                System.out.println(ConsoleColors.ANSI_RED + "[Server] Errore durante l'interpretazione di un comando (verificare la sintassi)"  + ConsoleColors.ANSI_RESET);
                            }
                            System.out.println("Deactivating logging..");
                            LoggerManager.getInstance().setLogActive(false);
                        } else if (line.startsWith("new log ")) {
                            String nomeFile = line.split(" ")[2];
                            if (LoggerManager.getInstance().isLogging()) {
                                try {
                                    System.out.println("Un log è già in esecuzione al momento, vuoi davvero avviare un nuovo log e concludere il precedente? (y/n) ");
                                    String risposta = reader.readLine();
                                    if (risposta.equals("y")) {
                                        System.out.println("Log precedente concluso. Avvio nuovo log...");
                                        LoggerManager.getInstance().stopLogging();
                                    } else if (risposta.equals("n")) {
                                        System.out.println("Operazione annullata, log non sovrascritto.(Il log è ancora utilizzabile)");
                                        continue;
                                    } else {
                                        System.out.println(ConsoleColors.ANSI_RED + "[Server] Errore durante l'interpretazione di un comando (verificare la sintassi)" + ConsoleColors.ANSI_RESET);
                                        continue;
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                            }
                            if (LoggerManager.getInstance().isLogActive()) {
                                LoggerManager.getInstance().newLog(nomeFile);
                                System.out.println("New log file with name [" + nomeFile + "] has been created");
                            } else {
                                System.out.println("Vuoi attivare la procedura di logging?");
                                System.out.println("Digitare y per attivare");
                                System.out.println("Digitare n per continuare");
                                String answere = reader.readLine();
                                if (answere.equals("y")) {
                                    System.out.println("Activating logging..");
                                    LoggerManager.getInstance().setLogActive(true);
                                    LoggerManager.getInstance().newLog(nomeFile);
                                    System.out.println("New log file with name [" + nomeFile + "] has been created");
                                } else if (answere.equals("n")) {
                                    System.out.println("Impossibile creare il file [" + nomeFile + "]");
                                } else {
                                    System.out.println(ConsoleColors.ANSI_RED + "[Server] Errore durante l'interpretazione di un comando (verificare la sintassi)" + ConsoleColors.ANSI_RESET);
                                }
                            }
                        }else if (line.equals("stop log")) {
                            if(!LoggerManager.getInstance().isLogActive()){
                                System.out.println(ConsoleColors.ANSI_RED + "Impossibile eseguire quando il log è OFF" + ConsoleColors.ANSI_RESET);
                            }
                            else{
                            String path = LoggerManager.getInstance().getCurrentLogPath();
                            LoggerManager.getInstance().stopLogging();
                            LoggerManager.getInstance().openPath(path);
                            System.out.println("The current logging file has been closed, no further log will be accepted on such file");
                            }
                        } else if (line.equals("test emotion")) {
                            System.out.println("testing sentiment API");
                            List<String> targets = new LinkedList<>();
                            targets.add("Luca");
                            WatsonManager.getInstance().analyzeEmotionByTarget("I'm Luca. Today was a day that started badly and ended worse, plus my mother-in-law doesn't answer my phone", targets);
                        } else if (line.equals("test sentiment")) {
                            System.out.println("testing sentiment API");
                            List<String> targets = new LinkedList<>();
                            targets.add("Luca");
                            targets.add("Fabio");
                            targets.add("Io");
                            WatsonManager.getInstance().analyzeSentimentByTarget("Oggi è stata una giornata cominciata male e finita peggio, e in più mia suocera non mi risponde al telefono", targets);
                        } else if (line.equals("test translate")) {
                            String en = WatsonManager.getInstance().toEnglish("Oggi è stata una giornata cominciata male e finita peggio, e in più mia suocera non mi risponde al telefono");
                            System.out.println("ENGLISH TEXT: " + en);
                        } else if (line.startsWith("face -")) {
                            String[] split = line.split("-");
                            String commandFace = split[1];
                            System.out.println(ConsoleColors.ANSI_GREEN + "face command [" + commandFace + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/face", commandFace);

                        } else if (line.equals("test table")) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "face command [" + "table standard" + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/table", "A<CELL>B<ROW>C<CELL>D");

                        } else if (line.startsWith("test table ")) {
                            String[] split = line.split(" ");
                            String message = split[2];
                            for (int i = 3; i < split.length; i++) {
                                message = message + " " + split[i];
                            }
                            System.out.println(ConsoleColors.ANSI_GREEN + "topic: [" + message + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command/table", message);

                        } else if (line.startsWith("secret topics")) {
                            List<String> tid = MQTTServer.topicids;

                            for (String tir : tid) {
                                System.out.println(ConsoleColors.ANSI_GREEN + "face command [" + ConsoleColors.ANSI_RED + tir + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            }

                        } else if (line.startsWith("test vtable ")) {
                            String[] split = line.split(" ");
                            String message = split[2];
                            for (int i = 3; i < split.length; i++) {
                                message = message + " " + split[i];
                            }
                            System.out.println(ConsoleColors.ANSI_GREEN + "face command [" + message + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command/vtable", message);

                        } else if (line.equals("c -video")) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "video" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command", "video");

                        } else if (line.equals("c -timg")) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "test image" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command", "test image");

                        } else if (line.equals("version")) {
                            System.out.println(ConsoleColors.ANSI_PURPLE + version + ConsoleColors.ANSI_RESET);

                        } else if (line.startsWith("c -img ")) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "test image " + line.split(" ")[2] + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command", "test image " + line.split(" ")[2]);

                        } else if (line.equals("repeat")) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "repeat" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command", "repeat");

                        } else if (line.equals("multichoice")) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "multichoice" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command", "multichoice:piazza,pasta,carne:cosa ti piace di più ?");

                        } else if (line.equals("youtube")) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "youtube" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command/youtube", "test");

                        } else if (line.startsWith("youtube ")) {
                            String link = line.split(" ")[1];
                            System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "repeat" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/command/youtube", link);

                        } else if (line.startsWith("link ")) {
                            String link = line.split(" ")[1];
                            System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "link" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish("user/110/to_user/link", link);

                        } else if (line.equals("history -clear")) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "history -clear" + ConsoleColors.ANSI_GREEN + "] has been detected" + ConsoleColors.ANSI_RESET);
                            HistoryBook.getInstance().clear();

                        } else if (line.equals("mute")) {
                            System.out.println(ConsoleColors.ANSI_RED + "WARNING:  all client are now " + ConsoleColors.ANSI_YELLOW + "MUTED" + ConsoleColors.ANSI_RESET);
                            WatsonManager.getInstance().mute();
                        } else if (line.equals("unmute")) {
                            System.out.println(ConsoleColors.ANSI_RED + "WARNING:  all client are now " + ConsoleColors.ANSI_GREEN + "UNMUTED" + ConsoleColors.ANSI_RESET);
                            WatsonManager.getInstance().unmute();
                        } else if (line.equals("history -all")) {
                            System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "history -all" + ConsoleColors.ANSI_GREEN + "] has been detected" + ConsoleColors.ANSI_RESET);
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

                        } else if (line.startsWith("history ") && (!line.contains("-")) &&(line.split(" ")).length == 2) {
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
                                System.out.println("Logger is currently " + ConsoleColors.ANSI_GREEN + "ON." + ConsoleColors.ANSI_RESET);
                            } else {
                                System.out.println("Logger is currently " + ConsoleColors.ANSI_RED + "OFF." + ConsoleColors.ANSI_RESET);
                            }
                        } else if (line.startsWith("log note ") && !line.replace("log note ", "").isEmpty()) {
                            if(!LoggerManager.getInstance().isLogActive()){
                                System.out.println(ConsoleColors.ANSI_RED + "Impossibile eseguire quando il log è OFF" + ConsoleColors.ANSI_RESET);
                            }
                            else{
                            String free_text = line.substring(9, line.length());
                            LoggerManager.getInstance().log(LoggingTag.NOTE.getTag() + " " + free_text);
                            System.out.println("Note has been added");
                            }
                        } else if(line.equals("log wrong") || line.equals("log w")){
                            LoggerManager.getInstance().log(LoggingTag.WRONG_ANSWER.getTag());
                            System.out.println("Wrong answer has been logged");
                        } else if (line.startsWith("t -#") && line.split(" ").length > 2) {
                            String[] split = line.split(" ");
                            int idi = Integer.parseInt(split[1].substring(2, split[1].length())) - 1;
                            System.out.println("idi= " + idi);
                            String id = server.getON_LINE().get(idi).getId();
                            String message = split[2];
                            for (int i = 3; i < split.length; i++) {
                                message = message + " " + split[i];
                            }
                            System.out.println(ConsoleColors.ANSI_GREEN + "publishing message " + "[" + ConsoleColors.ANSI_PURPLE + message + ConsoleColors.ANSI_GREEN + "] to user-id: " + ConsoleColors.ANSI_GREEN + id + ConsoleColors.ANSI_RESET);
                            MQTTClient.getInstance().publish(Topics.RESPONSES.getTopic() + "/" + id, message);

                        }
                        else if(line.equals("locate log") || line.equals("log locate") || line.equals("locate logs")){
                            //String path = StringUtils.substring(LoggerManager.getInstance().getCurrentLogPath(),0,6);
                            LoggerManager.getInstance().openPath(LoggerManager.LOG_FOLDER);
                        }else if (line.equals("log reprompt") || line.equals("log r")) {
                            if(!LoggerManager.getInstance().isLogActive()){
                                System.out.println(ConsoleColors.ANSI_RED + "Impossibile eseguire quando il log è OFF" + ConsoleColors.ANSI_RESET);
                            }
                            else{
                            LoggerManager.getInstance().log(LoggingTag.REPROMPT.getTag());
                            }
                        } else if (line.equals("log dump") || line.equals("log d")) {
                            if(!LoggerManager.getInstance().isLogActive()){
                                System.out.println(ConsoleColors.ANSI_RED + "Impossibile eseguire quando il log è OFF" + ConsoleColors.ANSI_RESET);
                            }
                            else{
                            LoggerManager.getInstance().dump();
                            }
                        }
                        else if(line.equals("upload current log")){
                            GoogleDriveManager.getInstance().uploadFile(LoggerManager.getInstance().getCurrentLogPath(), LoggerManager.getInstance().getLogName());
                        }
                        else if (line.equals("help")) {
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
                        } else {
                            System.out.println(ConsoleColors.ANSI_RED + "[Server] Errore, comando sconosciuto. (digita help per conoscere i comandi in uso)" + ConsoleColors.ANSI_RESET);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ConsoleColors.ANSI_RED + "[Server] Errore durante l'interpretazione di un comando (verificare la sintassi)" + ConsoleColors.ANSI_RESET);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
