package org.example;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This class handles chat message data in an MQTT-based application using the Observer pattern.
 * It formats and parses chat messages for MQTT transmission and notifies observers when a new message is inserted.
 * The broker used is broker.hivemq.com and the topic is cal-poly/csc/309.
 *
 * @author Davis Morales
 * @version 1.1
 */

public class ChatHandler {
    private static ChatHandler instance;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private ChatHandler() {}

    public static ChatHandler getInstance() {
        if (instance == null) {
            instance = new ChatHandler();
        }
        return instance;
    }

    public static class Data {
        public final String message;
        public final String senderId;
        public final boolean isLocal;

        public Data(String message, String senderId, boolean isLocal) {
            this.message = message;
            this.senderId = senderId;
            this.isLocal = isLocal;
        }
    }

    public void insert(Data coord) {
        support.firePropertyChange("newMessage", null, coord);
    }

    public String toPayload(Data coord) {
        return coord.senderId + "::" + coord.message;
    }

    public Data fromPayload(String payload) {
        String[] parts = payload.split("::", 2);
        String senderId = parts.length > 0 ? parts[0] : "unknown";
        String message = parts.length > 1 ? parts[1] : payload;
        return new Data(message, senderId, false);
    }

    public void addObserver(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
