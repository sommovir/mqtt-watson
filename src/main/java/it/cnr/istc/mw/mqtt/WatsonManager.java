/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt;

import com.google.common.collect.HashBiMap;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.model.GenericModel;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.DeleteSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageContext;
import com.ibm.watson.assistant.v2.model.MessageContextGlobal;
import com.ibm.watson.assistant.v2.model.MessageContextGlobalSystem;
import com.ibm.watson.assistant.v2.model.MessageContextSkill;
import com.ibm.watson.assistant.v2.model.MessageInputOptions;
import com.ibm.watson.assistant.v2.model.RuntimeEntity;
import com.ibm.watson.assistant.v2.model.RuntimeIntent;
import com.ibm.watson.assistant.v2.model.SessionResponse;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.natural_language_understanding.v1.model.EmotionOptions;
import com.ibm.watson.natural_language_understanding.v1.model.Features;
import com.ibm.watson.natural_language_understanding.v1.model.SentimentOptions;
import it.cnr.istc.mw.mqtt.logic.Emotion;
import it.cnr.istc.mw.mqtt.logic.LoggerManager;
import it.cnr.istc.mw.mqtt.logic.LoggingTag;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca
 */
public class WatsonManager {

    private static WatsonManager _instance = null;
    private Assistant assistant = null;
    String assistant_id = "165ef413-b2c1-44f6-a9a9-2e44d20ae2ec";
    private Map<String, String> sessionIdMap = new HashMap<>();
    private Map<String, Long> expireTimeMap = new HashMap<>();
    private double minSingleDeltaThreshold = 0.6d; //alpha
    private double minDeltaThreshold = 0.3d;       //beta
    private boolean mute = false;
    private boolean testMode = false;
    private static final String HARD_RESET_SECRET_KEY = "A5--AAA!-A";
    //LUCA ASSISTANT ID 3f2e01db-3b43-419b-a81e-dac841b9b373

    public double getMinSingleDeltaThreshold() {
        return minSingleDeltaThreshold; 
    }

    public void setMinSingleDeltaThreshold(double minSingleDeltaThreshold) {
        this.minSingleDeltaThreshold = minSingleDeltaThreshold;
        LoggerManager.getInstance().logAlphaBeta();
    }

    public double getMinDeltaThreshold() {
        return minDeltaThreshold;
    }

    public void setMinDeltaThreshold(double minDeltaThreshold) {
        this.minDeltaThreshold = minDeltaThreshold;
        LoggerManager.getInstance().logAlphaBeta();
    }

    //String session_id = "scemotto";
    //String url = "https://api.eu-gb.assistant.watson.cloud.ibm.com/instances/4e8f3f39-69bd-47cb-8f80-274eb7b26316/v2/assistants/3f2e01db-3b43-419b-a81e-dac841b9b373/sessions";
    public static WatsonManager getInstance() {
        if (_instance == null) {
            _instance = new WatsonManager();
            return _instance;
        } else {
            return _instance;
        }
    }

    public String getSessionId(String userId) {
        String session = sessionIdMap.get(userId);
        return session == null ? "off" : session;
    }

    public String getExpireStatus(String userId) {
        String session = getSessionId(userId);
        boolean expired = false;
        if (session.equals("off")) {
            return "off";
        }
        expired = isSessionExpired(session);
        return expired ? "expired" : "valid";
    }

    public boolean isTestMode() {
        return this.testMode;
    }

    public void setTestMode(boolean testMode) {
        //prova123456
        this.testMode = testMode;
    }

    public void mute() {
        mute = true;
    }

    public void unmute() {
        mute = false;
    }

    public boolean isSessionExpired(String sessionId) {
        if (!expireTimeMap.containsKey(sessionId)) {
            return true;
        } else {
            long time = expireTimeMap.get(sessionId);
            long delta = new Date().getTime() - time;
            long minutFIVE = (60l * 5l * 1000l) - 15000l; //tolleranza di 15 secondi   
            return delta >= minutFIVE;
        }
    }

    public void refreshSessionId(String userId) {
        CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder(assistant_id).build();
        SessionResponse session = assistant.createSession(createSessionOptions).execute().getResult();

        this.sessionIdMap.put(userId, session.getSessionId());
        this.expireTimeMap.put(session.getSessionId(), new Date().getTime());
        System.out.println("[Watson] session [" + session.getSessionId() + "] has been refreshed");
    }

    private WatsonManager() {
        super();
        //connection
        //LUCA IamAuthenticator authenticator = new IamAuthenticator("ABn-91mFQS77sjCF9S3JfxIO7ifcUAIN60vqZzvDXYsq");
        IamAuthenticator authenticator = new IamAuthenticator("yuiKGYRZb68m8z8LHhKOm0eaGoucyszYUpXK6BxODUE2");
        assistant = new Assistant("2020-09-24", authenticator);
        //LUCA assistant.setServiceUrl("https://api.eu-gb.assistant.watson.cloud.ibm.com");
        assistant.setServiceUrl("https://api.eu-de.assistant.watson.cloud.ibm.com/instances/eacf1cec-f25a-4638-95e7-9a2432fbe388");

        // Create session.
//        CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder(assistant_id).build();
//        SessionResponse session = assistant.createSession(createSessionOptions).execute().getResult();
        // session_id = session.getSessionId();
        // CreateSessionOptions options = new CreateSessionOptions.Builder(assistant_id).build();
    }

    private String isAppTextPresent(MessageContext context) {
        Map<String, MessageContextSkill> skills = context.skills();
        for (String key : skills.keySet()) {
            System.out.println("SKILL: " + key);
            if (key.equals("main skill")) {
                MessageContextSkill mcs = skills.get(key);
                if (mcs == null) {
                    return null;
                }
                if (mcs.userDefined() == null) {
                    System.out.println("[watson] no user defined.");
                    return null;
                }
                if (mcs.userDefined().containsKey("apptext")) {
                    if (mcs.userDefined().get("apptext") instanceof String) {
                        // System.out.println("[Server][CRITICAL ERROR] bad format in app text !! --------------------------------------------- NEED REVIEW");
                        return ((String) mcs.userDefined().get("apptext")).replace("<FORCE>", "");
                    } else {
                        return null;
                    }
                }

            }

        }
        return null;
    }
    
        private boolean isAppTextForced(MessageContext context) {
        Map<String, MessageContextSkill> skills = context.skills();
        for (String key : skills.keySet()) {
            System.out.println("SKILL: " + key);
            if (key.equals("main skill")) {
                MessageContextSkill mcs = skills.get(key);
                if (mcs == null) {
                    return false;
                }
                if (mcs.userDefined() == null) {
                    System.out.println("[watson] no user defined.");
                    return false;
                }
                if (mcs.userDefined().containsKey("apptext")) {
                    if (mcs.userDefined().get("apptext") instanceof String) {
                        // System.out.println("[Server][CRITICAL ERROR] bad format in app text !! --------------------------------------------- NEED REVIEW");
                        return ((String) mcs.userDefined().get("apptext")).contains("<FORCE>");
                    } else {
                        return false;
                    }
                }

            }

        }
        return false;
    }

    private String isFacePresent(MessageContext context) {
        Map<String, MessageContextSkill> skills = context.skills();
        for (String key : skills.keySet()) {
            if (key.equals("main skill")) {
                MessageContextSkill mcs = skills.get(key);
                if (mcs == null) {
                    return null;
                }
                if (mcs.userDefined() == null) {
                    System.out.println("[watson] no user defined.");
                    return null;
                }
                if (mcs.userDefined().containsKey("face")) {
                    if (mcs.userDefined().get("face") instanceof String) {
                        System.out.println("[Server][CRITICAL ERROR] bad format in face !! --------------------------------------------- NEED REVIEW");
                        return (String) mcs.userDefined().get("face");
                    } else {
                        return null;
                    }
                }

            }

        }
        return null;
    }

    public String parseAppText(String apptext, String userId) {
        apptext = apptext.replace("<AT>", "@");
        if (apptext.startsWith("*")) {
            String text = "";
            try {
                String mixedString = apptext.substring(1);
                String[] split = mixedString.split("<COMMAND>");
                for (String string : split) {
                    String[] commandAndValue = string.split("<:>");
                    String command = commandAndValue[0];
                    String value = commandAndValue[1];
                    if (command.equals("face")) {
                        String topic = Topics.COMMAND.getTopic() + "/" + userId + "/face";
                        MQTTClient.getInstance().publish(topic, value);
                        try {
                            LoggerManager.getInstance().log(LoggingTag.FACE.getTag() + " " + value);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    if (command.equals("table")) {
                        String topic = Topics.COMMAND.getTopic() + "/" + userId + "/table";
                        if (value.contains("<CONTINUE>")) {
                            String[] tables = value.split("<CONTINUE>");
                            for (String table : tables) {
                                MQTTClient.getInstance().publish(topic, "<CONTINUE>" + table);
                                Thread.sleep(50);
                            }
                        } else {
                            MQTTClient.getInstance().publish(topic, value);
                        }
                        try {
                            LoggerManager.getInstance().log(LoggingTag.TABLE.getTag() + " " + value);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                    }
                    if (command.equals("link")) {
                        String topic = Topics.COMMAND.getTopic() + "/" + userId + "/link";
                        MQTTClient.getInstance().publish(topic, value);
                        try {
                            LoggerManager.getInstance().log(LoggingTag.LINK.getTag() + " " + value);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    if (command.equals("youtube")) {
                        String topic = Topics.COMMAND.getTopic() + "/" + userId + "/youtube";
                        MQTTClient.getInstance().publish(topic, value);
                        try {
                            LoggerManager.getInstance().log(LoggingTag.VIDEO.getTag() + " " + value);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    if (command.equals("img")) {
                        String topic = Topics.COMMAND.getTopic() + "/" + userId + "/img";
                        MQTTClient.getInstance().publish(topic, value);
                        try {
                            LoggerManager.getInstance().log(LoggingTag.IMG.getTag() + " " + value);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    if (command.equals("listen")) {
                        String topic = Topics.COMMAND.getTopic() + "/" + userId + "/listen";
                        MQTTClient.getInstance().publish(topic, value);
                    }
                    if (command.equals("text")) {
                        String nameById = MQTTClient.getInstance().getNameById(userId);
                        if (nameById == null) {
                            nameById = "";
                        }
                        value = value.replace("<NAME>", nameById);
                        text = value;
                    }
                }
                if (text.startsWith("<REMINDER>")) {
                    text = text.replace("<REMINDER>", "");
                    String topic = Topics.COMMAND.getTopic() + "/" + userId + "/reminder";
                    String reminderText = apptext.split("<ROW>Messaggio: ")[1];
                    String reminderTime = apptext.split("<ROW>Orario: ")[1].substring(0, 5);
                    MQTTClient.getInstance().publish(topic, reminderText + "<:>" + reminderTime);
                }
                return text;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "c'è stato un errore nella gestione del messaggio da parte del server";
            }
        } else {
            return apptext;
        }
    }

    public boolean isMute() {
        return mute;
    }

    public boolean analyzeSentimentByTarget(String text, List<String> targets) {

        IamAuthenticator authenticator = new IamAuthenticator("heVbLRGb-xJiYVUGpJbNmB_OwxKAByyIQxD-E96EPP5_");
        NaturalLanguageUnderstanding naturalLanguageUnderstanding = new NaturalLanguageUnderstanding("2020-08-01", authenticator);
        naturalLanguageUnderstanding.setServiceUrl("https://gateway-lon.watsonplatform.net/natural-language-understanding/api");

        SentimentOptions sentiment = new SentimentOptions.Builder()
                .targets(targets)
                .build();

        Features features = new Features.Builder()
                .sentiment(sentiment)
                .build();

//        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
//                .html(html)
//                .features(features)
//                .build();
        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(text)
                .features(features)
                .build();

        AnalysisResults response = naturalLanguageUnderstanding
                .analyze(parameters)
                .execute()
                .getResult();
        System.out.println(response);

        return true;

    }

    public String toEnglish(String italianText) {
        IamAuthenticator authenticator = new IamAuthenticator("AV32Gzzrt_XadKKhjMxgr00X1Uyf66DJpOaLRm3K6R-J");
        LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
        languageTranslator.setServiceUrl("https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/f3a46212-88ab-4423-8c06-9860c651fe14");

        TranslateOptions translateOptions = new TranslateOptions.Builder()
                .addText(italianText)
                .modelId("it-en")
                .build();

        TranslationResult result = languageTranslator.translate(translateOptions)
                .execute().getResult();

        System.out.println(result);

        return result.getTranslations().get(0).getTranslation();

    }

    public Emotion analyzeEmotionByTarget(String text, List<String> targets) {

        IamAuthenticator authenticator = new IamAuthenticator("heVbLRGb-xJiYVUGpJbNmB_OwxKAByyIQxD-E96EPP5_");
        NaturalLanguageUnderstanding naturalLanguageUnderstanding = new NaturalLanguageUnderstanding("2020-08-01", authenticator);
        naturalLanguageUnderstanding.setServiceUrl("https://gateway-lon.watsonplatform.net/natural-language-understanding/api");

        EmotionOptions emotion = new EmotionOptions.Builder()
                .targets(targets)
                .build();

        Features features = new Features.Builder()
                .emotion(emotion)
                .build();

//        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
//                .html(html)
//                .features(features)
//                .build();
        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(text)
                .features(features)
                .build();

        AnalysisResults response = naturalLanguageUnderstanding
                .analyze(parameters)
                .execute()
                .getResult();
        System.out.println(response);

        return null;

    }

    private boolean isAffermative(List<RuntimeEntity> entitiesList) {
        for (RuntimeEntity runtimeEntity : entitiesList) {
            if (runtimeEntity.entity().equals("risposta_affermativa")) {
                return true;
            }
        }
        return false;
    }

    private boolean isNegative(List<RuntimeEntity> entitiesList) {
        for (RuntimeEntity runtimeEntity : entitiesList) {
            if (runtimeEntity.entity().equals("risposta_negativa")) {
                return true;
            }
        }
        return false;
    }

    public void hardReset() {
        Collection<String> userIds = sessionIdMap.keySet();
        for (String userId : userIds) {
            System.out.println("USER-ID -> > > > > "+userId);
            if (userIds.equals("110")) {
                continue;
            }
            sendMessage(HARD_RESET_SECRET_KEY, userId);
        }
    }

    public String sendMessage(String message, String userId) {
        try {
            System.out.println("[Watson] sending message to AI.. ");
            System.out.println("[Watson] user id:  " + userId);
            System.out.println("[Watson] message:  " + message);
            if (!this.sessionIdMap.containsKey(userId)) {
                CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder(assistant_id).build();
                SessionResponse session = assistant.createSession(createSessionOptions).execute().getResult();
                this.sessionIdMap.put(userId, session.getSessionId());
                this.expireTimeMap.put(session.getSessionId(), new Date().getTime());
                if (isSessionExpired(this.sessionIdMap.get(userId))) {
                    System.out.println("[Watson] Session EXPIRED");
                    refreshSessionId(userId);
                }

            } else {
                if (isSessionExpired(this.sessionIdMap.get(userId))) {
                    System.out.println("[Watson] Session EXPIRED");
                    refreshSessionId(userId);
                }
            }
            String session_id = this.sessionIdMap.get(userId);

            System.out.println("[Watson] session id: " + session_id);

            MessageInputOptions option = new MessageInputOptions.Builder()
                    .alternateIntents(Boolean.TRUE)
                    .returnContext(Boolean.TRUE)
                    .build();

            MessageInput input = new MessageInput.Builder()
                    .text(message)
                    .options(option)
                    .build();

            MessageOptions options = new MessageOptions.Builder(assistant_id, session_id)
                    .input(input)
                    .build();

            MessageResponse response = assistant.message(options).execute().getResult();

            MessageContext context = response.getContext();

            System.out.println(ConsoleColors.ANSI_GREEN + "CONTEXT= " + ConsoleColors.ANSI_RESET + context);
            System.out.println("-----------------------------");
            System.out.println("-----------------------------");
            System.out.println(ConsoleColors.ANSI_GREEN + "RESPONSE= " + ConsoleColors.ANSI_RESET + response);

//            String actualResponse = isAppTextPresent(context);
//            if (actualResponse != null) {
//                actualResponse = parseAppText(actualResponse, userId);
//            }
            //  String facePresent = isFacePresent(context);
//        if (actualResponse != null) {
//            System.out.println("-----------------------------");
//            System.out.println(ConsoleColors.ANSI_CYAN+"APP RESPONSE= " +ConsoleColors.ANSI_RESET+actualResponse);
//           // return actualResponse;
//        }
            String[] errorMessages = new String[]{
                "Non ho capito. Puoi riformulare la frase?",
                "Puoi ripetere usando altre parole? Non ho capito.",
                "Non ho capito cosa mi hai detto."
            };

            Random rand = new Random();
            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            int randomNum = rand.nextInt(errorMessages.length);

            /*
            for (String errorMessage : errorMessages) {
                if(response.getOutput().getGeneric().get(0).text().equals(errorMessage)){
                    LoggerManager.getInstance().log(LoggingTag.REJECTS.getTag());
                }
            }*/
            if (response.getOutput().getGeneric() == null || response.getOutput().getGeneric().isEmpty()) {
                try {
                    LoggerManager.getInstance().newFailedIntentDetected(0);
                    LoggerManager.getInstance().log(LoggingTag.REJECTS.getTag());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return errorMessages[randomNum];
            }

//            if (response.getOutput().getGeneric().get(0).responseType().equals("suggestion")) {
//                try {
//                    LoggerManager.getInstance().log(LoggingTag.REJECTS.getTag());
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//                System.out.println(ConsoleColors.GREEN_BRIGHT + "[Watson]: " + ConsoleColors.PURPLE_BRIGHT + "Mi spiace non ho capito" + ConsoleColors.ANSI_RESET);
//                return "mi spiace non ho capito";
//            }
//            
            String risposta = "Mi spiace non ho capito";
            List<Double> entitiesConfList = toDobleList(response.getOutput().getEntities());
            List<Double> intentsConfList = toDobleList(response.getOutput().getIntents());
            
            boolean appTextForced = isAppTextForced(context);
            
//            System.out.println("------------------------------- " + appTextForced + " -------------------------------");
            
            if (hasNoEntitis(0.2f, entitiesConfList) && hasNoIntents(0.2f, intentsConfList) && !appTextForced) {
                try {
                    LoggerManager.getInstance().newFailedIntentDetected(intentsConfList == null || intentsConfList.isEmpty()? 0 : intentsConfList.get(0));
                    LoggerManager.getInstance().newEntitiesDetected(entitiesConfList == null || entitiesConfList.isEmpty()? 0 : entitiesConfList.get(0));
                    LoggerManager.getInstance().log(LoggingTag.REJECTS.getTag() + LoggingTag.BYPASS.getTag());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println(ConsoleColors.GREEN_BRIGHT + "[Watson]: " + ConsoleColors.PURPLE_BRIGHT + "bypass" + ConsoleColors.ANSI_RESET);
                return risposta;
            }

            
            if (!isAppTextForced(context) && (hasNoEntitis(0.2f, entitiesConfList) && isLowDeltaExisting(minDeltaThreshold, minSingleDeltaThreshold, intentsConfList)) || 
                    response.getOutput().getGeneric().get(0).responseType().equals("suggestion")) {
                try {
                    LoggerManager.getInstance().newFailedIntentDetected(intentsConfList == null || intentsConfList.isEmpty()? 0 : intentsConfList.get(0));
                    LoggerManager.getInstance().newEntitiesDetected(entitiesConfList == null || entitiesConfList.isEmpty()? 0 : entitiesConfList.get(0));
                    LoggerManager.getInstance().log(LoggingTag.LOW_DELTA.getTag());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println(ConsoleColors.GREEN_BRIGHT + "[Watson]: " + ConsoleColors.PURPLE_BRIGHT + "DELTA" + ConsoleColors.ANSI_RESET);
                return "<AUTOLISTEN>Scusa potresti essere più preciso?";
            }

            if (response.getOutput().getGeneric().get(0).text() != null && response.getOutput().getGeneric().get(0).text().toLowerCase().contains("non ho capito")) {
                try {
                    LoggerManager.getInstance().newFailedIntentDetected(intentsConfList == null || intentsConfList.isEmpty()? 0 : intentsConfList.get(0));
                    LoggerManager.getInstance().newEntitiesDetected(entitiesConfList == null || entitiesConfList.isEmpty()? 0 : entitiesConfList.get(0));
                    LoggerManager.getInstance().log(LoggingTag.REJECTS.getTag());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            String actualResponse = isAppTextPresent(context);
            if (actualResponse != null) {
                actualResponse = parseAppText(actualResponse, userId);
            }

//            if (hasNoEntitis(0.3f, response.getOutput().getEntities()) && hasNoIntents(0.3f, response.getOutput().getIntents())) {
//                System.out.println(ConsoleColors.GREEN_BRIGHT + "[Watson]: " + ConsoleColors.PURPLE_BRIGHT + "bypass" + ConsoleColors.ANSI_RESET);
//                try {
//                    LoggerManager.getInstance().log(LoggingTag.REJECTS.getTag() + LoggingTag.BYPASS.getTag());
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//                return risposta;
//            }
            if (isAffermative(response.getOutput().getEntities())) {
                try {
                    LoggerManager.getInstance().log(LoggingTag.POSITIVE_ANS.getTag());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (isNegative(response.getOutput().getEntities())) {
                try {
                    LoggerManager.getInstance().log(LoggingTag.NEGATIVE_ANS.getTag());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            risposta = response.getOutput().getGeneric().get(0).text();
            System.out.println(ConsoleColors.GREEN_BRIGHT + "[Watson] input: " + ConsoleColors.PURPLE_BRIGHT + message + ConsoleColors.ANSI_RESET);
            if (actualResponse != null) {
                System.out.println(ConsoleColors.GREEN_BRIGHT + "[Watson] app response: " + ConsoleColors.ANSI_YELLOW + actualResponse + ConsoleColors.ANSI_RESET);

            } else {
                System.out.println(ConsoleColors.GREEN_BRIGHT + "[Watson] app response: " + ConsoleColors.ANSI_RED + "not found" + ConsoleColors.ANSI_RESET);
            }
            System.out.println(ConsoleColors.GREEN_BRIGHT + "[Watson] chat response: " + ConsoleColors.ANSI_CYAN + risposta + ConsoleColors.ANSI_RESET);
            if (actualResponse != null) {
                risposta = actualResponse;
            }
            // risposta = risposta.replace("è", "e'");
            System.out.println("about to finishing the send watson method");
            if (risposta == null || risposta.isEmpty()) {
                try {
                    LoggerManager.getInstance().newFailedIntentDetected(intentsConfList == null || intentsConfList.isEmpty()? 0 : intentsConfList.get(0));
                    LoggerManager.getInstance().newEntitiesDetected(entitiesConfList == null || entitiesConfList.isEmpty()? 0 : entitiesConfList.get(0));
                    LoggerManager.getInstance().log(LoggingTag.NOANSWER.getTag());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return "Scusa non ho capito";

            }
            System.out.println("\n\n\n--------------------------" + risposta + "--------------------------\n\n\n");
            if (risposta.contains("<NAME>")) {
                String nameById = MQTTClient.getInstance().getNameById(userId);
                if (nameById == null) {
                    nameById = "";
                }
                risposta = risposta.replace("<NAME>", nameById);
            }
            try {
                LoggerManager.getInstance().newIntentDetected(intentsConfList == null || intentsConfList.isEmpty()? 0 : intentsConfList.get(0));
                LoggerManager.getInstance().newEntitiesDetected(entitiesConfList == null || entitiesConfList.isEmpty()? 0 : entitiesConfList.get(0));
                LoggerManager.getInstance().log(LoggingTag.CONFIDENCE_INTENTS.getTag() + " " + generateIntensLog(response.getOutput().getIntents()));
                LoggerManager.getInstance().log(LoggingTag.CONFIDENCE_ENTITIES.getTag() + " " + generateEntitiesLog(response.getOutput().getEntities()));
                //LoggerManager.getInstance().log(LoggingTag.PRECISION_ENTITIES.getTag() + " " + precisionEntitiesCalcolation(response.getOutput().getEntities()));
                //LoggerManager.getInstance().log(LoggingTag.PRECISION_INTENTS.getTag() + " " + precisionIntentsCalcolation(response.getOutput().getIntents()));
                LoggerManager.getInstance().log(LoggingTag.SYSTEM_TURNS.getTag() + " " + risposta);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return risposta;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "errore";
        }
        // risposta = risposta.replace("televita", " .Televita");

    }
    
    public String generateIntensLog(List<RuntimeIntent> list){
        String result = "";
        
        for (RuntimeIntent runtimeIntent : list) {
            result += "[#" + runtimeIntent.intent() + ", " + runtimeIntent.confidence() + "]";
        }
        return result;
    }
    
    public String generateEntitiesLog(List<RuntimeEntity> list){
        String result = "";
        
        for (RuntimeEntity runtimeEntity : list) {
            result += "[#" + runtimeEntity.entity() + ", " + runtimeEntity.confidence() + "]";
        }
        return result;
    }

    public boolean hasNoIntents(float treshold, List<Double> intents) {
        return (Collections.max(intents) < treshold);
    }

    /**
     *
     * @param treshold soglia minima della differenza della confidence dei primi
     * due intenti
     * @param intents lista della confidence di tutti gli intenti presenti
     * @return true se il delta tra la prima e la seconda confidence è minore di
     * treshold false se il delta tra la prima e la seconda confidence è
     * maggiore uguale di treshold
     */
    public boolean isLowDeltaExisting(double multiTreshold, double singleTreshold, List<Double> intents) {
        if (intents.isEmpty()) {
            return true;
        }

        Collections.sort(intents, Collections.reverseOrder());
        for (Double intent : intents) {
            System.out.println("----------- " + intent + " -----------");
        }
        if (intents.get(0) < singleTreshold) {
            return true;
        }

        if (intents.size() == 1) {
            return false;
        }

        if ((intents.get(0) - intents.get(1)) < multiTreshold) {
            return true;
        }

        return false;
    }

    private <T extends GenericModel> List<Double> toDobleList(List<T> genericList) {
        List<Double> floatini = new ArrayList<>(genericList.size());
        for (GenericModel intent : genericList) {
            try {
                floatini.add((Double) intent.getClass().getMethod("confidence").invoke(intent));
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(WatsonManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(WatsonManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(WatsonManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(WatsonManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(WatsonManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return floatini;
    }

    public boolean hasNoEntitis(float treshold, List<Double> entities) {
        if(entities == null || entities.isEmpty()){
            return true;
        }
        return (Collections.max(entities) < treshold);
    }

    public void quit() {
        //DeleteSessionOptions deleteSessionOptions = new DeleteSessionOptions.Builder(assistant_id, session_id).build();
        //assistant.deleteSession(deleteSessionOptions).execute();
    }
}
