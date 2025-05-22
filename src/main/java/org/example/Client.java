package org.example;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {

    private static final String IP = "localhost";
    private static final int PORT = 8888;

    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private final boolean isReady;

    public Client() throws IOException {
        Socket socket = new Socket(IP, PORT);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        isReady = true;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 / 30);
                receive();
                send();
            } catch (Exception e) {
                //throw new RuntimeException(e);
            }
        }
    }

    private void send() throws IOException {
        Repository repository = PongLogic.getInstance().getPongData().clone();
        outputStream.writeObject(repository);
        outputStream.flush();
    }

    private void receive() {
        try {
            Repository repository = (Repository) inputStream.readObject();
            PongLogic.getInstance().setServerPlayerY(repository.getServerPlayerY());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isReady() {
        return isReady;
    }

}