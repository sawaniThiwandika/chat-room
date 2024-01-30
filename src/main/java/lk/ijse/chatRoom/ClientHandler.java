package lk.ijse.chatRoom;

import lk.ijse.chatRoom.bo.Impl.MessageBoImpl;
import lk.ijse.chatRoom.bo.MessageBo;
import lk.ijse.chatRoom.dto.MessageDto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ClientHandler {
    private Socket socket;
    private List<ClientHandler> clients;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String msg = "";
    MessageBo messageBo=new MessageBoImpl();

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (socket.isConnected()) {// sever hdl dena socket eka
                        //System.out.println("client handler while loop ekaethule mn dan inne");
                        msg = dataInputStream.readUTF();
                        sendMessageToDatabase(msg);
                        clients = Server.getClientList();
                        for (ClientHandler clientHandler : clients) {
                            if (clientHandler.socket.getPort() != socket.getPort()) {
                                clientHandler.dataOutputStream.writeUTF(msg);
                                clientHandler.dataOutputStream.flush();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendMessageToDatabase(String msg) {
        String clientName = msg.split("-")[0];
        String msgFromCient = msg.split("-")[1];
        try {
            messageBo.saveMessage(new MessageDto(clientName, LocalDate.now(),msgFromCient,clientName, LocalTime.now()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
