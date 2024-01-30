package lk.ijse.chatRoom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Getter
public class Message_Details {
   String userName;
   LocalDate date;
   String message;
   String fromMessage;
   LocalTime sendTime;
}
