package lk.ijse.chatRoom.Dao;

import lk.ijse.chatRoom.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserDao {
    public ArrayList<User> getAllUsers() throws SQLException;
    public boolean save(User user) throws SQLException;
}
