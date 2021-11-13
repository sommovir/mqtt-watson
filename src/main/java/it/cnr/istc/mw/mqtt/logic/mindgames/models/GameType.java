/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.mindgames.models;

/**
 * Classe Placeholder per poi i valori veri
 * @author sommovir
 */
public enum GameType {
    LISTA_SPESA("Lista della spesa","Dopo aver elencato una lista di cose da acquistare al supermercato e\n" +
        "lasciato il tempo di memorizzarla, chiedere di ricordare cosa bisogna comprare in base ad uno specifico\n" +
        "reparto","Dopo aver elencato, una lista di cose da acquistare al supermercato ,e lasciato il tempo di memorizzarla, ti verrà chiesto di ricordareh cosa bisogna acquistare in base ad uno specifico repartoh"),
    DUE("","",""),
    TRE("","","");
  
  private String nomeDelGioco;
  private String descrizioneTestuale;
  private String descrizioneVocale;

    private GameType(String nomeDelGioco, String descrizioneTestuale, String descrizioneVocale) {
        this.nomeDelGioco = nomeDelGioco;
        this.descrizioneTestuale = descrizioneTestuale;
        this.descrizioneVocale = descrizioneVocale;
    }

    public String getNomeDelGioco() {
        return nomeDelGioco;
    }

    public String getDescrizioneTestuale() {
        return descrizioneTestuale;
    }

    public String getDescrizioneVocale() {
        return descrizioneVocale;
    }

    
  
}
