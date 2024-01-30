package lk.ijse.chatRoom;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private static Server server;

    private static List<ClientHandler> clients = new ArrayList<>();
    public static List<Socket> sockets = new ArrayList<>();

    private Server() throws IOException {
        serverSocket = new ServerSocket(3000);

    }

    public static Server getInstance() throws IOException {
        return server != null ? server : (server = new Server());
    }

    public void makeSocket() {
        while (!serverSocket.isClosed()) {

            try {
                socket = serverSocket.accept();
                sockets.add(socket);
                System.out.println("clients size" + clients.size());
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                System.out.println("client socket accepted " + socket.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<ClientHandler> getClientList() {
        return clients;
    }

}
