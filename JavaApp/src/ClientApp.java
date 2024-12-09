import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientApp {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private JFrame frame;
    private JPanel mainPanel;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField scoreField;
    private JComboBox<String> titleComboBox;

    public ClientApp() {
        // Hlavné okno
        frame = new JFrame("Student Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        // Vytvorenie MenuBar
        JMenuBar menuBar = new JMenuBar();
        // Menu Dat
        JMenu menuData = new JMenu("Data");
        JMenuItem addDataItem = new JMenuItem("Add Data");
        menuData.add(addDataItem);
        // Menu View
        JMenu menuView = new JMenu("View");
        JMenuItem showChartItem = new JMenuItem("Show Chart");
        JMenuItem statsItem = new JMenuItem("Show Stats");
        JMenuItem tableItem = new JMenuItem("Show Table");
        menuView.add(showChartItem);
        menuView.add(statsItem);
        menuView.add(tableItem);
        // Menu Help
        JMenu menuHelp = new JMenu("Help");
        JMenuItem userGuideItem = new JMenuItem("User Guide");
        menuHelp.add(userGuideItem);
        // Pridanie menu do MenuBar
        menuBar.add(menuData);
        menuBar.add(menuView);
        menuBar.add(menuHelp);
        frame.setJMenuBar(menuBar);
        // Panel pre formulár
        createFormPanel();
        // Pridanie akcií
        addDataItem.addActionListener(e -> switchToForm());
        showChartItem.addActionListener(e -> showChart());
        statsItem.addActionListener(e -> showStats());
        userGuideItem.addActionListener(e -> showHelp());
        tableItem.addActionListener(e -> showTable());
        // Zobrazenie hlavného okna
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void createFormPanel() {
        mainPanel = new JPanel(new GridLayout(5,2));
        nameField = new JTextField();
        surnameField = new JTextField();
        scoreField = new JTextField();

        String[] titles = {"None", "Bc.", "Ing.", "MEng.", "PhD.", "prof."};
        titleComboBox = new JComboBox<>(titles);

        JButton addButton = new JButton("Add Student");
        JButton deleteLastButton = new JButton("Delete Last");

        mainPanel.add(new JLabel("Name:"));
        mainPanel.add(nameField);
        mainPanel.add(new JLabel("Surname:"));
        mainPanel.add(surnameField);
        mainPanel.add(new JLabel("Score:"));
        mainPanel.add(scoreField);
        mainPanel.add(new JLabel("Title:"));
        mainPanel.add(titleComboBox);
        mainPanel.add(addButton);
        mainPanel.add(deleteLastButton);

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String scoreText = scoreField.getText().trim();
            String title = (String) titleComboBox.getSelectedItem();
            addStudent(name, surname, scoreText, title);
        });
        deleteLastButton.addActionListener(e -> deleteLastStudent());

    }

    private void switchToForm() {
        frame.getContentPane().removeAll();
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void addStudent(String name, String surname, String scoreText, String title) {
        if (name.isEmpty() || surname.isEmpty() || scoreText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields (Name, Surname, Score, Title).",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int score = Integer.parseInt(scoreText);
            if (score < 0 || score > 100) {
                JOptionPane.showMessageDialog(frame, "Score must be between 0 and 100.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                output.writeUTF("ADD");
                output.writeUTF(name);
                output.writeUTF(surname);
                output.writeUTF(title);
                output.writeInt(score);
                output.flush();

                String response = input.readUTF();
                if ("SUCCESS".equals(response)) {
                    JOptionPane.showMessageDialog(frame, "Student added successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add student.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Server communication error.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Score must be a valid number.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteLastStudent() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("DELETE_LAST");
            output.flush();

            String response = input.readUTF();
            if ("SUCCESS".equals(response)) {
                JOptionPane.showMessageDialog(frame, "Last student deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "No students to delete.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Server communication error.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStats() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("STATS");
            output.flush();

            String stats = (String) input.readObject();
            JOptionPane.showMessageDialog(frame, stats, "Statistics", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to retrieve statistics.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showChart() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("GET");
            output.flush();
            ArrayList<ServerApp.Student> students = (ArrayList<ServerApp.Student>) input.readObject();

            // Vytvorenie datasetu pre graf
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            int index = 1;
            for (ServerApp.Student student : students) {
                String category = index + ". " + student.getName().charAt(0) + student.getSurname().charAt(0);
                dataset.addValue(student.getScore(), "Score", category);
                index++;
            }

            // Vytvorenie grafu
            JFreeChart chart = ChartFactory.createBarChart(
                    "Úspešnosť študentov na všeobecnom testovani",
                    "Poradové číslo (Iniciály študentov)",
                    "Body (0-100)",
                    dataset,
                    org.jfree.chart.plot.PlotOrientation.VERTICAL,
                    false,
                    true,
                    false
            );

            // Zobrazenie grafu
            ChartPanel chartPanel = new ChartPanel(chart);
            chart.getCategoryPlot().getRangeAxis().setRange(0, 100);
            JFrame chartFrame = new JFrame("Student Performance Chart");
            chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            chartFrame.setSize(1000, 600);
            chartFrame.add(chartPanel);
            chartFrame.setVisible(true);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to retrieve data for chart.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void showTable() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeUTF("GET");
            output.flush();

            ArrayList<ServerApp.Student> students = (ArrayList<ServerApp.Student>) input.readObject();

            // Vytvorenie tabuľky
            String[] columnNames = {"Title", "Name", "Surname", "Score"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            for (ServerApp.Student student : students) {
                tableModel.addRow(new Object[]{
                        student.getTitle(),
                        student.getName(),
                        student.getSurname(),
                        student.getScore()
                });
            }

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);

            // Zobrazenie tabuľky v novom okne
            JFrame tableFrame = new JFrame("Student Database");
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tableFrame.setSize(600, 400);
            tableFrame.add(scrollPane);
            tableFrame.setVisible(true);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to retrieve data for table.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void showHelp() {
        JOptionPane.showMessageDialog(frame,
                        "WARNING : CHECK YOUR SERVER CONNECTION\n\n" + "User Guide:\n\n" +
                        "1. **Data > Add Data**: Use this option to add a new student.\n" +
                        "   - Fill in the fields for Name, Surname, Title, and Score.\n" +
                        "   - Click 'Add Student' to save the student to the list.\n" +
                        "   - Use 'Delete Last' to remove the last student from the list.\n\n" +
                        "2. **View > Show Chart**: Displays a bar chart of student scores.\n" +
                        "   - The Y-axis shows scores (fixed from 0 to 100).\n" +
                        "   - The X-axis shows student order with initials.\n\n" +
                        "3. **View > Stats**: Displays interesting statistics about student scores:\n" +
                        "   - Median: The central score in the sorted list.\n" +
                        "   - Average: The mean score.\n" +
                        "   - Highest Score: The best score in the list.\n" +
                        "   - Lowest Score: The worst score in the list.\n\n" +
                        "4. **View > Table**: Displays all students in a tabular format.\n" +
                        "   - Columns: Title, Name, Surname, and Score.\n\n" +
                        "For any issues, contact the administrator or refer to the documentation.",
                "Help", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientApp::new);
    }
}