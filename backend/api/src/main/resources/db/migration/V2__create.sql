create table room (
    id int primary key auto_increment,
    name varchar(255)
);

create table chat_message (
    id int primary key auto_increment,
    user_id varchar(255),
    room_id int,
    content mediumtext
)