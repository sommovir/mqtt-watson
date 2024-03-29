/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt;

import it.cnr.istc.mw.mqtt.logic.logger.InfoUser;
import static com.hazelcast.client.impl.protocol.util.UnsafeBuffer.UTF_8;
import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.InterceptConnectMessage;
import io.moquette.interception.messages.InterceptConnectionLostMessage;
import io.moquette.interception.messages.InterceptDisconnectMessage;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.moquette.server.Server;
import io.moquette.server.config.ClasspathResourceLoader;
import io.moquette.server.config.ResourceLoaderConfig;
import io.netty.buffer.ByteBufUtil;
import static it.cnr.istc.mw.mqtt.MQTTClient.clientId;
import it.cnr.istc.mw.mqtt.exceptions.InvalidAttemptToLogException;
import it.cnr.istc.mw.mqtt.exceptions.LogOffException;
import it.cnr.istc.mw.mqtt.logic.generals.DeviceType;
import it.cnr.istc.mw.mqtt.logic.logger.LogTitles;
import it.cnr.istc.mw.mqtt.logic.logger.LoggerManager;
import it.cnr.istc.mw.mqtt.logic.logger.LoggingTag;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca
 */
public class MQTTServer {

    private List<InfoUser> ON_LINE = new LinkedList<>();
    final Server mqtt_broker = new Server();
    public static List<String> topicids = new LinkedList<>();
    public static Map<String, String> idTopicMap = new HashMap<>();
    private boolean lock = false;
    private boolean serverEntered = false;
    private static Map<String, Integer> resetMap = new  HashMap<>();
    private static Map<String, DeviceType> deviceMap = new  HashMap<>();
    
    public static DeviceType getDeviceType(String id){
        if(deviceMap.isEmpty()){
            return null;
        }
        return deviceMap.get(id);
    }
            
    public static void updateDeviceType(String id, DeviceType deviceType){
        System.out.println("STO AGGIORNANDO LA MAPPA CON: ");
        System.out.println("ID: "+id);
        System.out.println("DEVICE: "+deviceType.name());
        deviceMap.put(id, deviceType);
    }
            
    public static int getResetTurns(String userId){
        return resetMap.get(userId);
    }
    
    public static int clearResetTurns(String userId){
        return resetMap.put(userId,0);
    }
    
    public static void increaseResetTurns(String userId){
        resetMap.put(userId,resetMap.get(userId)+1);
    }
    
    public static void restartResetTurns(String userId){
        resetMap.put(userId,0);
    }
    
    public void stop() {
        mqtt_broker.stopServer();
    }

    public List<String> getTopicids() {
        return topicids;
    }

    public void lock() {
        this.lock = true;
    }

    public void unlock() {
        this.lock = true;
    }

    public void start() throws IOException {

        // we create the MQTT broker..
        // we start the MQTT broker..
        mqtt_broker.startServer(new ResourceLoaderConfig(new ClasspathResourceLoader()),
                Collections.singletonList(new AbstractInterceptHandler() {

                    @Override
                    public String getID() {
                        return "EmbeddedLauncherPublishListener";
                    }

                    @Override
                    public void onDisconnect(InterceptDisconnectMessage idm) {
                        if (idm.getClientID().equals("Server")) {
                            System.out.println(LogTitles.SERVER.getTitle()+"[mqtt] disconnect ignoring s.d.");
                            return;
                        }
                        synchronized (this) {
                            ON_LINE.removeIf(info -> info.getId().equals(idm.getClientID()));
                            System.out.println(LogTitles.SERVER.getTitle()+"DISCONNECT");
                            MQTTClient.getInstance().publish(Topics.USER_DISCONNECTED.getTopic(), idm.getClientID());
                        }
                    }

                    @Override
                    public void onConnectionLost(InterceptConnectionLostMessage iclm) {
                        synchronized (this) {
//                            if (iclm.getClientID().equals("Server")) {
//                                System.out.println("[mqtt] connection lost -  ignoring s.d.");
//                                return;
//                            }
                            ON_LINE.removeIf(info -> info.getId().equals(iclm.getClientID()));
                            System.out.println(LogTitles.SERVER.getTitle()+"LOST");
                            String tid = Topics.CHAT.getTopic() + "/" + iclm.getClientID();
                            idTopicMap.remove(tid, Topics.RESPONSES.getTopic() + "/" + iclm.getClientID());
                            //     MQTTClient.getInstance().unsubscribe(tid);
                            topicids.remove(tid);
                            MQTTClient.getInstance().publish(Topics.USER_DISCONNECTED.getTopic(), iclm.getClientID());
                            
                            try {
                                LoggerManager.getInstance().log(LoggingTag.USER_DISCONNECTED.getTag() + " " + iclm.getClientID());
                            } catch (LogOffException | InvalidAttemptToLogException ex) {
                                System.out.println(LogTitles.LOGGER.getTitle()+ex.getMessage());
                            }
                            
                        }
                        
                        
                        
                    }

                    @Override
                    public void onConnect(InterceptConnectMessage icm) {
                        synchronized (this) {
//                            if (icm.getClientID().equals("Server") && serverEntered) {
//                                System.out.println("[mqtt] ignoring reconection of server");
//                                return;
//                            }
                            if(WatsonManager.getInstance().isTestMode() && ON_LINE.size() == 2){
                                return;
                            }
                            ON_LINE.add(new InfoUser(icm.getClientID(), new Date()));
                            deviceMap.put(icm.getClientID(), DeviceType.UNKNOWN);
                            resetMap.put(icm.getClientID(),0);
                            System.out.println(LogTitles.SERVER.getTitle()+"[info] l'utente [" + icm.getClientID() + "] si è connesso");
                            String tid = Topics.CHAT.getTopic() + "/" + icm.getClientID();
                            String tlog = Topics.LOG.getTopic() + "/" + icm.getClientID();
                            idTopicMap.put(tid, Topics.RESPONSES.getTopic() + "/" + icm.getClientID());
                            topicids.add(tid);
                            MQTTClient.getInstance().subscribe(tid);
                            MQTTClient.getInstance().subscribe(tlog);
                            if (icm.getClientID().equals("Server")) {
                                serverEntered = true;
                            } else {
                                MQTTClient.getInstance().subscribe(Topics.USERNAME.getTopic()+ "/" + icm.getClientID());
                                MQTTClient.getInstance().subscribe(Topics.BUTTON_PRESSED.getTopic()+ "/" + icm.getClientID());
                                try {
                                    LoggerManager.getInstance().log(LoggingTag.USER_CONNECTED.getTag() + " " + icm.getClientID());
                                } catch (LogOffException | InvalidAttemptToLogException ex) {
                                    System.out.println(LogTitles.LOGGER.getTitle()+ex.getMessage());
                                }
                            }
                            
                        }

                    }

                    @Override
                    public void onPublish(InterceptPublishMessage msg) {
                        final String decodedPayload = new String(ByteBufUtil.getBytes(msg.getPayload()), UTF_8);
                        System.out.println(LogTitles.SERVER.getTitle()+"Received on topic: " + msg.getTopicName() + " content: " + decodedPayload);
                        String topic = msg.getTopicName();

                    }
                }));

        MQTTClient.getInstance().setIp_server("127.0.0.1");
        MQTTClient.getInstance().connect();

        System.out.println(LogTitles.SERVER.getTitle()+"started..");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            //app.stop();
            mqtt_broker.stopServer();
            //EMF.close();
        }));

    }

    public synchronized List<InfoUser> getON_LINE() {
        return ON_LINE;
    }

}
