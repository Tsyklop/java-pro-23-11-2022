package ua.ithillel.javapro.internet.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread implements Runnable {

    private final Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());

            while (true) {

                String clientMessage = is.readUTF();

                if (clientMessage == null) {
                    continue;
                }

                System.out.println("Receive message from client: " + clientMessage);

                os.writeUTF("OK");
                os.flush();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }

    }

}
