package Delete;

import java.io.*;
import java.net.Socket;
//TODO: delete Client.SendToServer class
import clientServer.SendToServer;
public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // create input stream for the client socket
            InputStream input = clientSocket.getInputStream();
            ObjectInputStream in = new ObjectInputStream(input);

            //TODO: chang class SendToServer to CustomerInput
            SendToServer inputLine;
            while ((inputLine = (SendToServer) in.readObject()) != null) {
                //TODO:
                System.out.println("Received message from client: " + inputLine.getKindOfHelp() + " => " + inputLine.getPhone());
                if (inputLine.equals("help")) {
                    //TODO: ExcelReader function
                    sayHello();
                }
            }

            // close the client socket
            //in.close();
            //clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void sayHello() {
        System.out.println("Hello, world!");
    }
}