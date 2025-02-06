import java.io.*;
import java.net.*;

public class clientcode {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server. Enter commands:");
            String command;
            while ((command = console.readLine()) != null) {
                out.println(command);
                System.out.println("Server: " + in.readLine());
            }
        }
    }
}
