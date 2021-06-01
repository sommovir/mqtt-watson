/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;

/**
 *
 * @author sommovir
 */
public class QRManager {

    private static QRManager _instance = null;

    public static QRManager getInstance() {
        if (_instance == null) {
            _instance = new QRManager();
        }
        return _instance;

    }

    private QRManager() {
        super();
    }

    private BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix
                = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    
    public void printQR(String barcodeText) throws Exception{
        BufferedImage generateQRCodeImage = this.generateQRCodeImage(barcodeText);
//        generateQRCodeImage
        
        
    }

}
