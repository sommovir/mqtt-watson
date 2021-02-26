/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt;

import it.cnr.istc.mw.mqtt.db.DBManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fusesource.jansi.AnsiConsole;

/**
 *
 * @author Luca
 */
public class Main {

    static MQTTServer server = new MQTTServer();

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
                    if (line.equals("quit")) {
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
                            t.interrupt();
                            System.exit(0);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.out.println("[server] quit failed");
                        }
                    } else if (line.equals("list")) {
                        List<InfoUser> on_line = server.getON_LINE();

                        System.out.println(ConsoleColors.ANSI_GREEN + "#  |id\t\t\t\t| timestamp " + ConsoleColors.ANSI_RESET);
                        System.out.println(ConsoleColors.ANSI_GREEN + "--------------------------------------------------------------------------" + ConsoleColors.ANSI_RESET);
                        int i = 1;
                        for (InfoUser infoUser : on_line) {
                            if (infoUser.getId().equals("Server")) {
                                System.out.println(ConsoleColors.GREEN_BRIGHT + i + ") " + ConsoleColors.ANSI_CYAN + " " + infoUser.getId() + "\t\t\t" + ConsoleColors.ANSI_GREEN + "| " + ConsoleColors.ANSI_CYAN + infoUser.getTimestamp() + ConsoleColors.ANSI_RESET);
                            } else {
                                System.out.println(ConsoleColors.GREEN_BRIGHT + i + ") " + ConsoleColors.ANSI_CYAN + " " + infoUser.getId() + "\t\t" + ConsoleColors.ANSI_GREEN + "| " + ConsoleColors.ANSI_CYAN + infoUser.getTimestamp() + ConsoleColors.ANSI_RESET);
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
                    } else if (line.startsWith("face -")) {
                        String[] split = line.split("-");
                        String commandFace = split[1];
                        System.out.println(ConsoleColors.ANSI_GREEN + "face command [" + commandFace + "] has been sent" + ConsoleColors.ANSI_RESET);
                        MQTTClient.getInstance().publish("user/110/to_user/face", commandFace);

                    } else if (line.equals("test table")) {
                        System.out.println(ConsoleColors.ANSI_GREEN + "face command [" + "table standard" + "] has been sent" + ConsoleColors.ANSI_RESET);
                        MQTTClient.getInstance().publish("user/110/to_user/table", "A;B!C;D");

                    } else if (line.startsWith("test table ")) {
                        String[] split = line.split(" ");
                        String message = split[2];
                        for (int i = 3; i < split.length; i++) {
                            message = message + " " + split[i];
                        }
                        System.out.println(ConsoleColors.ANSI_GREEN + "face command [" + message + "] has been sent" + ConsoleColors.ANSI_RESET);
                        MQTTClient.getInstance().publish("user/110/to_user/command/table", message);

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

                    } else if (line.startsWith("c -img ")) {
                        System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "test image " + line.split(" ")[2] + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                        MQTTClient.getInstance().publish("user/110/to_user/command", "test image " + line.split(" ")[2]);

                    } else if (line.equals("repeat")) {
                        System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "repeat" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                        MQTTClient.getInstance().publish("user/110/to_user/command", "repeat");

                    } else if (line.equals("youtube")) {
                        System.out.println(ConsoleColors.ANSI_GREEN + "command [" + ConsoleColors.ANSI_RED + "repeat" + ConsoleColors.ANSI_GREEN + "] has been sent" + ConsoleColors.ANSI_RESET);
                        MQTTClient.getInstance().publish("user/110/to_user/command/youtube", "test");

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
                        System.out.println(ConsoleColors.ANSI_WHITE + "\tcomanda l'esecuzione di una tabella standard 2x2: A;B!C;D ");
                        System.out.println(ConsoleColors.ANSI_WHITE + "\tin alternativa si può far seguire dopo il comando il testo di una tabella da far eseguire");
                        System.out.println(ConsoleColors.ANSI_YELLOW + "\tEs. " + ConsoleColors.ANSI_WHITE + "test table Laboraratorio di cucina;10:30!Laboratorio di Filatelia;19:00");
                        System.out.println(ConsoleColors.ANSI_YELLOW + "7) " + ConsoleColors.ANSI_CYAN + "test vtable [table]");
                        System.out.println(ConsoleColors.ANSI_WHITE + "\tcomanda l'esecuzione di una tabella standard generica ad esempio: A;B;C!D;D;E!F;G;H");
                        System.out.println(ConsoleColors.ANSI_WHITE + "\tNota: la prima riga verrà presa come header");
                        System.out.println(ConsoleColors.ANSI_YELLOW + "8) " + ConsoleColors.ANSI_CYAN + "![testo libero]");
                        System.out.println(ConsoleColors.ANSI_WHITE + "\tinvia come input il testo che segue il !");
                        System.out.println(ConsoleColors.ANSI_YELLOW + "\tEs. " + ConsoleColors.ANSI_WHITE + "!che ore sono? (invia il testo 'che ore sono?'");
                        System.out.println(ConsoleColors.ANSI_YELLOW + "9) " + ConsoleColors.ANSI_CYAN + "c -video");
                        System.out.println(ConsoleColors.ANSI_WHITE + "\tesegue un video di test interno alla app");
                        System.out.println(ConsoleColors.ANSI_YELLOW + "10) " + ConsoleColors.ANSI_CYAN + "c -timg");
                        System.out.println(ConsoleColors.ANSI_WHITE + "\tesegue un caricamento dal drive di un immagine di test con link embeddato");
                        System.out.println(ConsoleColors.ANSI_YELLOW + "11) " + ConsoleColors.ANSI_CYAN + "c -timg [link]");
                        System.out.println(ConsoleColors.ANSI_WHITE + "\tinvia al cellulare il comando per visualizzare a schermo un immagine esterna con il link dato in argomento");
                        System.out.println(ConsoleColors.ANSI_YELLOW + "12) " + ConsoleColors.ANSI_CYAN + "repeat");
                        System.out.println(ConsoleColors.ANSI_WHITE + "\tfa ripetere l'ultimo messaggio ricevuto");
                        System.out.println(ConsoleColors.ANSI_YELLOW + "\tEs. " + ConsoleColors.ANSI_WHITE + "c -img https://drive.google.com/uc?export=view&id=10xKwmTRVEVyi_b3jpgLvx1ovGf9qhnX8");
                        System.out.println(ConsoleColors.ANSI_YELLOW + "13) " + ConsoleColors.ANSI_CYAN + "t -#[id] [message]");
                        System.out.println(ConsoleColors.ANSI_WHITE + "\tinvia un messagge all'utente con id = numero intero che appare a sinistra dell'utente dal comando list.");
                        System.out.println(ConsoleColors.ANSI_YELLOW + "\tEs. " + ConsoleColors.ANSI_WHITE + "t -#2 ciao come stai ? (manda all'utente 2 il messaggo 'ciao come stai ?'");

                        System.out.println(ConsoleColors.ANSI_GREEN + "----------------------------------------------------------------" + ConsoleColors.ANSI_RESET);
                    } else {
                        System.out.println(ConsoleColors.ANSI_RED + "[Server] Errore, comando sconosciuto. (digita help per conoscere i comandi in uso)");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ConsoleColors.ANSI_RED + "[Server] Errore durante l'interpretazione di un comando (verificare la sintassi)");
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
