package lk.ijse.chatRoom.Dao.impl;

import lk.ijse.chatRoom.Dao.MessageDao;
import lk.ijse.chatRoom.dbConnnection.DbConnection;
import lk.ijse.chatRoom.entity.Message_Details;

import java.sql.*;
import java.util.ArrayList;

public class MessageDaoImpl implements MessageDao {

    @Override
    public ArrayList<Message_Details> loadAllChats() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT *FROM message_details";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        ArrayList<Message_Details> messages=new ArrayList<>();

        while(resultSet.next()) {messages.add(new Message_Details(
                resultSet.getString(1),
                resultSet.getDate(2).toLocalDate(),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getTime(5).toLocalTime()));
        }
        return messages;
    }

    @Override
    public boolean save(Message_Details detail) throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="INSERT INTO message_details VALUES (?,?,?,?,?)";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1,detail.getUserName());
        pstm.setDate(2, Date.valueOf(detail.getDate()));
        pstm.setString(3,detail.getMessage());
        pstm.setString(4,detail.getFromMessage());
        pstm.setTime(5,Time.valueOf(detail.getSendTime()));

        return  pstm.executeUpdate()>0;
    }
}
