create database chatRoom;
use chatRoom;
create table user(
                     userName varchar(50) primary key,
                     password varchar(20) not null
);
create table message_details(
                                userName varchar(50) ,
                                date date,
                                message longtext,
                                fromMessage varchar(50),
                                sendTime time,
                                CONSTRAINT  FOREIGN KEY (userName) REFERENCES user (userName) ON DELETE CASCADE ON UPDATE CASCADE
);