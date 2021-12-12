/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import it.cnr.istc.mw.mqtt.logic.generals.ConsoleColors;
import it.cnr.istc.mw.mqtt.logic.logger.LogTitles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author sommovir
 */
public class GoogleSheetManager {

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private Sheets service = null;

    private String SPREADSHEET_ID = "1w-nTXZJ9Bhl3lsKPt_wrqdlG_HXfEymMn7qm4QZEMDw"; //default

    private static GoogleSheetManager _instance = null;

    public static GoogleSheetManager getInstance() {
        if (_instance == null) {
            _instance = new GoogleSheetManager();
        }
        return _instance;
    }

    private GoogleSheetManager() {
        super();
        try {
            connect();
        } catch (GeneralSecurityException | IOException ex) {
            System.out.println(LogTitles.SERVER.getTitle() + ConsoleColors.ANSI_RED + "ERROR connecting google sheet api: " + ConsoleColors.ANSI_RESET + " cause: " + ex.getMessage());
        }
    }

    public void setSpreadsheetId(String spreadsheetId) {
        this.SPREADSHEET_ID = spreadsheetId;
    }

    public String getSpreadsheetId() {
        return SPREADSHEET_ID;
    }

    private void connect() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1w-nTXZJ9Bhl3lsKPt_wrqdlG_HXfEymMn7qm4QZEMDw";
        final String range = "A2:E10";
        service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

    }

    /**
     * Global instance of the scopes required by this quickstart. If modifying
     * these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleSheetManager.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

//    /**
//     * Prints the names and majors of students in a sample spreadsheet:
//     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
//     * mio: https://docs.google.com/spreadsheets/d/1w-nTXZJ9Bhl3lsKPt_wrqdlG_HXfEymMn7qm4QZEMDw/edit?usp=sharing
//     */
//    public static void main(String... args) throws IOException, GeneralSecurityException {
//        // Build a new authorized API client service.
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        final String spreadsheetId = "1w-nTXZJ9Bhl3lsKPt_wrqdlG_HXfEymMn7qm4QZEMDw";
//        final String range = "Class Data!A2:E";
//        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//        ValueRange response = service.spreadsheets().values()
//                .get(spreadsheetId, range)
//                .execute();
//        List<List<Object>> values = response.getValues();
//        if (values == null || values.isEmpty()) {
//            System.out.println("No data found.");
//        } else {
//            System.out.println("Name, Major");
//            for (List row : values) {
//                // Print columns A and E, which correspond to indices 0 and 4.
//                System.out.printf("%s, %s\n", row.get(0), row.get(4));
//            }
//        }
//    }
    public void test() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1w-nTXZJ9Bhl3lsKPt_wrqdlG_HXfEymMn7qm4QZEMDw";
        final String range = "A2:E10";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("Name, Major");
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                System.out.printf("%s, %s\n", row.get(0), row.get(4));
            }
        }
    }

    public void write(String cellX, String cellY, String content) throws IOException {
        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList("Expenses January"),
                        Arrays.asList("books", "30"),
                        Arrays.asList("pens", "10"),
                        Arrays.asList("Expenses February"),
                        Arrays.asList("clothes", "20"),
                        Arrays.asList("shoes", "5")));
        UpdateValuesResponse result = service.spreadsheets().values()
                .update(SPREADSHEET_ID, "A1", body)
                .setValueInputOption("RAW")
                .execute();
    }
}
