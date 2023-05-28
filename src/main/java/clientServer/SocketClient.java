package clientServer;

import DTO.VolunteerDTO;
import SQL_connection.AuthenticationService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient implements Server {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public SocketClient() {
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void outToServerObject(SendToServer toServer) {
        try {
            out.writeObject(toServer);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthenticationService inFromServerObject() throws IOException, ClassNotFoundException {
        return (AuthenticationService) in.readObject();
    }

    public String inFromServerString() throws IOException, ClassNotFoundException {
        return (String) in.readObject();
    }

    public VolunteerDTO inFromServerDTO() throws IOException, ClassNotFoundException {
        return (VolunteerDTO) in.readObject();
    }

    @Override
    public void connectToServer() {
        try {
            socket = new Socket(HOST, PORT);
            OutputStream outputStream = socket.getOutputStream();
            out = new ObjectOutputStream(outputStream);
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeConnection() {
        try {
            out.close();
            in.close();
            socket.close();
            socket.shutdownOutput();  //מאותת לשרת שסיימתי לשלוח לו נתונים
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}