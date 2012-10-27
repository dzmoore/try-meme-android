create table user (
    id int(21) not null auto_increment,
    username varchar(40) not null,
    password varchar(60) not null,
    active tinyint(1),
    last_mod timestamp default current_timestamp on update current_timestamp,
    salt varchar(40),
    primary key (id)
) engine=InnoDB;	

