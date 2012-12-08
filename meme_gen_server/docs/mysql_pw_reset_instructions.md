Resetting the root password of a MySQL database is trivial if you know the current password if you don’t it is a little trickier. Thankfully it isn’t too difficult to fix, and here we’ll show one possible way of doing so.

If you’ve got access to the root account already, because you know the password, you can change it easily:
gaurav@gaurav:~$ mysql --user=root --pass mysql
Enter password:
 
mysql> update user set Password=PASSWORD('new-password-here') WHERE User='root';
Query OK, 2 rows affected (0.04 sec)
Rows matched: 2  Changed: 2  Warnings: 0
 
mysql> flush privileges;
Query OK, 0 rows affected (0.02 sec)
 
mysql> exit
Bye
Forget the MySQL root password?

However if you don’t know the current password this approach will not work – you need to login to run any commands and without the password you’ll not be able to login!

Thankfully there is a simple solution to this problem also, we just need to start MySQL with a flag to tell it to ignore any username/password restrictions which might be in place. Once that is done you can successfully update the stored details.

First of all you will need to ensure that your database is stopped:
root@gaurav:~# /etc/init.d/mysql stop
 
Now you should start up the database in the background, via the mysqld_safe command:
 
root@gaurav:~# /usr/bin/mysqld_safe --skip-grant-tables &
[1] 4271
Starting mysqld daemon with databases from /var/lib/mysql
mysqld_safe[6763]: started

Here you can see the new job (number “1″) has started and the server is running with the process ID (PID) of 4271.

Now that the server is running with the –skip-grant-tables flag you can connect to it without a password and complete the job:
root@gaurav:~$ mysql --user=root mysql
Enter password:
 
mysql> update user set Password=PASSWORD('new-password-here') WHERE User='root';
Query OK, 2 rows affected (0.04 sec)
Rows matched: 2  Changed: 2  Warnings: 0
 
mysql> flush privileges;
Query OK, 0 rows affected (0.02 sec)
 
mysql> exit
Bye

Now that you’ve done that you just need to stop the server, so that you can go back to running a secure MySQL server with password restrictions in place. First of all bring the server you started into the foreground by typing “fg”, then kill it by pressing “Ctrl+C” afterward.

This will now allow you to start the server:
root@gaurav:~# /etc/init.d/mysql start
Starting MySQL database server: mysqld.
Checking for corrupt, not cleanly closed and upgrade needing tables..

Now everything should be done and you should have regained access to your MySQL database(s); you should verify this by connecting with your new password:
root@gaurav:~# mysql --user=root --pass=new-password-here
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 5 to server version: 5.0.24a-Debian_4-log
 
Type 'help;' or '\h' for help. Type '\c' to clear the buffer.
 
mysql> exit
Bye

That was easy :) Isn’t it?
