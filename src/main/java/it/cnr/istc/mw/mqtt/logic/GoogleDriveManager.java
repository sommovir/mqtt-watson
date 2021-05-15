/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.logic;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.driveactivity.v2.DriveActivityScopes;
import com.google.api.services.driveactivity.v2.model.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import it.cnr.istc.mw.mqtt.exceptions.InvalidFolderNameException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sommovir
 */
public class GoogleDriveManager {

    private static GoogleDriveManager _instance = null;
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "mqtt-wa";

    /**
     * Directory to store authorization tokens for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File("tokens");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     *
     * <p>
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES
            = Arrays.asList(DriveActivityScopes.DRIVE_ACTIVITY_READONLY, DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Drive drive = null;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    public static GoogleDriveManager getInstance() {
        if (_instance == null) {
            _instance = new GoogleDriveManager();
        }
        return _instance;
    }

    private GoogleDriveManager() {
        super();
        try {
            drive = getDrive();
            com.google.api.services.driveactivity.v2.DriveActivity service = getDriveActivityService();

            // Print the recent activity in your Google Drive.
//            QueryDriveActivityResponse result
//                    = service.activity().query(new QueryDriveActivityRequest().setPageSize(10)).execute();
//            List<DriveActivity> activities = result.getActivities();
//            if (activities == null || activities.size() == 0) {
//                System.out.println("No activity.");
//            } else {
//                System.out.println("Recent activity:");
//                for (DriveActivity activity : activities) {
//                    String time = getTimeInfo(activity);
//                    String action = getActionInfo(activity.getPrimaryActionDetail());
//                    List<String> actors
//                            = activity.getActors().stream()
//                                    .map(GoogleDriveManager::getActorInfo)
//                                    .collect(Collectors.toList());
//                    List<String> targets
//                            = activity.getTargets().stream()
//                                    .map(GoogleDriveManager::getTargetInfo)
//                                    .collect(Collectors.toList());
//                    System.out.printf(
//                            "%s: %s, %s, %s\n", time, truncated(actors), action, truncated(targets));
//                }
//            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = GoogleDriveManager.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets
                = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow
                = new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential
                = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
                        .authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive Activity client service.
     *
     * @return an authorized DriveActivity client service
     * @throws IOException
     */
    public static com.google.api.services.driveactivity.v2.DriveActivity getDriveActivityService()
            throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.driveactivity.v2.DriveActivity.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Build and return an authorized Drive Activity client service.
     *
     * @return an authorized DriveActivity client service
     * @throws IOException
     */
    public static Drive getDrive()
            throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * crea una cartella nel drive con il nome specificato in argomento
     *
     * @param folderName il nome della cartella da creare
     */
    private String createFolder(String folderName) throws InvalidFolderNameException, IOException {
        if (folderName == null || folderName.isEmpty()) {
            throw new InvalidFolderNameException();
        }
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        File file = drive.files().create(fileMetadata)
                .setFields("id")
                .execute();
        System.out.println("Folder ID: " + file.getId());
        return file.getId();
    }

    /**
     * Controlla se esiste una data cartella sul drive, nella directory principale
     * @param folderName
     * il nome della cartella da ricercare
     * @return 
     * l'id della cartella se viene trovata, null altrimenti
     */
    public String isFolderExisting(String folderName) {
        try {
            Files.List request = drive.files().list().setQ(
                    "mimeType='application/vnd.google-apps.folder' and trashed=false");
            FileList files = request.execute();
            List<File> filesList = files.getFiles();
            for (File file : filesList) {
                //System.out.println("FOLDER_NAME: "+file.getName());
                if (file.getName().equals(folderName)) {
                    return file.getId();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GoogleDriveManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Uploads a file using either resumable or direct media upload.
     */
    public void uploadFile(String localFilePath, String driveName) throws IOException {
        String folderId = GoogleDriveManager.getInstance().isFolderExisting("LOGS");

        if (folderId == null) {
            try {
                folderId = createFolder("LOGS");
            } catch (InvalidFolderNameException ex) {
                folderId = "1OJbhhzQHdL1Wfbfl9oOsrxisZV4M_WqH"; //appia/backup
            } catch (Exception ex) {
                folderId = "1OJbhhzQHdL1Wfbfl9oOsrxisZV4M_WqH"; //appia/backup
            }
        }

        File fileMetadata = new File();
        fileMetadata.setName(driveName);
        fileMetadata.setParents(Collections.singletonList(folderId));
        java.io.File filePath = new java.io.File(localFilePath);
        FileContent mediaContent = new FileContent("text/plain", filePath);
        File file = drive.files().create(fileMetadata, mediaContent)
                .setFields("id, parents") //.setFields("id") se non si vuole mettere il file in qualche sottocartella
                .execute();
        System.out.println("File ID: " + file.getId());
    }

    public static void main(String[] args) throws IOException {
        // Build a new authorized API client service.
        com.google.api.services.driveactivity.v2.DriveActivity service = getDriveActivityService();

        // Print the recent activity in your Google Drive.
        QueryDriveActivityResponse result
                = service.activity().query(new QueryDriveActivityRequest().setPageSize(10)).execute();
        List<DriveActivity> activities = result.getActivities();
        if (activities == null || activities.size() == 0) {
            System.out.println("No activity.");
        } else {
            System.out.println("Recent activity:");
            for (DriveActivity activity : activities) {
                String time = getTimeInfo(activity);
                String action = getActionInfo(activity.getPrimaryActionDetail());
                List<String> actors
                        = activity.getActors().stream()
                                .map(GoogleDriveManager::getActorInfo)
                                .collect(Collectors.toList());
                List<String> targets
                        = activity.getTargets().stream()
                                .map(GoogleDriveManager::getTargetInfo)
                                .collect(Collectors.toList());
                System.out.printf(
                        "%s: %s, %s, %s\n", time, truncated(actors), action, truncated(targets));
            }
        }
    }

    /**
     * Returns a string representation of the first elements in a list.
     */
    private static String truncated(List<String> array) {
        return truncatedTo(array, 2);
    }

    /**
     * Returns a string representation of the first elements in a list.
     */
    private static String truncatedTo(List<String> array, int limit) {
        String contents = array.stream().limit(limit).collect(Collectors.joining(", "));
        String more = array.size() > limit ? ", ..." : "";
        return "[" + contents + more + "]";
    }

    /**
     * Returns the name of a set property in an object, or else "unknown".
     */
    private static <T> String getOneOf(AbstractMap<String, T> obj) {
        Iterator<String> iterator = obj.keySet().iterator();
        return iterator.hasNext() ? iterator.next() : "unknown";
    }

    /**
     * Returns a time associated with an activity.
     */
    private static String getTimeInfo(DriveActivity activity) {
        if (activity.getTimestamp() != null) {
            return activity.getTimestamp();
        }
        if (activity.getTimeRange() != null) {
            return activity.getTimeRange().getEndTime();
        }
        return "unknown";
    }

    /**
     * Returns the type of action.
     */
    private static String getActionInfo(ActionDetail actionDetail) {
        return getOneOf(actionDetail);
    }

    /**
     * Returns user information, or the type of user if not a known user.
     */
    private static String getUserInfo(User user) {
        if (user.getKnownUser() != null) {
            KnownUser knownUser = user.getKnownUser();
            Boolean isMe = knownUser.getIsCurrentUser();
            return (isMe != null && isMe) ? "people/me" : knownUser.getPersonName();
        }
        return getOneOf(user);
    }

    /**
     * Returns actor information, or the type of actor if not a user.
     */
    private static String getActorInfo(Actor actor) {
        if (actor.getUser() != null) {
            return getUserInfo(actor.getUser());
        }
        return getOneOf(actor);
    }

    /**
     * Returns the type of a target and an associated title.
     */
    private static String getTargetInfo(Target target) {
        if (target.getDriveItem() != null) {
            return "driveItem:\"" + target.getDriveItem().getTitle() + "\"";
        }
        if (target.getDrive() != null) {
            return "drive:\"" + target.getDrive().getTitle() + "\"";
        }
        if (target.getFileComment() != null) {
            DriveItem parent = target.getFileComment().getParent();
            if (parent != null) {
                return "fileComment:\"" + parent.getTitle() + "\"";
            }
            return "fileComment:unknown";
        }
        return getOneOf(target);
    }

}
