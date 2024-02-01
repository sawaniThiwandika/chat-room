package lk.ijse.chatRoom;

import lk.ijse.chatRoom.bo.Impl.MessageBoImpl;
import lk.ijse.chatRoom.bo.MessageBo;
import lk.ijse.chatRoom.controller.ChatFormController;
import lk.ijse.chatRoom.dto.MessageDto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
    private Socket socket;
    private List<ClientHandler> clients;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String msg = "";
    MessageBo messageBo=new MessageBoImpl();
    int x=0;

    public ClientHandler(Socket socket) {

        try {
            this.socket = socket;
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
           // loadAllChats();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (socket.isConnected()) {// sever hdl dena socket eka
                        //System.out.println("client handler while loop ekaethule mn dan inne");
                        ArrayList<MessageDto> messageDtos = messageBo.loadAllChats();
                        if(x==0) {
                           for (MessageDto messageDto : messageDtos) {
                              // System.out.println(messageDto.getMessage());
                               //System.out.println("message" + messageDto.getMessage());
                               dataOutputStream.writeUTF(messageDto.getUserName() + "-" + messageDto.getMessage());
                               dataOutputStream.flush();
                               x = 1;
                             }
                         }

                        try {
                            msg = dataInputStream.readUTF();

                          //  else{
                            System.out.println( msg.split("-")[0]+" : "+ msg.split("-")[1]);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        if(msg.split("-")[1].equals("left")){
                            System.out.println( msg.split("-")[0]+" left the chat");
                        }
                        else {
                            sendMessageToDatabase(msg);
                            clients = Server.getClientList();
                            for (ClientHandler clientHandler : clients) {
                                if (clientHandler.socket.getPort() != socket.getPort()) {
                                    clientHandler.dataOutputStream.writeUTF(msg);
                                    clientHandler.dataOutputStream.flush();
                                }
                            }
                        }
                    }
                    } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
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
