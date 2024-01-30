package lk.ijse.chatRoom.bo;

import lk.ijse.chatRoom.dto.UserDto;

import java.sql.SQLException;

public interface UserBo {

    boolean checkUserNamePassword(UserDto userDto) throws SQLException;
    boolean saveUser(UserDto userDto) throws SQLException;
}
