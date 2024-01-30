package lk.ijse.chatRoom.bo.Impl;

import lk.ijse.chatRoom.Dao.UserDao;
import lk.ijse.chatRoom.Dao.impl.UserDaoImpl;
import lk.ijse.chatRoom.bo.UserBo;
import lk.ijse.chatRoom.dto.UserDto;
import lk.ijse.chatRoom.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserBoImpl implements UserBo {
UserDao userDao=new UserDaoImpl();
    @Override
    public boolean checkUserNamePassword(UserDto userDto) throws SQLException {
        ArrayList<User> allUsers = userDao.getAllUsers();
        for (User user: allUsers){
        if(userDto.getUserName().equals(user.getUserName())){
            if (userDto.getPassword().equals(user.getPassword())){
                return true;
            }
        }
    }
        return false;
    }

    @Override
    public boolean saveUser(UserDto userDto) throws SQLException {
        return userDao.save(new User(userDto.getUserName(), userDto.getPassword()));
    }
}
