import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Client");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // GUI Components
        JTextField ipField = new JTextField("localhost", 15);
        JTextField portField = new JTextField("12345", 5);
        JTextField messageField = new JTextField(20);
        JButton sendButton = new JButton("Send");
        JTextArea responseArea = new JTextArea(10, 30);
        responseArea.setEditable(false);

        // Panel Setup
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("IP:"));
        panel.add(ipField);
        panel.add(new JLabel("Port:"));
        panel.add(portField);
        panel.add(new JLabel("Message:"));
        panel.add(messageField);
        panel.add(sendButton);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(new JScrollPane(responseArea), BorderLayout.SOUTH);

        sendButton.addActionListener(e -> {
            String ip = ipField.getText();
            int port = Integer.parseInt(portField.getText());
            String message = messageField.getText();

            try (Socket socket = new Socket(ip, port);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                out.println(message); // Send message
                String response = in.readLine(); // Read server response
                responseArea.append("Server: " + response + "\n");

            } catch (IOException ex) {
                responseArea.append("Error: " + ex.getMessage() + "\n");
            }
        });

        frame.setVisible(true);
    }
}