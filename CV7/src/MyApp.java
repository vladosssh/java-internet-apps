import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;

public class MyApp extends JFrame {
    private JTextField firstNameField, lastNameField;
    private JComboBox<String> titleComboBox;
    private JList<String> nameList;
    private DefaultListModel<String> listModel;
    private JPanel colorPanel;
    private JSlider redSlider, greenSlider, blueSlider;

    public MyApp() {
        setTitle("My GUI App");
        setLayout(new BorderLayout());

        //Horny panel pre meno a priezvisko
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        firstNameField = new JTextField(10);
        lastNameField = new JTextField(10);
        titleComboBox = new JComboBox<>(new String[]{"Mr.", "Ms.", "M.Eng.", "MSc", "Ing.", "Bc.", "Doc.", "Prof.", "Dr.", "RNDr."});
        inputPanel.add(new JLabel("Meno:"));
        inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Priezvisko:"));
        inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Oslovenie:"));
        inputPanel.add(titleComboBox);

        //Panel pre zoznam a tlacidla
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        nameList = new JList<>(listModel);
        nameList.setVisibleRowCount(10);
        nameList.setFixedCellWidth(200);
        JScrollPane listScrollPane = new JScrollPane(nameList);

        //Tlacidla
        JPanel buttonColumn = new JPanel();
        buttonColumn.setLayout(new BoxLayout(buttonColumn, BoxLayout.Y_AXIS));
        JButton addButton = new JButton("Pridať");
        JButton removeSelectedButton = new JButton("Vymazať vybrané");
        JButton clearListButton = new JButton("Vymazať všetko");
        buttonColumn.add(addButton);
        buttonColumn.add(Box.createVerticalStrut(5));
        buttonColumn.add(removeSelectedButton);
        buttonColumn.add(Box.createVerticalStrut(5));
        buttonColumn.add(clearListButton);

        listPanel.add(listScrollPane, BorderLayout.CENTER);
        listPanel.add(buttonColumn, BorderLayout.EAST);

        //Button actions
        addButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String title = (String) titleComboBox.getSelectedItem();
            if (!firstName.isEmpty() && !lastName.isEmpty()) {
                listModel.addElement(title + " " + firstName + " " + lastName);
                firstNameField.setText("");
                lastNameField.setText("");
            }
        });
        removeSelectedButton.addActionListener(e -> {
            int selectedIndex = nameList.getSelectedIndex();
            if (selectedIndex != -1) {
                listModel.remove(selectedIndex);
            }
        });
        clearListButton.addActionListener(e -> listModel.clear());

        //RGB sliders
        JPanel rgbPanel = new JPanel();
        rgbPanel.setLayout(new BorderLayout());
        redSlider = new JSlider(0, 255, 128);
        greenSlider = new JSlider(0, 255, 128);
        blueSlider = new JSlider(0, 255, 128);

        Dimension sliderSize = new Dimension(300, 40);
        redSlider.setPreferredSize(sliderSize);
        greenSlider.setPreferredSize(sliderSize);
        blueSlider.setPreferredSize(sliderSize);

        redSlider.setBorder(BorderFactory.createTitledBorder("Red"));
        greenSlider.setBorder(BorderFactory.createTitledBorder("Green"));
        blueSlider.setBorder(BorderFactory.createTitledBorder("Blue"));

        //Color panel
        colorPanel = new JPanel();
        colorPanel.setPreferredSize(new Dimension(300, 200));
        colorPanel.setBackground(new Color(128, 128, 128));

        //Vlozenie do rgb panelu
        rgbPanel.add(redSlider, BorderLayout.NORTH);
        rgbPanel.add(greenSlider, BorderLayout.CENTER);
        rgbPanel.add(blueSlider, BorderLayout.SOUTH);
        rgbPanel.add(colorPanel, BorderLayout.EAST);

        //Aktualizacia farby
        ChangeListener sliderChangeListener = e -> {
            int red = redSlider.getValue();
            int green = greenSlider.getValue();
            int blue = blueSlider.getValue();
            colorPanel.setBackground(new Color(red, green, blue));
        };
        redSlider.addChangeListener(sliderChangeListener);
        greenSlider.addChangeListener(sliderChangeListener);
        blueSlider.addChangeListener(sliderChangeListener);

        add(inputPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
        add(rgbPanel, BorderLayout.SOUTH);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyApp::new);
    }
}
