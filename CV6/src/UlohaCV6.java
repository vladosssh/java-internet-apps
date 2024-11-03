import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class UlohaCV6 extends JFrame {
    private JPanel panel;
    public UlohaCV6() {
        setTitle("Zobrazenie mena zamestnanca");
        setLayout(new FlowLayout());
        //Vytvorenie komponentov
        JTextField firstNameField = new JTextField(10);
        JTextField lastNameField = new JTextField(10);
        JLabel nameLabel = new JLabel("Meno zamestnanca: ");
        JButton showNameButton = new JButton("Zobraziť meno");
        //Pridanie akcie pre button
        showNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                nameLabel.setText("Meno zamestnanca: " + firstName + " " + lastName);
            }
        });
        //Priadnie na panel
        add(new JLabel("Meno:"));
        add(firstNameField);
        add(new JLabel("Priezvisko:"));
        add(lastNameField);
        add(showNameButton);
        add(nameLabel);

        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args) {new UlohaCV6();}
}