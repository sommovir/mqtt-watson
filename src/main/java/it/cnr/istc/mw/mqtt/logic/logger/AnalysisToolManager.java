/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.logger;

import it.cnr.istc.mw.mqtt.logic.generals.ConsoleColors;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sommovir
 */
public class AnalysisToolManager {

    private static AnalysisToolManager _instance = null;
    public static final String ANALYSIS_FOLDER = "./analysis";

    public static AnalysisToolManager getInstance() {
        if (_instance == null) {
            _instance = new AnalysisToolManager();
        }
        return _instance;
    }

    /**
     * Restituisce la lista del log file contenuti in ANALYSIS_FOLDER
     *
     * @return
     */
    public List<File> getAllLogFiles() {
        Path path = Paths.get(ANALYSIS_FOLDER);
        if (Files.exists(path)) {
            File dir = path.toFile();
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".log");
                }
            });

            for (File log : files) {
                System.out.println(log);
            }
        } else {
            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_RED + "La cartella di analisi dei log non esiste. [" + ConsoleColors.ANSI_YELLOW + ANALYSIS_FOLDER + ConsoleColors.ANSI_RED + "]" + ConsoleColors.ANSI_RESET);
            return null;
        }

        return null;
    }

    /**
     * Restituisce il dizionario dei tag presenti in file
     *
     * @param file
     * @return
     */
    public TagDictionary countTag(File file) {
        TagDictionary dictionary = new TagDictionary(file.getName());
        return null;

    }

    private AnalysisToolManager() {
        super();
    }

}
