import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class ServerApp {
    private static final int PORT = 12345;
    private static final ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

                String action = input.readUTF();

                if ("ADD".equals(action)) {
                    String name = input.readUTF();
                    String surname = input.readUTF();
                    String title = input.readUTF();
                    int score = input.readInt();
                    students.add(new Student(name, surname, title, score));
                    output.writeUTF("SUCCESS");
                } else if ("DELETE_LAST".equals(action)) {
                    if (!students.isEmpty()) {
                        students.remove(students.size() - 1);
                        output.writeUTF("SUCCESS");
                    } else {
                        output.writeUTF("EMPTY_LIST");
                    }
                } else if ("STATS".equals(action)) {
                    String stats = calculateStats();
                    output.writeObject(stats);
                } else if ("GET".equals(action)) {
                    output.writeObject(new ArrayList<>(students));
                }
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String calculateStats() {
            if (students.isEmpty()) {
                return "No students available to calculate statistics.";
            }

            ArrayList<Integer> scores = new ArrayList<>();
            for (Student student : students) {
                scores.add(student.getScore());
            }

            Collections.sort(scores);

            double median;
            if (scores.size() % 2 == 0) {
                median = (scores.get(scores.size() / 2 - 1) + scores.get(scores.size() / 2)) / 2.0;
            } else {
                median = scores.get(scores.size() / 2);
            }

            double average = scores.stream().mapToInt(Integer::intValue).average().orElse(0);
            int max = Collections.max(scores);
            int min = Collections.min(scores);

            return String.format("Statistics:\n" +
                            "Median: %.2f\n" +
                            "Average: %.2f\n" +
                            "Highest Score: %d\n" +
                            "Lowest Score: %d",
                    median, average, max, min);
        }
    }

    static class Student implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String name;
        private final String surname;
        private final String title;
        private final int score;

        public Student(String name, String surname, String title, int score) {
            this.name = name;
            this.surname = surname;
            this.title = title;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public String getTitle() {
            return title;
        }

        public int getScore() {
            return score;
        }

        @Override
        public String toString() {
            return title + " " + name + " " + surname + " (" + score + ")";
        }
    }
}