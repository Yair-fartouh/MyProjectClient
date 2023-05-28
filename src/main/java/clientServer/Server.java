package clientServer;
public interface Server {
        final int PORT = 9999;
        final String HOST = "localhost";
        void connectToServer();
        void closeConnection();
}
