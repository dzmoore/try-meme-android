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

create table lv_meme_type (
	id int(21) not null auto_increment,
	descr varchar(100),
	active tinyint(1),
	primary key (id)
) engine=InnoDB;

create table meme_background (
	id int(21) not null auto_increment,
	active tinyint(1),
	path varchar(200),
	primary key (id)
) engine=InnoDB;

create table meme (
	id int(21) not null auto_increment,
	meme_background_fk int(21),
	lv_meme_type_fk int(21),
	primary key (id),
	foreign key (lv_meme_type_fk) references lv_meme_type(id),
	foreign key (meme_background_fk) references meme_background(id)
) engine=InnoDB;

create table meme_text (
	id int(21) not null auto_increment,
	text varchar(200),
	text_type varchar(20),
	meme_fk int(21),
	primary key (id),
	foreign key (meme_fk) references meme(id)
) engine=InnoDB;

create table sample_meme (
	id int(21) not null auto_increment,
	lv_meme_type_fk int(21),
	meme_fk int(21),
	primary key (id),
	foreign key (lv_meme_type_fk) references lv_meme_type(id),
	foreign key (meme_fk) references meme(id)	
) engine=InnoDB;

