package org.example;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A Swing panel for sending and displaying chat messages.
 * Acts as an observer of ChatHandler, updating the UI when new messages are received.
 * Sends user input to ChatHandler, which notifies all registered observers.
 * Used for real-time chat in MQTT-based applications.
 *
 * @author Davis Morales
 * @version 1.1
 */

public class ChatPanel extends JPanel implements PropertyChangeListener {
    private final JTextField textField;
    private final JPanel displayPanel;

    public ChatPanel(String senderId) {
        setLayout(new BorderLayout());

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(600, 50));
        add(textField, BorderLayout.SOUTH);

        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        displayPanel.setPreferredSize(new Dimension(600, 150));
        add(new JScrollPane(displayPanel), BorderLayout.CENTER);

        textField.addActionListener(e -> {
            String text = textField.getText().trim();
            if (!text.isEmpty()) {
                ChatHandler.Data coord = new ChatHandler.Data(text, senderId, true);
                ChatHandler.getInstance().insert(coord);
                textField.setText("");
            }
        });

        ChatHandler.getInstance().addObserver(this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 200);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("newMessage".equals(evt.getPropertyName())) {
            Object value = evt.getNewValue();
            if (value instanceof ChatHandler.Data coord) {
                JLabel messageLabel = new JLabel(coord.senderId + ": " + coord.message);
                messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                displayPanel.add(messageLabel);
                displayPanel.revalidate();
                displayPanel.repaint();
            }
        }
    }
}