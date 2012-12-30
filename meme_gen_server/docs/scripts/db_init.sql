create database mgsdb;
use mgsdb;

create table user (
    id int(21) not null auto_increment,
    username varchar(40) not null,
    password varchar(60) not null,
    active tinyint(1),
    last_mod timestamp default current_timestamp on update current_timestamp,
    salt varchar(40),
    primary key (id)
) engine=InnoDB;	

create table device_info (
	id int(21) not null auto_increment,
	unique_id varchar(100),
	user_fk int(21),
	last_mod timestamp default current_timestamp on update current_timestamp,
	primary key (id),
	foreign key (user_fk) references user(id)
) engine=InnoDB;

create table lv_meme_type (
	id int(21) not null auto_increment,
	descr varchar(100),
    last_mod timestamp default current_timestamp on update current_timestamp,
	active tinyint(1) default 1,
	primary key (id)
) engine=InnoDB;

create table meme_background (
	id int(21) not null auto_increment,
	active tinyint(1) default 1,
	path varchar(200),
	last_mod timestamp default current_timestamp on update current_timestamp,
	primary key (id)
) engine=InnoDB;

create table meme (
	id int(21) not null auto_increment,
	meme_background_fk int(21),
	lv_meme_type_fk int(21),
	created_by_user_fk int(21),
	is_sample_meme tinyint(1) default 0,
	last_mod timestamp default current_timestamp on update current_timestamp,
	primary key (id),
	foreign key (lv_meme_type_fk) references lv_meme_type(id),
	foreign key (meme_background_fk) references meme_background(id),
	foreign key (created_by_user_fk) references user(id)
) engine=InnoDB;

create table meme_text (
	id int(21) not null auto_increment,
	text varchar(200),
	text_type varchar(20),
	font_size int(3) default 26,
	meme_fk int(21),
	last_mod timestamp default current_timestamp on update current_timestamp,
	primary key (id),
	foreign key (meme_fk) references meme(id)
) engine=InnoDB;

create table user_fav_meme_type (
	id int(21) not null auto_increment,
	meme_type_fk int(21),
	user_fk int(21),
	active tinyint(1) default 1,
	last_mod timestamp default current_timestamp on update current_timestamp,
	primary key (id),
	foreign key (meme_type_fk) references lv_meme_type(id),
	foreign key (user_fk) references user(id)
);

insert into meme_background (active, path) values (1, 'tmimitw.jpg');

insert into meme_background (active, path) values (1, 'most_int.jpg');
insert into lv_meme_type (active, descr) values (1, 'MostIntManWorld');
insert into lv_meme_type (active, descr) values (1, 'Most_Int_Man_In_World');

insert into user (active, username, password) values (1, 'mostintuser', 'password');
insert into user (active, username, password) values (1, 'most_inty_user_world', 'password');

insert into user (username, password) values ('testuser', 'password');

insert into user_fav_meme_type (meme_type_fk, user_fk) values (1, 1);

insert into meme (meme_background_fk, lv_meme_type_fk, created_by_user_fk) values (1, 1, 1);

insert into meme_text (text, text_type, font_size, meme_fk) values ("Top Text Test", "TOP", 26, 1);

insert into meme_text (text, text_type, font_size, meme_fk) values ("Bottom Text Test", "BOTTOM", 26, 1);


insert into meme (meme_background_fk, lv_meme_type_fk, created_by_user_fk, is_sample_meme) values (1, 1, 1, 1);

insert into meme_text (text, text_type, font_size, meme_fk) values ("SampleMeme Top Text Test", "TOP", 26, 2);

insert into meme_text (text, text_type, font_size, meme_fk) values ("SampleMeme Bottom Text Test", "BOTTOM", 26, 2);

