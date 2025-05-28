alter table chat_room
    drop user_id;
alter table chat_summary
    add column user_id varchar(255);