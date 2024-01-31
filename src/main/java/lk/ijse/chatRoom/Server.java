package lk.ijse.chatRoom;
import lk.ijse.chatRoom.bo.Impl.UserBoImpl;
import lk.ijse.chatRoom.bo.UserBo;
import lk.ijse.chatRoom.dto.UserDto;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private static Server server;

    private static List<ClientHandler> clients = new ArrayList<>();
    private static List<String> clientsUserNames = new ArrayList<>();
    public static List<Socket> sockets = new ArrayList<>();
    UserBo userBo = new UserBoImpl();
    private Server() throws IOException {
        serverSocket = new ServerSocket(3000);
        System.out.println("Server has started now!!!");
    }

    public static Server getInstance() throws IOException {
        return server != null ? server : (server = new Server());
    }

    public void makeSocket() {
        while (!serverSocket.isClosed()) {

            try {
                socket = serverSocket.accept();
                sockets.add(socket);
               // System.out.println("clients size" + clients.size());
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

    public boolean checkUser(String userName,String password) throws SQLException {
        if (userBo.checkUserNamePassword(new UserDto(userName, password))) {
            for (String client : clientsUserNames) {
                if (userName.equals(client)) {
                    return false;
                }
            }
            clientsUserNames.add(userName);
            System.out.println(userName+" join to chat");
            return true;

        }
        return false;
    }
}
