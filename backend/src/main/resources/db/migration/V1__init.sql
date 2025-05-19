create table users
(
    id         varchar(255) primary key,
    email      varchar(255) unique,
    password   varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    roles      varchar(255)
)