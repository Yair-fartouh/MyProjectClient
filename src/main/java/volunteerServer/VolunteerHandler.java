package volunteerServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class VolunteerHandler implements Runnable {

    private Socket socket;

    public VolunteerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                // Get input and output streams from the socket
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            // Here you can implement the code to receive and send data to the volunteer
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the socket and remove the volunteer handler from the list of handlers
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //server.removeVolunteer(this);
        }
    }
}