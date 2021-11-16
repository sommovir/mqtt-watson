/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author sommovir
 */
public class ConfigurationManager {

    private static ConfigurationManager _instance = null;
    private Properties prop = null;

    public static ConfigurationManager getInstance() {
        if (_instance == null) {
            _instance = new ConfigurationManager();
            return _instance;
        } else {
            return _instance;
        }
    }

    private ConfigurationManager() {
        super();
        load();
    }

    /**
     * Prova prima a caricare il file in posizione /config/.. esternamente al jar
     * Se il file è cancellato allora prova a caricare il file interno. 
     */
    private final void load() {

        try {
            File jarPath = new File(ConfigurationManager.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String propertiesPath = jarPath.getParentFile().getAbsolutePath();
            System.out.println(" propertiesPath-" + propertiesPath);
            prop = new Properties();
            prop.load(new FileInputStream(propertiesPath + "/config/watson.properties"));

            System.out.println("[TARGET CONFIG] watson.main = " + prop.getProperty("watson.main"));
            System.out.println("[TARGET CONFIG] watson.main.apikey = " + prop.getProperty("watson.main.apikey"));
            System.out.println("[TARGET CONFIG] watson.main.url = " + prop.getProperty("watson.main.url"));

        } catch (IOException ex) {
            System.out.println("{ERRORE} nonho trovato il watson");
            prop = null;
        }
        if(prop !=null){
            return;
        }

        try ( InputStream input = ConfigurationManager.class.getClassLoader().getResourceAsStream("config/watson.properties")) {

            prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            System.out.println("[CONFIG] watson.main = " + prop.getProperty("watson.main"));
            System.out.println("[CONFIG] watson.main.apikey = " + prop.getProperty("watson.main.apikey"));
            System.out.println("[CONFIG] watson.main.url = " + prop.getProperty("watson.main.url"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
