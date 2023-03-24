package ua.ithillel.javapro.internet.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {

            Socket socket = serverSocket.accept();

            System.out.println("Client connected");

            new Thread(new ClientThread(socket)).start();

        }

    }

}
