package lk.ijse.chatRoom.bo;

import lk.ijse.chatRoom.dto.MessageDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MessageBo {
    public ArrayList<MessageDto> loadAllChats() throws SQLException;
    public  boolean saveMessage(MessageDto dto) throws SQLException;
}
