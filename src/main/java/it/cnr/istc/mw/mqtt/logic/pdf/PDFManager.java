/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.pdf;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.Path;
import it.cnr.istc.mw.mqtt.db.Person;
import it.cnr.istc.mw.mqtt.logic.generals.DefaultPaths;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Date;


/**
 * Questa classe è responsabile della creazione del PDF di autenticazione che
 * verrà poi mandato via email ai partecipanti del laboratorio. Il pdf è
 * composto da varie sezioni, le più importanti sono i due QR-code denominati
 * QR-D e QR-A. Il QR-D o QR-Download contiene il link dell'ultima versione
 * dell'app da scaricare sul proprio dispositivo. Il QR-A o QR-Authentication
 * invece serve per autenticarsi associando quindi l'app ad un account già
 * preesistente. L'app appena installata chiederà di inquadrare un QR per
 * autenticarsi. Dopo che l'app è stata attivata, questo QR sarà marchiato
 * invalido e nessun altro potrà usarlo. Se lo stesso utente vuole collegare più
 * dispositivi allora questi altri dispositivi mostreranno un QR code di
 * autenticazione che dovrà essere inquadrato dal dispositivo primario che li
 * validerà.
 *
 * IL PDF deve essere composto da una instestazione, una breve introduzione e
 * poi il primo QR code a sinistra con la spiegazione a destra. Poi una
 * separazione grafica e una seconda sezione con il secondo QR e come prima la
 * spiegazione a destra seguita da alcune istruzioni.
 *
 * Al termine di queste istruzioni poi si dovrà allegare un manuale d'uso da
 * fondere con un pdf già fatto e messo in una cartella server specifica.
 *
 * @author sommovir
 */
public class PDFManager {

    private static PDFManager _instance = null;

    public static PDFManager getInstance() {
        if (_instance == null) {
            _instance = new PDFManager();
        }
        return _instance;

    }

    private PDFManager() {
        super();
    }

    private void createOutputFolder(String pdfFilePath) {
        File storedFile = new File(pdfFilePath);
        storedFile.getParentFile().mkdirs();
    }

    public void createRegistrationPDF(Person person) throws URISyntaxException, FileNotFoundException, DocumentException, BadElementException, IOException {

        String pdfPath = DefaultPaths.REGISTRATION_PDF.getPath() + "r_" + person.getName() + person.getSurname() + "_"+(new Date().getTime())+".pdf";
        //java.nio.file.Path path = Paths.get(ClassLoader.getSystemResource("/icons/CNR-Logo.jpg").toURI());
        createOutputFolder(pdfPath);

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
        document.open();
        Image img = Image.getInstance(getClass().getResource("/icons/cnrlogo.jpg").getFile());
        document.add(img);
        document.close();

    }

}
