package volunteerServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class VolunteerServer {
    private static final int PORT = 8000;
    private List<VolunteerHandler> volunteerHandlers = new ArrayList<>();

    public void start() {
        try {
            // Create a server socket on the specified port
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Volunteer server listening on port " + PORT);

            // Listen for incoming connections from volunteers
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Volunteer connected: " + socket.getInetAddress().getHostAddress());

                // Create a new VolunteerHandler to handle the volunteer's requests
                VolunteerHandler volunteerHandler = new VolunteerHandler(socket);
                volunteerHandlers.add(volunteerHandler);

                // Start the volunteer handler in a new thread
                Thread thread = new Thread(volunteerHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        VolunteerServer server = new VolunteerServer();
        server.start();
    }

    public void removeVolunteer(VolunteerHandler volunteerHandler) {
        volunteerHandlers.remove(volunteerHandler);
    }
}