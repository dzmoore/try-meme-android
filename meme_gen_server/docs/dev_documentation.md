Dev Documentation
====================

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