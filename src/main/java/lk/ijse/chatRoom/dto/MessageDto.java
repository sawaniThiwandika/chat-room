package lk.ijse.chatRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    String userName;
    LocalDate date;
    String message;
    String fromMessage;
    LocalTime sendTime;
}
