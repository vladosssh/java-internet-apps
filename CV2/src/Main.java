import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        // Create a JFrame
        JFrame frame = new JFrame("Clicker App");

        // Set the default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the frame
        frame.setSize(500, 300);

        // Create a JPanel to hold components
        JPanel panel = new JPanel();

        // Create a button
        JButton button = new JButton("Don't click Me!");
        JButton button1 = new JButton("Better click Me!");

        // Add an ActionListener to the button
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show a message dialog when the button is clicked
                JOptionPane.showMessageDialog(frame, "But you Clicked!");
            }
        });
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e2) {
                JOptionPane.showMessageDialog(frame, "Great!");
            }
        });

        // Add the button to the panel
        panel.add(button);
        panel.add(button1);
        // Add the panel to the frame
        frame.add(panel);
        // Set the frame to be visible
        frame.setVisible(true);
    }
}