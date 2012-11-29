meme gen project
====================

## Committed State:
This project consists of a simple web application using Spring MVC, Hibernate, 
and MySQL, running on the tomcat app server, as well as a simple Android
application.  Currently, the Android app is able to fetch the attributes of a meme: 
+ JSON object: top/bottom text, top/bottom font size, meme type, meme background type
+ Bitmap: the meme background

Storing a meme is currently implemented and is accessible through the 'Save' button on the 'create meme' view.

## History:
### Nox-28-2012
+ Updated above description. 
+ getting/storing 'meme' objects is currently functional and barebones implemented in memeGenAndroid
+ font now has a black text shadow
+ seekbars have been added and configured to allow changing of the top/bottom font size
+ memeGenServer has been deployed to 'dylanxpeth', an always-on (development) laptop running windows xp with a cygwin ssh server and a native windows mysql server.


### Oct-25-2012
+ Static meme is now working. still need to get 'correct' font and slightly padding adjustment. 

### Oct-23-2012
+ Successfully ran ViewMeme activity in a 2.2 emulator. 2.2 apparently has an issue with titlebars.  this issue is resolved an this activity can be launched in a 2.2 AVD

### Oct-20-2012
+ Simple protocol that responds to requests for domain information stored in the database.
+ create and add Android project to this repo

### Oct-19-2012
+ Create a buildable web app using Spring MVC and Hibernate that interfaces with MySQL and can be deployed to tomcat

