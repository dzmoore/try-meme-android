create table user (
    id int(21) not null auto_increment,
    username varchar(40) not null,
    password varchar(60) not null,
    active tinyint(1),
    last_mod timestamp default current_timestamp on update current_timestamp,
    salt varchar(40),
    primary key (id)
) engine=InnoDB;	

create table lv_meme_text_type (
	id int(21) not null auto_increment,
	type varchar(20) not null,
	active tinyint(1),
	primary key (id)	
);

create table lv_meme_type (
	id int(21) not null auto_increment,
	type varchar(20),
	desc varchar(100),
	active tinyint(1),
	primary key (id)
);

create table background_path (
	id int(21) not null auto_increment,
	path varchar(200),
	primary key (id)
);

create table meme_background (
	id int(21) not null auto_increment,
	background_path_fk int(21),
	active tinyint(1),
	primary key (id),
	foreign key (background_path_fk) references background_path(id)
);

create table meme (
	id int(21) not null auto_increment,
	meme_background_fk int(21),
	lv_meme_type_fk int(21),
	primary key (id),
	foreign key (lv_meme_type_fk) references lv_meme_type(id),
	foreign key (meme_background_fk) references meme_background(id)
);

create table meme_text (
	id int(21) not null auto_increment,
	text varchar(200),
	lv_meme_text_type_fk int(21),
	meme_fk int(21),
	primary key (id),
	foreign key (lv_meme_text_type_fk) references lv_meme_text_type(id),
	foreign key (meme_fk) references meme(id)
);
