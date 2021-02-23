/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt;

import java.util.Date;
import java.util.List;
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

    String content = "Visit www.hascode.com! :D";

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
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            System.out.println("paho-client connecting to broker: " + broker);
            sampleClient.connect(connOpts);

            /* subscribe section */
            sampleClient.subscribe("UserConnected");
            sampleClient.subscribe(Topics.CHAT.getTopic());
            sampleClient.setCallback(this);

            System.out.println("paho-client connected to broker");
            System.out.println("paho-client publishing message: " + content);

            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("paho-client message published");

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
                System.out.println("CONNESSOOOO");
            } else {
                System.out.println("NOT CONNESSO");
                sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                System.out.println("paho-client connecting to broker: " + broker);

                sampleClient.connect(connOpts);

            }
            sampleClient.subscribe(Topics.ACK_LOGIN.getTopic() + "/" + clientId);
            sampleClient.publish(topic, mx);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendChatMessage(String message) {
        publish(Topics.CHAT.getTopic(), myNickName + ":" + message);

    }

    public void publish(String topic, String message) {
        try {
            //message = CryptoManager.getInstance().encrypt(message);
            System.out.println("PUBLISHING MESSAGE: " + message);
//            message = Base64.getEncoder().encodeToString(message.getBytes()); //BASE 64
            MqttMessage mx = new MqttMessage(message.getBytes());
            mx.setQos(qos);
            if (sampleClient.isConnected()) {
                System.out.println("CONNESSOOOO");
            } else {
                sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                System.out.println("paho-client connecting to broker: " + broker);
                sampleClient.connect(connOpts);
                System.out.println("NOT CONNESSO");
            }
            sampleClient.publish(topic, mx);
        } catch (MqttException ex) {
            Logger.getLogger(MQTTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void connectionLost(Throwable thrwbl) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage mm) {

        System.out.println("TOPIC: " + topic);
        System.out.println("MESSAGE: " + new String(mm.getPayload()));
        String message = new String(mm.getPayload());

        if (topic.startsWith("chat")) {
            List<String> tid = MQTTServer.topicids;
            for (String t : tid) {
                if (topic.equals(t)) {
                    String id = topic.split("/")[1];
                    System.out.println("message received from: " + t+", with id: "+id);
                    String risposta = WatsonManager.getInstance().sendMessage(message, id);
                    publish(MQTTServer.idTopicMap.get(t), risposta);

                }
            }
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {

    }

}
