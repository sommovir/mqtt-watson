/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.pdf;

/**
 * Questa classe è responsabile della creazione del PDF di autenticazione che 
 * verrà poi mandato via email ai partecipanti del laboratorio. 
 * Il pdf è composto da varie sezioni, le più importanti sono i due QR-code 
 * denominati QR-D e QR-A. 
 * Il QR-D o QR-Download contiene il link dell'ultima versione dell'app da
 * scaricare sul proprio dispositivo. 
 * Il QR-A o QR-Authentication invece serve per autenticarsi associando quindi
 * l'app ad un account già preesistente. L'app appena installata chiederà
 * di inquadrare un QR per autenticarsi. Dopo che l'app è stata attivata, questo
 * QR sarà marchiato invalido e nessun altro potrà usarlo. 
 * Se lo stesso utente vuole collegare più dispositivi allora questi altri
 * dispositivi mostreranno un QR code di autenticazione che dovrà essere
 * inquadrato dal dispositivo primario che li validerà. 
 * 
 * IL PDF deve essere composto da una instestazione, una breve introduzione
 * e poi il primo QR code a sinistra con la spiegazione a destra. 
 * Poi una separazione grafica e una seconda sezione con il secondo QR e come
 * prima la spiegazione a destra seguita da alcune istruzioni. 
 * 
 * Al termine di queste istruzioni poi si dovrà allegare un manuale d'uso da
 * fondere con un pdf già fatto e messo in una cartella server specifica.
 * @author sommovir
 */
public class PDFManager {
    
}
