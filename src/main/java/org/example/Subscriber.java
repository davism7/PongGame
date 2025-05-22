
package org.example;
import org.eclipse.paho.client.mqttv3.*;

/**
 * MQTT subscriber that listens for messages on a shared topic and updates ChatHandler.
 * Connects to the broker at broker.hivemq.com and subscribes to the topic "cal-poly/csc/309".
 * Filters out messages from the local sender and passes incoming messages to ChatHandler.
 * Is Used to receive and propagate chat messages in real-time.
 *
 * @author Davis Morales
 * @version 1.0
 */

public class Subscriber implements MqttCallback {
    private final static String BROKER = "tcp://broker.hivemq.com:1883";
    private final static String TOPIC = "cal-poly/csc/309";
    private static final String CLIENT_ID = MqttClient.generateClientId();

    public Subscriber(String senderId) {

        try {
            MqttClient client = new MqttClient(BROKER, CLIENT_ID);
            client.setCallback(this);
            client.connect();
            System.out.println("Connected to BROKER: " + BROKER);
            client.subscribe(TOPIC, (t, message) -> {
                ChatHandler.Data coord = ChatHandler.getInstance()
                        .fromPayload(new String(message.getPayload()));
                if (!coord.senderId.equals(senderId)) {
                    ChatHandler.getInstance().insert(coord);
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost: " + throwable.getMessage());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        System.out.println("Message arrived. Topic: " + s +
                " Message: " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery complete: " + token.getMessageId());
    }
}