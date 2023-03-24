package ua.ithillel.javapro.internet.client;

import org.apache.commons.io.IOUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(InetAddress.getByName("localhost"), 8080));

        Scanner scanner = new Scanner(System.in);

        try {

            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {

                try (is) {

                    while (true) {

                        String response = is.readUTF();

                        if (response != null) {

                            if ("bye".equals(response)) {
                                break;
                            }

                            System.out.println("Server: " + response);
                        }

                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }).start();

            while (true) {

                System.out.println("Enter message:");

                String message = scanner.nextLine();

                if ("exit".equals(message)) {
                    break;
                }

                os.writeUTF(message);
                os.flush();

            }

        } finally {
            socket.close();
        }

    }

}
