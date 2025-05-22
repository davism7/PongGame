package org.example;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Player1 extends JFrame {
    private static final String SENDER_ID = "PLAYER1";

    public Player1() {
        setTitle(SENDER_ID);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        PongPanel pongPanel = new PongPanel();
        ChatPanel chatPanel = new ChatPanel(SENDER_ID);

        add(pongPanel, BorderLayout.CENTER);
        add(chatPanel, BorderLayout.SOUTH);

        new Publisher();
        new Subscriber(SENDER_ID);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        Player1 pongGame = new Player1();
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pongGame.connect();
        } catch (IOException e) {
            System.exit(1);
        }
    }

    private void connect () throws IOException {
        PongLogic.getInstance().setWhoAmI(PongLogic.SERVER);
        Server server = new Server();
        if (server.isReady()) {
            Thread thread = new Thread(server);
            thread.start();
        }
    }
}