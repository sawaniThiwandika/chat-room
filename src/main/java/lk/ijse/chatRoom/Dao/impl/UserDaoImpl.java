package lk.ijse.chatRoom.Dao.impl;

import lk.ijse.chatRoom.Dao.UserDao;
import lk.ijse.chatRoom.dbConnnection.DbConnection;
import lk.ijse.chatRoom.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDaoImpl implements UserDao {

    @Override
    public ArrayList<User> getAllUsers() throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        ArrayList<User> users = new ArrayList<>();
        String sql="select* from user";
        PreparedStatement pstm=connection.prepareStatement(sql);
        ResultSet resultSet= pstm.executeQuery();
        while (resultSet.next()){
            users.add(new User(resultSet.getString(1),resultSet.getString(2)));
        }
        return users;
    }
}
