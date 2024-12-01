import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Server");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea logArea = new JTextArea(10, 30);
        logArea.setEditable(false);
        frame.add(new JScrollPane(logArea), BorderLayout.CENTER);

        frame.setVisible(true);

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            logArea.append("Server is listening on port 12345\n");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logArea.append("Client connected: " + clientSocket + "\n");

                new Thread(() -> {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                        String clientMessage;
                        while ((clientMessage = in.readLine()) != null) {
                            logArea.append("Client: " + clientMessage + "\n");
                            out.println("Server received: " + clientMessage);
                        }

                    } catch (IOException ex) {
                        logArea.append("Error: " + ex.getMessage() + "\n");
                    }
                }).start();
            }

        } catch (IOException e) {
            logArea.append("Server error: " + e.getMessage() + "\n");
        }
    }
}
