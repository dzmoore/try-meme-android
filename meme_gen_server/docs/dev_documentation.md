Dev Documentation
====================

## 03-Nov-2012
### Database Initialization and Population:
Ensure an empty 'mgsdb' database exists as mentioned previously.  Then run the table creation script:

    mysql -u mgsdb_user -ppassword mgsdb < db_init.sql

A simple script exists for adding new memes into the database: add_meme.sh.  This script relies on the variables at the top of the script:


    imgPath='/full/path/here/tmimitw.jpg';
    memeType='most_int_man';
    memeTypeDesc='The Most Interesting Man in the World';
    topText="I don\'t always test my code, but when I do..."
    bottomText="it\'s in production."

This script should be fleshed out a bit further, but it will be sufficient for getting started.  I think a symlink should be made at the root level of the current environment, such that all files in the database reside from a single location.  Example:

    ln -s /home/dylan/meme_imgs /meme_imgs
    ls -al / | grep meme_imgs

    lrwxrwxrwx   1 root root    21 Nov  3 09:52 meme_imgs -> /home/dylan/meme_imgs

    


## 20-Oct-2012
### Database / Hibernate Configuration:
create a user table in the db:  
    
    create table user (
        id int(21) not null auto_increment,
        username varchar(40) not null,
        password varchar(60) not null,
        active tinyint(1),
        last_mod timestamp default current_timestamp on update current_timestamp,
        salt varchar(40),
        primary key (id)
    ) engine=InnoDB;	

+ Run hibernate tools reverse engineering to generate annotated domain classes
+ src/main/webapp/WEB-INF/root-context.xml loads db.xml.  db.xml reads from src/main/resources/db.properties.
+ db.xml establishes a 'sessionFactory' bean that will be autowired into controller classes
+ db.xml: annotatedClasses list must be updated with the classes that should be mapped to the database
	
	

## 19-Oct-2012

### Already Installed:
+ Eclipse Indigo --upgraded_to--> Juno
+ Maven Plugin
+ Android SDK
+ Android Eclipse Plugin
+ MySQL server v 5.5
+ Apache Tomcat Version 7.0.23

### Downloaded/Installed:
+ JBoss Hibernate Tools

### Database:

##### create user and database:

	mysql -u root -p
	
	create user 'mgsdb_user'@'localhost' identified by 'password';
	create database mgsdb;
	grant  
		select,insert,update,delete,create,drop
		on mgsdb.*
		to 'mgsdb_user'@'localhost';
