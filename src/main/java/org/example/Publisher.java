package org.example;

import org.eclipse.paho.client.mqttv3.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * MQTT publisher that sends local chat messages to a shared topic using ChatHandler.
 * Observes ChatHandler for new local messages and publishes them to the MQTT broker
 * at broker.hivemq.com on the topic "cal-poly/csc/309".
 * Runs in a background thread and ensures messages are sent in order.
 *
 * @author Davis Morales
 * @version 1.0
 */

public class Publisher implements PropertyChangeListener, Runnable {
    private final static String BROKER = "tcp://broker.hivemq.com:1883";
    private final static String TOPIC = "cal-poly/csc/309";
    private static final String CLIENT_ID = MqttClient.generateClientId();
    private MqttClient client;
    private final List<ChatHandler.Data> list = new ArrayList<>();

    public Publisher() {
        try {
            client = new MqttClient(BROKER, CLIENT_ID);
            client.connect();
            System.out.println("Connected to BROKER: " + BROKER);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        Thread publisherThread = new Thread(this);
        publisherThread.start();

        ChatHandler.getInstance().addObserver(this);
    }

    public void publish(ChatHandler.Data coord) {
        synchronized (list) {
            list.add(coord);
            list.notify();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("newMessage".equals(evt.getPropertyName())) {
            Object newVal = evt.getNewValue();
            if (newVal instanceof ChatHandler.Data coord) {
                if (coord.isLocal) {
                    publish(coord);
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            ChatHandler.Data coord;

            synchronized (list) {
                while (list.isEmpty()) {
                    try {
                        list.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                coord = list.remove(0);
            }

            try {
                MqttMessage message = new MqttMessage(
                        ChatHandler.getInstance().toPayload(coord).getBytes());
                message.setQos(2);
                if (client.isConnected()) {
                    client.publish(TOPIC, message);
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}
