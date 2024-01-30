package lk.ijse.chatRoom.Dao;

import lk.ijse.chatRoom.entity.Message_Details;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MessageDao {
    public ArrayList<Message_Details> loadAllChats() throws SQLException;
    public boolean save(Message_Details detail) throws SQLException;
}
