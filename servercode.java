import java.io.*;
import java.net.*;
import java.util.concurrent.locks.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class servercode {
    private static int accountA = 1000;
    private static int accountB = 1000;
    private static int accountC = 1000;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final String LOG_FILE = "transaction_log.txt";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected...");
            new ClientHandler(clientSocket).start();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                String request;
                while ((request = in.readLine()) != null) {
                    System.out.println("Received request: " + request); // Debug statement
                    String response = processRequest(request.trim());
                    System.out.println("Sending response: " + response); // Debug statement
                    out.println(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String processRequest(String request) {
            String[] parts = request.split(",");
            String operation = parts[0].toUpperCase();

            lock.lock();
            try {
                String logMessage;

                switch (operation) {
                    case "ADD":
                    case "SUBTRACT":
                        if (parts.length != 3) {
                            return "Error: Invalid command format. Use " + operation + ",ACCOUNT,VALUE.";
                        }
                        String account = parts[1];
                        int value;
                        try {
                            value = Integer.parseInt(parts[2]);
                        } catch (NumberFormatException e) {
                            return "Error: Invalid value. Must be an integer.";
                        }
                        if (value <= 0) return "Error: Value must be a positive integer.";
                        updateBalance(account, operation.equals("ADD") ? value : -value);
                        logMessage = (operation.equals("ADD") ? "Added " : "Subtracted ") + value + " to " + account;
                        logTransaction(logMessage);
                        return logMessage;

                    case "TRANSFER":
                        if (parts.length != 4) {
                            return "Error: Invalid command format. Use TRANSFER,ACCOUNT1,ACCOUNT2,VALUE.";
                        }
                        String sourceAccount = parts[1];
                        String targetAccount = parts[2];
                        try {
                            value = Integer.parseInt(parts[3]);
                        } catch (NumberFormatException e) {
                            return "Error: Invalid value. Must be an integer.";
                        }
                        if (value <= 0) return "Error: Value must be a positive integer.";
                        updateBalance(sourceAccount, -value);
                        updateBalance(targetAccount, value);
                        logMessage = "Transferred " + value + " from " + sourceAccount + " to " + targetAccount;
                        logTransaction(logMessage);
                        return logMessage;

                    case "BALANCE":
                        if (parts.length != 1) {
                            return "Error: Invalid command format. Use BALANCE.";
                        }
                        return getAccountBalances();

                    default:
                        return "Error: Unknown operation.";
                }
            } finally {
                lock.unlock();
            }
        }

        private void updateBalance(String account, int value) {
            switch (account.toUpperCase()) {
                case "A": accountA += value; break;
                case "B": accountB += value; break;
                case "C": accountC += value; break;
                default: throw new IllegalArgumentException("Invalid account: " + account);
            }
        }

        private String getAccountBalances() {
            return String.format("Account A: %d, Account B: %d, Account C: %d", accountA, accountB, accountC);
        }

        private void logTransaction(String message) {
            File logFile = new File(LOG_FILE);
            File parentDir = logFile.getParentFile();

            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs(); // Create directories if they don't exist
            }

            try (FileWriter fw = new FileWriter(logFile, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                out.println(timestamp + " - " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
