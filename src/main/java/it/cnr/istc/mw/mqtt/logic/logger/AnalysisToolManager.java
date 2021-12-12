/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.logger;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sommovir
 */
public class AnalysisToolManager {

    private static AnalysisToolManager _instance = null;

    public static AnalysisToolManager getInstance() {
        if (_instance == null) {
            _instance = new AnalysisToolManager();
        }
        return _instance;
    }
    
    /**
     * Restituisce la lista del log file contenuti in logpath
     * @param logpath
     * @return 
     */
    public List<File> getAllLogFiles(String logpath){
        
        return null;
    }


    /**
     * Restituisce il dizionario dei tag presenti in file
     * @param file
     * @return 
     */
    public TagDictionary countTag(File file){
        return null;
    }
    
    private AnalysisToolManager() {
        super();
    }

}
