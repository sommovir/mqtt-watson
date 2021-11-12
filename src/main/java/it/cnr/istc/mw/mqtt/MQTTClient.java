/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt;

import it.cnr.istc.mw.mqtt.logic.generals.ConsoleColors;
import it.cnr.istc.mw.mqtt.exceptions.InvalidAttemptToLogException;
import it.cnr.istc.mw.mqtt.exceptions.LogOffException;
import it.cnr.istc.mw.mqtt.logic.generals.DeviceType;
import it.cnr.istc.mw.mqtt.logic.logger.HistoryBook;
import it.cnr.istc.mw.mqtt.logic.logger.LogTitles;
import it.cnr.istc.mw.mqtt.logic.logger.LoggerManager;
import it.cnr.istc.mw.mqtt.logic.logger.LoggingTag;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author Luca
 */
public class MQTTClient implements MqttCallback {

    public static final String LOGIN_TOPIC = "loginTopic";
    private static MQTTClient _instance = null;
    private final int qos = 2;
    private MqttClient sampleClient = null;
    private String myNickName = "unknown";
    private String secret = "bumbu";
    private String rispostaPrecedente = "";
    private Map<String, String> idNameMap = new HashMap<String, String>();

    String content = "Tester message";

    //String broker = "tcp://localhost:1883";
    String broker;
    String ip_server;

    static final String clientId = "Server";

    public static MQTTClient getInstance() {
        if (_instance == null) {
            _instance = new MQTTClient();
            return _instance;
        } else {
            return _instance;
        }
    }

    private MQTTClient() {
        super();
    }

    public boolean isConnected() {
        if (this.sampleClient == null) {
            return false;
        }
        return this.sampleClient.isConnected();
    }

    public String getNameById(String id) {
        return idNameMap.get(id);
    }

    /**
     * Ritorna l'ip della macchina su cui si sta operando.
     *
     * @return
     */
    public String getIP() {
        String ip = null;
        try (final DatagramSocket socket = new DatagramSocket()) { //try-with (closeable)
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (SocketException ex) {
            Logger.getLogger(MQTTServer.class.getName()).log(Level.SEVERE, null, ex);
            ip = "socket-exception";
        } catch (UnknownHostException ex) {
            Logger.getLogger(MQTTServer.class.getName()).log(Level.SEVERE, null, ex);
            ip = "unknown-exception";
        }
        return ip;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setIp_server(String ip_server) {
        this.ip_server = ip_server;
    }

    public void unsubscribe(String topic) {
        try {
            sampleClient.unsubscribe(topic);
        } catch (MqttException ex) {
            ex.printStackTrace();
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void subscribe(String topic) {
        try {
            sampleClient.subscribe(topic);
        } catch (MqttException ex) {
            ex.printStackTrace();
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void connect() {
        String topic = "news";
        this.broker = "tcp://" + ip_server + ":1883";
        try {
            sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
//            connOpts.setCleanSession(true);
            //connOpts.setAutomaticReconnect(true);
            System.out.println(LogTitles.SERVER.getTitle() + "paho-client connecting to broker: " + broker);
            sampleClient.connect(connOpts);

            /* subscribe section */
            sampleClient.subscribe("UserConnected");
            sampleClient.subscribe(Topics.CHAT.getTopic());
            sampleClient.subscribe(Topics.LOG.getTopic());
            sampleClient.subscribe(Topics.USERNAME.getTopic());
            sampleClient.subscribe(Topics.BUTTON_PRESSED.getTopic());
            sampleClient.subscribe(Topics.REPEAT.getTopic());
            sampleClient.subscribe(Topics.GETDEVICE.getTopic());
            sampleClient.subscribe("AllConnected");

            sampleClient.setCallback(this);

            System.out.println(LogTitles.SERVER.getTitle() + "paho-client connected to broker");
            System.out.println(LogTitles.SERVER.getTitle() + "paho-client publishing message: " + content);

            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println(LogTitles.SERVER.getTitle() + "paho-client message published");

            System.out.println(LogTitles.SERVER.getTitle() + "Status of client connection: " + (sampleClient.isConnected() ? "ONLINE" : "OFFLINE"));

//            sampleClient.disconnect();
//            System.out.println("paho-client disconnected");
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    public void tryLogin(String username, String encryptedPassword) {
        try {
            String topic = Topics.ATTEMPT_LOGIN.getTopic() + "/" + clientId;
            String message = username + "," + encryptedPassword;
            MqttMessage mx = new MqttMessage(message.getBytes());
            mx.setQos(qos);
            if (sampleClient.isConnected()) {
                System.out.println(username + " is connected");
            } else {
                System.out.println(username + " is not connected");
                sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                // connOpts.setCleanSession(true);
                //connOpts.setAutomaticReconnect(true);
                System.out.println("paho-client connecting to broker: " + broker);

                sampleClient.connect(connOpts);
                sampleClient.subscribe(Topics.ACK_LOGIN.getTopic() + "/" + clientId);
                sampleClient.subscribe(Topics.LOG.getTopic());
                sampleClient.subscribe(Topics.BUTTON_PRESSED.getTopic());
                sampleClient.subscribe(Topics.USERNAME.getTopic() + "/" + clientId);
                sampleClient.subscribe(Topics.REPEAT.getTopic() + "/" + clientId);

            }

            sampleClient.publish(topic, mx);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendChatMessage(String message) {
        publish(Topics.CHAT.getTopic(), myNickName + ":" + message);

    }

    public void reconnect() {
        synchronized (this) {
            try {
                System.out.println("reconnecting..");
                sampleClient.close(true);
                sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                System.out.println("paho-client connecting to broker: " + broker);
                sampleClient.connect(connOpts);
                System.out.println("client is connected ?? " + sampleClient.isConnected());
            } catch (MqttException ex) {
                Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void publish(String topic, String message) {

        if (WatsonManager.getInstance().isMute()) {
            System.out.println(ConsoleColors.ANSI_RED + "[MQTT] BAN HAMMER IS ACTIVE: " + ConsoleColors.ANSI_YELLOW + " the attempt to communicate with client has been blocked. Mute flag is active.");
            return;
        }
        try {
            //message = CryptoManager.getInstance().encrypt(message);
            System.out.println("PUBLISHING TOPIC: " + topic);
            System.out.println("PUBLISHING MESSAGE: " + message);

            System.out.println(LogTitles.SERVER.getTitle() + "Checling client status: " + (sampleClient.isConnected() ? "ONLINE" : "OFFLINE"));
//            message = Base64.getEncoder().encodeToString(message.getBytes()); //BASE 64
            MqttMessage mx = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
            mx.setQos(qos);
            if (sampleClient.isConnected()) {
                sampleClient.publish(topic, mx);
            } else {
                reconnect();
                sampleClient.publish(topic, mx);

            }

        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        System.out.println("connection lost..");
    }

    @Override
    public synchronized void messageArrived(String topic, MqttMessage mm) {

        System.out.println("TOPIC: " + topic);
        System.out.println("MESSAGE: " + new String(mm.getPayload(), StandardCharsets.UTF_8));
        String message = new String(mm.getPayload(), StandardCharsets.UTF_8);

        if (topic.startsWith(Topics.CHAT.getTopic())) {
            List<String> tid = MQTTServer.topicids;
            System.out.println("listing topic: " + tid);
            for (String t : tid) {
                if (topic.equals(t)) {
                    String id = topic.split("/")[1];
                    System.out.println(LogTitles.SERVER.getTitle() + "message received from: " + t + ", with id: " + id);
                    try {
                        LoggerManager.getInstance().log(LoggingTag.USER_TURNS.getTag() + " " + message);
                    } catch (LogOffException | InvalidAttemptToLogException ex) {
                        System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                    }
                    System.out.println(LogTitles.SERVER.getTitle() + "ENTERING WATSON WORLD and client is: " + (sampleClient.isConnected() ? "ONLINE" : "OFFLINE"));
                    String risposta = WatsonManager.getInstance().sendMessage(message, id);
                    boolean badLuck = false;
                    if (risposta.endsWith(WatsonManager.BAD_LUCK)) {
                        badLuck = true;
                        risposta = risposta.replace(WatsonManager.BAD_LUCK, "");
                    }
                    if (risposta.equals(rispostaPrecedente) && badLuck) {
                        MQTTServer.increaseResetTurns(id);
                    } else {
                        MQTTServer.restartResetTurns(id);
                    }
                    if (MQTTServer.getResetTurns(id) == WatsonManager.getInstance().getMaxDeadlocks()) {
                        WatsonManager.getInstance().automaticHardReset(id);
                        System.out.println(ConsoleColors.ANSI_YELLOW + "EFFETTUATO HARD RESET AUTOMATICO" + ConsoleColors.ANSI_RESET);
                        try {
                            LoggerManager.getInstance().log(LoggingTag.WATSON_HARD_RESET.getTag() + "AUTOMATIC!");
                        } catch (LogOffException | InvalidAttemptToLogException ex) {
                            System.out.println(LogTitles.SERVER.getTitle() + ex.getMessage());
                        }
                    } else {
                        System.out.println(LogTitles.SERVER.getTitle() + "EXITING WATSON WORLD and client is: " + (sampleClient.isConnected() ? "ONLINE" : "OFFLINE"));
                        System.out.println(LogTitles.SERVER.getTitle() + "going to publish the answer: " + risposta);
                        publish(MQTTServer.idTopicMap.get(t), risposta);
                    }
                    HistoryBook.getInstance().addHistoryElement(message, risposta);
                    rispostaPrecedente = risposta;
                }
            }
        }
        if (topic.startsWith(Topics.LOG.getTopic())) {
            System.out.println(LogTitles.SERVER.getTitle() + "logging message from app device");
            try {
                LoggerManager.getInstance().log(message);
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
            }
        }
        if (topic.startsWith(Topics.USERNAME.getTopic())) {
            System.out.println(LogTitles.SERVER.getTitle() + ">>>> username changed <<<<<");
            try {
                //message = id:username
                LoggerManager.getInstance().log(LoggingTag.CHANGE_USERNAME.getTag() + " " + message);
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
            }
            String id = topic.split("/")[1];
            idNameMap.put(id, message);
        }
        if (topic.startsWith(Topics.GETDEVICE.getTopic())) {
            System.out.println(LogTitles.SERVER.getTitle() + ">>>> get device <<<<<");
            //message = id:device
            String[] split = message.split(":");
            String id = split[0];
            String device = split[1];
            MQTTServer.updateDeviceType(id, DeviceType.of(device));
            
            try {
                LoggerManager.getInstance().log(LoggingTag.DEVICE.getTag() + " " + device);
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
            }
        }
        if (topic.startsWith(Topics.BUTTON_PRESSED.getTopic()) && message.equals(LoggingTag.SPEAK.getTag())) {
            System.out.println(LogTitles.SERVER.getTitle() + "button speak pressed");
            try {
                //message = id:username
                LoggerManager.getInstance().log(LoggingTag.REC_BUTTON_PRESSED.getTag() + " " + message);
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
            }
        }
        if (topic.startsWith(Topics.BUTTON_PRESSED.getTopic()) && message.equals(LoggingTag.REPEAT.getTag())) {
            System.out.println(LogTitles.SERVER.getTitle() + "button repeat pressed");
            try {
                //message = id:username
                LoggerManager.getInstance().log(LoggingTag.REPEAT.getTag() + " L'utente ha riascoltato l'ultimo messaggio arrivato a lui");
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
            }
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {

    }

}
