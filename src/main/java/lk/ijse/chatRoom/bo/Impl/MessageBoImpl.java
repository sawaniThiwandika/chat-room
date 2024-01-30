package lk.ijse.chatRoom.bo.Impl;

import lk.ijse.chatRoom.Dao.MessageDao;
import lk.ijse.chatRoom.Dao.impl.MessageDaoImpl;
import lk.ijse.chatRoom.bo.MessageBo;
import lk.ijse.chatRoom.dto.MessageDto;
import lk.ijse.chatRoom.entity.Message_Details;

import java.sql.SQLException;
import java.util.ArrayList;

public class MessageBoImpl implements MessageBo {
    MessageDao messageDao=new MessageDaoImpl();
    @Override
    public ArrayList<MessageDto> loadAllChats() throws SQLException {
        ArrayList<MessageDto> messages = new ArrayList<>();
        ArrayList<Message_Details> messageDetails = messageDao.loadAllChats();
        for(Message_Details detail:messageDetails){
            messages.add(new MessageDto(detail.getUserName(),detail.getDate(),detail.getMessage(),detail.getFromMessage(),detail.getSendTime()));
        }
        return messages;
    }

    @Override
    public boolean saveMessage(MessageDto dto) throws SQLException {
        return messageDao.save(new Message_Details(dto.getUserName(),dto.getDate(),dto.getMessage(),dto.getFromMessage(),dto.getSendTime()));
    }
}
