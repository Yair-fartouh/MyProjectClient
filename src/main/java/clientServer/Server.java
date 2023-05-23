package clientServer;
public interface Server {
        final int PORT = 9999;
        void connectToServer();
        void closeConnection();
}
