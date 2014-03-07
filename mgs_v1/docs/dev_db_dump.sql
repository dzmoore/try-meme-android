-- MySQL dump 10.13  Distrib 5.5.31, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: mgsdbv1
-- ------------------------------------------------------
-- Server version	5.5.31-0ubuntu0.12.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `crawler_background`
--

DROP TABLE IF EXISTS `crawler_background`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crawler_background` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `crawler_img_desc` varchar(255) DEFAULT NULL,
  `crawler_img_filename` varchar(255) DEFAULT NULL,
  `source_desc` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crawler_background`
--

LOCK TABLES `crawler_background` WRITE;
/*!40000 ALTER TABLE `crawler_background` DISABLE KEYS */;
/*!40000 ALTER TABLE `crawler_background` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crawler_meme`
--

DROP TABLE IF EXISTS `crawler_meme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crawler_meme` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bottom_text` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `top_text` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crawler_meme`
--

LOCK TABLES `crawler_meme` WRITE;
/*!40000 ALTER TABLE `crawler_meme` DISABLE KEYS */;
/*!40000 ALTER TABLE `crawler_meme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_info`
--

DROP TABLE IF EXISTS `device_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `unique_id` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `device_user` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA40484F7C79C845B` (`device_user`),
  CONSTRAINT `FKA40484F7C79C845B` FOREIGN KEY (`device_user`) REFERENCES `meme_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_info`
--

LOCK TABLES `device_info` WRITE;
/*!40000 ALTER TABLE `device_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `device_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lv_popularity_type`
--

DROP TABLE IF EXISTS `lv_popularity_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lv_popularity_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `popularity_type_name` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lv_popularity_type`
--

LOCK TABLES `lv_popularity_type` WRITE;
/*!40000 ALTER TABLE `lv_popularity_type` DISABLE KEYS */;
INSERT INTO `lv_popularity_type` VALUES (1,'popular',0);
/*!40000 ALTER TABLE `lv_popularity_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meme`
--

DROP TABLE IF EXISTS `meme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meme` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `bottom_text` bigint(20) DEFAULT NULL,
  `created_by_user` bigint(20) DEFAULT NULL,
  `meme_background` bigint(20) DEFAULT NULL,
  `top_text` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK331530E025ECA3` (`created_by_user`),
  KEY `FK331530E9FD9B80` (`top_text`),
  KEY `FK33153012E30C47` (`meme_background`),
  KEY `FK331530FF45D86A` (`bottom_text`),
  CONSTRAINT `FK331530FF45D86A` FOREIGN KEY (`bottom_text`) REFERENCES `meme_text` (`id`),
  CONSTRAINT `FK33153012E30C47` FOREIGN KEY (`meme_background`) REFERENCES `meme_background` (`id`),
  CONSTRAINT `FK331530E025ECA3` FOREIGN KEY (`created_by_user`) REFERENCES `meme_user` (`id`),
  CONSTRAINT `FK331530E9FD9B80` FOREIGN KEY (`top_text`) REFERENCES `meme_text` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meme`
--

LOCK TABLES `meme` WRITE;
/*!40000 ALTER TABLE `meme` DISABLE KEYS */;
INSERT INTO `meme` VALUES (1,0,2,1,1,1),(2,0,3,NULL,NULL,4),(3,0,8,15,1,9),(4,0,10,15,1,11),(5,0,12,15,1,13),(6,NULL,15,NULL,1,14),(7,NULL,17,NULL,1,16),(8,NULL,19,NULL,1,18),(9,NULL,21,NULL,3,20),(10,NULL,23,NULL,3,22),(11,NULL,25,NULL,4,24),(12,NULL,27,NULL,4,26),(13,NULL,29,NULL,4,28),(14,NULL,31,NULL,6,30),(15,NULL,33,NULL,6,32),(16,NULL,35,NULL,6,34),(17,NULL,37,NULL,7,36),(18,NULL,39,NULL,7,38),(19,NULL,41,NULL,7,40),(20,NULL,43,NULL,10,42),(21,NULL,45,NULL,10,44),(22,NULL,47,NULL,10,46),(23,NULL,49,NULL,11,48),(24,NULL,51,NULL,11,50),(25,NULL,53,NULL,11,52),(26,NULL,55,NULL,13,54),(27,NULL,57,NULL,13,56),(28,NULL,59,NULL,13,58),(29,NULL,61,NULL,14,60),(30,NULL,63,NULL,14,62),(31,NULL,65,NULL,14,64),(32,NULL,67,NULL,15,66),(33,NULL,69,NULL,15,68),(34,NULL,71,NULL,15,70),(35,NULL,73,NULL,17,72),(36,NULL,75,NULL,17,74),(37,NULL,77,NULL,17,76),(38,NULL,79,NULL,18,78),(39,NULL,81,NULL,18,80),(40,NULL,83,NULL,18,82),(41,NULL,85,NULL,19,84),(42,NULL,87,NULL,19,86),(43,NULL,89,NULL,19,88),(44,NULL,91,NULL,22,90),(45,NULL,93,NULL,22,92),(46,NULL,95,NULL,22,94),(47,NULL,97,NULL,23,96),(48,NULL,99,NULL,23,98),(49,NULL,101,NULL,23,100),(50,NULL,103,NULL,26,102),(51,NULL,105,NULL,27,104),(52,NULL,107,NULL,27,106),(53,NULL,109,NULL,27,108),(54,NULL,111,NULL,28,110),(55,NULL,113,NULL,28,112),(56,NULL,115,NULL,28,114),(57,NULL,117,NULL,29,116),(58,NULL,119,NULL,29,118),(59,NULL,121,NULL,29,120),(60,NULL,123,NULL,30,122),(61,NULL,125,NULL,30,124),(62,NULL,127,NULL,30,126),(63,NULL,129,NULL,31,128),(64,NULL,131,NULL,31,130),(65,NULL,133,NULL,31,132),(66,NULL,135,NULL,32,134),(67,NULL,137,NULL,32,136),(68,NULL,139,NULL,32,138),(69,NULL,141,NULL,33,140),(70,NULL,143,NULL,33,142),(71,NULL,145,NULL,33,144),(72,NULL,147,NULL,34,146),(73,NULL,149,NULL,34,148),(74,NULL,151,NULL,34,150),(75,NULL,153,NULL,35,152),(76,NULL,155,NULL,35,154),(77,NULL,157,NULL,35,156),(78,NULL,159,NULL,37,158),(79,NULL,161,NULL,37,160),(80,NULL,163,NULL,37,162),(81,NULL,165,NULL,38,164),(82,NULL,167,NULL,38,166),(83,NULL,169,NULL,38,168),(84,NULL,171,NULL,40,170),(85,NULL,173,NULL,40,172),(86,NULL,175,NULL,40,174),(87,NULL,177,NULL,41,176),(88,NULL,179,NULL,41,178),(89,NULL,181,NULL,41,180),(90,NULL,183,NULL,42,182),(91,NULL,185,NULL,42,184),(92,NULL,187,NULL,42,186),(93,NULL,189,NULL,43,188),(94,NULL,191,NULL,43,190),(95,NULL,193,NULL,43,192),(96,NULL,195,NULL,44,194),(97,NULL,197,NULL,44,196),(98,NULL,199,NULL,44,198),(99,NULL,201,NULL,45,200),(100,NULL,203,NULL,45,202),(101,NULL,205,NULL,45,204),(102,NULL,207,NULL,46,206),(103,NULL,209,NULL,46,208),(104,NULL,211,NULL,46,210),(105,NULL,213,NULL,48,212),(106,NULL,215,NULL,48,214),(107,NULL,217,NULL,48,216),(108,NULL,219,NULL,49,218),(109,NULL,221,NULL,49,220),(110,NULL,223,NULL,49,222),(111,NULL,225,NULL,50,224),(112,NULL,227,NULL,50,226),(113,NULL,229,NULL,50,228),(114,NULL,231,NULL,51,230),(115,NULL,233,NULL,51,232),(116,NULL,235,NULL,51,234),(117,NULL,237,NULL,53,236),(118,NULL,239,NULL,53,238),(119,NULL,241,NULL,54,240),(120,NULL,243,NULL,54,242),(121,NULL,245,NULL,54,244),(122,NULL,247,NULL,56,246),(123,NULL,249,NULL,56,248),(124,NULL,251,NULL,56,250),(125,NULL,253,NULL,57,252),(126,NULL,255,NULL,57,254),(127,NULL,257,NULL,57,256),(128,NULL,259,NULL,58,258),(129,NULL,261,NULL,59,260),(130,NULL,263,NULL,59,262),(131,NULL,265,NULL,59,264),(132,NULL,267,NULL,61,266),(133,NULL,269,NULL,61,268),(134,NULL,271,NULL,61,270),(135,NULL,273,NULL,62,272),(136,NULL,275,NULL,62,274),(137,NULL,277,NULL,62,276),(138,NULL,279,NULL,63,278),(139,NULL,281,NULL,63,280);
/*!40000 ALTER TABLE `meme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meme_background`
--

DROP TABLE IF EXISTS `meme_background`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meme_background` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meme_background`
--

LOCK TABLES `meme_background` WRITE;
/*!40000 ALTER TABLE `meme_background` DISABLE KEYS */;
INSERT INTO `meme_background` VALUES (1,'','College Freshman','College_Freshman.jpg',0),(3,'','Advice Dog','Advice_Dog.jpg',0),(4,'','Advice God','Advice_God.jpg',0),(5,'','Ancient Aliens Guy','Ancient_Aliens_Guy.jpg',0),(6,'','Annoyed Picard','Annoyed_Picard.jpg',0),(7,'','Annoying Facebook Girl','Annoying_Facebook_Girl.jpg',0),(8,'','Anti Joke Chicken','Anti_Joke_Chicken.jpg',0),(9,'','Art Student Owl','Art_Student_Owl.jpg',0),(10,'','Bad Advice Cat','Bad_Advice_Cat.jpg',0),(11,'','Bear Grylls','Bear_Grylls.jpg',0),(12,'','Bill Oreilly','Bill_Oreilly.jpg',0),(13,'','Business Cat','Business_Cat.jpg',0),(14,'','Butthurt Dweller','Butthurt_Dweller.jpg',0),(15,'','Chemistry Cat','Chemistry_Cat.jpg',0),(16,'','College Freshman','College_Freshman.jpg',0),(17,'','Confession Bear','Confession_Bear.jpg',0),(18,'','Conspiracy Keanu','Conspiracy_Keanu.jpg',0),(19,'','Courage Wolf','Courage_Wolf.jpg',0),(20,'','Crabby Christian','Crabby_Christian.jpg',0),(21,'','Crazy Girlfriend Praying Mantis','Crazy_Girlfriend_Praying_Mantis.jpg',0),(22,'','Dating Site Murderer','Dating_Site_Murderer.jpg',0),(23,'','Depression Dog','Depression_Dog.jpg',0),(24,'','Dwight Schrute','Dwight_Schrute.jpg',0),(25,'','Excited Kitten','Excited_Kitten.jpg',0),(26,'','Family Tech Support Guy','Family_Tech_Support_Guy.jpg',0),(27,'','First World Problems','First_World_Problems.jpg',0),(28,'','Forever Alone','Forever_Alone.jpg',0),(29,'','Foul Bachelor Frog','Foul_Bachelor_Frog.jpg',0),(30,'','Foul Bachelorette Frog','Foul_Bachelorette_Frog.jpg',0),(31,'','Futurama Fry','Futurama_Fry.jpg',0),(32,'','Good Guy Greg','Good_Guy_Greg.jpg',0),(33,'','Grandma Finds The Internet','Grandma_Finds_The_Internet.jpg',0),(34,'','Grumpy Cat','Grumpy_Cat.jpg',0),(35,'','Hawkward','Hawkward.jpg',0),(36,'','Helpful Tyler Durden','Helpful_Tyler_Durden.jpg',0),(37,'','High Expectations Asian Father','High_Expectations_Asian_Father.jpg',0),(38,'','Hipster Kitty','Hipster_Kitty.jpg',0),(39,'','Indifferently Unaware Crush','Indifferently_Unaware_Crush.jpg',0),(40,'','Insanity Wolf','Insanity_Wolf.jpg',0),(41,'','Jimmy Mcmillan','Jimmy_Mcmillan.jpg',0),(42,'','Joseph Ducreux','Joseph_Ducreux.jpg',0),(43,'','Karate Kyle','Karate_Kyle.jpg',0),(44,'','Lame Pun Coon','Lame_Pun_Coon.jpg',0),(45,'','Lazy College Senior','Lazy_College_Senior.jpg',0),(46,'','Musically Oblivious 8th Grader','Musically_Oblivious_8th_Grader.jpg',0),(47,'','Oblivious Suburban Mon','Oblivious_Suburban_Mon.jpg',0),(48,'','Ordinary Muslim Man','Ordinary_Muslim_Man.jpg',0),(49,'','Overly Attached Girlfriend','Overly_Attached_Girlfriend.jpg',0),(50,'','Paranoid Parrot','Paranoid_Parrot.jpg',0),(51,'','Philosoraptor','Philosoraptor.jpg',0),(52,'','Pickup Line Panda','Pickup_Line_Panda.jpg',0),(53,'','Ptsd Clarinet Boy','Ptsd_Clarinet_Boy.jpg',0),(54,'','Rasta Science Teacher','Rasta_Science_Teacher.jpg',0),(55,'','Rebecca Black','Rebecca_Black.jpg',0),(56,'','Redditors Wife','Redditors_Wife.jpg',0),(57,'','Redneck Randal','Redneck_Randal.jpg',0),(58,'','Rich Raven','Rich_Raven.jpg',0),(59,'','Ridiculously Photogenic Guy','Ridiculously_Photogenic_Guy.jpg',0),(60,'','Robot Guy','Robot_Guy.jpg',0),(61,'','Scumbag Girl','Scumbag_Girl.jpg',0),(62,'','Scumbag Steve','Scumbag_Steve.jpg',0),(63,'','Sexually Oblivious Rhino','Sexually_Oblivious_Rhino.jpg',0);
/*!40000 ALTER TABLE `meme_background` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meme_background_popularity`
--

DROP TABLE IF EXISTS `meme_background_popularity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meme_background_popularity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `meme_background_popularity_value` bigint(20) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `lv_popularity_type` bigint(20) DEFAULT NULL,
  `meme_background` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK14EEF517C21000B4` (`lv_popularity_type`),
  KEY `FK14EEF51712E30C47` (`meme_background`),
  CONSTRAINT `FK14EEF51712E30C47` FOREIGN KEY (`meme_background`) REFERENCES `meme_background` (`id`),
  CONSTRAINT `FK14EEF517C21000B4` FOREIGN KEY (`lv_popularity_type`) REFERENCES `lv_popularity_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meme_background_popularity`
--

LOCK TABLES `meme_background_popularity` WRITE;
/*!40000 ALTER TABLE `meme_background_popularity` DISABLE KEYS */;
INSERT INTO `meme_background_popularity` VALUES (1,1,0,1,1);
/*!40000 ALTER TABLE `meme_background_popularity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meme_text`
--

DROP TABLE IF EXISTS `meme_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meme_text` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `font_size` double DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=282 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meme_text`
--

LOCK TABLES `meme_text` WRITE;
/*!40000 ALTER TABLE `meme_text` DISABLE KEYS */;
INSERT INTO `meme_text` VALUES (1,26,'Top Text',0),(2,26,'Bottom Text',0),(3,26,NULL,0),(4,26,'',0),(8,26,'Bottom Text',0),(9,26,'Top Text',0),(10,26,'Bottom Text',0),(11,26,'Top Text',0),(12,26,'Bottom Text',0),(13,26,'Top Text',0),(14,15,'Professor tells him he\'s failing and that he should\'ve come back after class',NULL),(15,26,'\"You never told me to\"',NULL),(16,15,'Takes Psych 101, Sociology 101, Gym elective.',NULL),(17,15,'\"I don\'t see why people stuggle in college. I got a 3.0 my first semester\"',NULL),(18,15,'Drinks Week Old Smoothie of Death',NULL),(19,26,'Dies.',NULL),(20,26,'Meet a new friend',NULL),(21,15,'sniff their ass it lets you know who they really are',NULL),(22,15,'Your boss at work missed the last episode of game of thrones?',NULL),(23,15,'Tell him how it ends so he doesn\'t have to wait until he gets home to know.',NULL),(24,26,'\"Thou shalt not kill.\"',NULL),(25,26,'kills',NULL),(26,15,'For someone who doesn\'t believe in me',NULL),(27,15,'You atheists sure talk about me a lot',NULL),(28,26,'',NULL),(29,26,'',NULL),(30,26,'why the fuck',NULL),(31,15,'would you have a date at red lobster',NULL),(32,26,'why the fuck',NULL),(33,15,'Would your partner tell you they cheated if they were deceptive in the first place ',NULL),(34,26,'why the fuck',NULL),(35,15,'Would anybody bring their 4 and 5 year old kids to see the Evil Dead movie?',NULL),(36,15,'Always posts about how much she loves god',NULL),(37,26,'Gets knocked up ',NULL),(38,15,'Complain about how nobody likes you and how fat you are',NULL),(39,15,'post 1001 pictures of your self ',NULL),(40,26,'',NULL),(41,26,'OMG!',NULL),(42,26,'Sick of bullies?',NULL),(43,26,'Buy a gun!',NULL),(44,26,'thirsty?',NULL),(45,26,'try the toilet!',NULL),(46,26,'Girlfriend thinks she\'s fat',NULL),(47,26,'Buy her bigger clothes',NULL),(48,26,'stubbed my toe ',NULL),(49,26,'Better drink my own piss',NULL),(50,15,'I often make up reasons to drink my own piss',NULL),(51,15,'just because i like to drink my own piss',NULL),(52,26,'When I was a kid',NULL),(53,26,'I pissed in my own bottle',NULL),(54,15,'We should get the BPD to consult us',NULL),(55,26,'on finding that red dot',NULL),(56,15,'Accepted a New Post in the company',NULL),(57,15,'currently sharpening claws on it.',NULL),(58,26,'NIGGUH',NULL),(59,26,'IM A CAT',NULL),(60,26,'My vote\'s important',NULL),(61,26,'My life has meaning',NULL),(62,15,'I should flavor text everything on the front page',NULL),(63,15,'People will think I\'m hilarious',NULL),(64,26,'Girls run away from me',NULL),(65,15,'Because they\'re playing hard to get',NULL),(66,15,'no one makes chemistry cat memes',NULL),(67,15,'I guess all the good ones Argon',NULL),(68,15,'Do I know any jokes about sodium?',NULL),(69,26,'Na',NULL),(70,15,'Protesters in Turkey being doused in O.C spray you say?',NULL),(71,15,'YOU HAVE TO USE YOGURT ON YOUR EYES MY DEAR BOYS!',NULL),(72,15,'If a Facebook post says\n\"Help this kid with cancer get a million likes\"',NULL),(73,26,'I don\'t click like',NULL),(74,26,'I really don\'t give a shit',NULL),(75,26,'about what\'s inside the safe',NULL),(76,15,'I hold the door open for ladies walking behind me until they enter the building',NULL),(77,15,'But the only thing I am doing is checking them out and being thanked afterwards',NULL),(78,26,'what if the hokey pokey',NULL),(79,26,'is what it\'s all about',NULL),(80,15,'What if the confession bear murder post ',NULL),(81,15,'wasn\'t actually a confession bear murder post',NULL),(82,15,'what if caption bot is messing up the captions on purpose',NULL),(83,15,'because its comments get more upvotes that way?',NULL),(84,15,'If you dont want the world to fuck you over',NULL),(85,15,'Quit being such a fucking pussy',NULL),(86,26,'make westboro baptist ',NULL),(87,26,'picket your funeral',NULL),(88,26,'AMERICA',NULL),(89,15,'ready to change your gun laws yet?',NULL),(90,26,'i\'m going to cut you',NULL),(91,26,'a big slice of birthday cake',NULL),(92,26,'I will cut you',NULL),(93,15,'a pice of organic cake \ni made just for you',NULL),(94,15,'I drive by my ex\'s  \ndorm almost everday',NULL),(95,15,'Because it\'s the only quick route to work',NULL),(96,15,'Drinking alone on the night before my birthday',NULL),(97,15,'Not even the bartender would take a shot with me at midnight',NULL),(98,26,'New depression meme?',NULL),(99,26,'Guess everyone forgot about me',NULL),(100,26,'WHY AM I DEPRESSED?',NULL),(101,26,'BORN WITHOUT EYELIDS',NULL),(102,15,'Mom finally gets that I just google problems',NULL),(103,15,'Starts asking me to google things for her',NULL),(104,26,'I want to eat',NULL),(105,15,'but I\'ve just brushed my theeth',NULL),(106,15,'I WANT TO USE MY GEORGE FOREMAN GRILL TONIGHT ',NULL),(107,15,'BUT THEN I\'LL HAVE TO CLEAN IT ',NULL),(108,15,'Someone confessed murder using confession bear',NULL),(109,15,'now my confession won\'t be as good and i have to whore karma',NULL),(110,15,'wAS FUNNY, LOVEABLE, AND NICE ON CRUISE',NULL),(111,26,'ON CRUISE',NULL),(112,15,'I turned 14 and never went to a bar mitzvah',NULL),(113,15,'I turned 17 and never went to a sweet 16',NULL),(114,15,'Receives e-mail out of the blue from the girl of his dreams',NULL),(115,15,'It\'s spam from her hijacked Yahoo account',NULL),(116,26,'Laundry Basket is full?',NULL),(117,15,'Time to get a bigger laundry basket.',NULL),(118,26,'cat throws up on the floor',NULL),(119,26,'no worries, he\'ll eat it later',NULL),(120,26,'Cat pukes on the floor',NULL),(121,26,'CAll dog in to \n\"clean up\"',NULL),(122,15,'wears low-cut shirt while studying',NULL),(123,15,'unable to focus because distracted by own cleavage',NULL),(124,15,'Stealing the batteries out of the remote',NULL),(125,26,'for your vibrator',NULL),(126,26,'Dropped Mac & Cheese on carpet',NULL),(127,15,'Picked out the hair and still ate it',NULL),(128,26,'not sure if i actually care',NULL),(129,26,'or feel obligated to care',NULL),(130,26,'not sure if i actually care',NULL),(131,26,'or feel obligated to care',NULL),(132,26,'not sure if /r/worldnews',NULL),(133,26,'or /r/northkoreanews',NULL),(134,26,'Makes a red light you don\'t',NULL),(135,26,'Pulls over and waits for you',NULL),(136,15,'in line with cart full of groceries',NULL),(137,15,'sees you have one item and lets you go in front of him',NULL),(138,26,'Enjoyed the new bioshock',NULL),(139,26,'shuts the fuck up about it',NULL),(140,26,'Sees the printer is out of ink',NULL),(141,15,'comes home with a new printer. ',NULL),(142,15,'IN FLORIDA TO ESCAPE THE COLD WEATHER',NULL),(143,26,'SEES SAFE BEING BROKEN INTO',NULL),(144,26,'Computer virus scan?',NULL),(145,15,'What do you think I am, a doctor?',NULL),(146,26,'\nI Will kill you',NULL),(147,26,'',NULL),(148,26,'Margaret Thatcher\'s Dead?',NULL),(149,26,'Good',NULL),(150,26,'oh, fuck you',NULL),(151,26,'and your happiness',NULL),(152,15,'Racing son to the end of the grocery aisle',NULL),(153,26,'\"Daddy, don\'t beat me!\"',NULL),(154,15,'Decide to check out womens clothes in new Target store and notice it is the maternity section',NULL),(155,26,'\"Abort, abort!\"',NULL),(156,26,'\"When is the baby due?\"',NULL),(157,26,'Baby delivered 1 month ago',NULL),(158,26,'Percent you get on math test',NULL),(159,26,'Percent of blood you keep',NULL),(160,26,'you blood type b ',NULL),(161,26,'why no blood type a',NULL),(162,26,'Depression?',NULL),(163,26,'why not a-pression?',NULL),(164,26,'Finally',NULL),(165,26,'Not mainstream anymore',NULL),(166,26,'I had a boat ',NULL),(167,26,'Before it was popular!',NULL),(168,26,'I remember when...',NULL),(169,26,'Everyone came over from Digg.',NULL),(170,26,'Confesses to murder on reddit',NULL),(171,15,'doesn\'t bother using a throwaway ',NULL),(172,26,'got shit-faced last night?',NULL),(173,26,'apologize to no one!',NULL),(174,26,'got shit-faced last night?',NULL),(175,15,'call random people and demand apologies!',NULL),(176,26,'These Lockers',NULL),(177,26,'Are too damn high',NULL),(178,15,'The amount of work I left for last minute',NULL),(179,26,'Is too damn high',NULL),(180,15,'The number of dead and dying worms on the sidewalk',NULL),(181,26,'is too damn high',NULL),(182,15,'Malicious Harlots are insignificant',NULL),(183,15,'And they convey nothing of value',NULL),(184,26,'Hang',NULL),(185,26,'thy self',NULL),(186,15,'My brethren and i all permanently reside  ',NULL),(187,15,'upon a sub aquatic vehicle with a yellow hue',NULL),(188,26,'She cancelled our date',NULL),(189,26,'I cancelled her life',NULL),(190,26,'She called me \'four eyes\'',NULL),(191,26,'now i have six',NULL),(192,26,'she broke my heart',NULL),(193,26,'i ripped hers out',NULL),(194,26,'All this murder talk',NULL),(195,26,'just kills me',NULL),(196,15,'I started dating a baseball player...',NULL),(197,26,'What a catch!',NULL),(198,15,'I quit smoking so the girlfriend let me put in her pooper.',NULL),(199,15,'I guess I made the best of a shitty situation.',NULL),(200,26,'Out of coffee',NULL),(201,26,'Looks like my paper is done',NULL),(202,26,'1,000 word essay due',NULL),(203,26,'Turns in a picture',NULL),(204,15,'Final paper can be on anything having to do with the topic \"writing and technology\"',NULL),(205,15,'Writes paper on subject of reddit',NULL),(206,26,'Do you like Kiss?',NULL),(207,26,'Why would I kiss you!',NULL),(208,26,'Have you heard of trap music',NULL),(209,26,'Its a new genre of dubstep',NULL),(210,26,'Bowling for soup?',NULL),(211,15,'Nah, I have plenty of ramen at home.',NULL),(212,26,'This place is going to blow',NULL),(213,15,'up when they have their grand opening next week',NULL),(214,26,'3 seconds to launch',NULL),(215,15,'the fireworks for my daughters graduation',NULL),(216,26,'I blew up the boston marathon',NULL),(217,15,'on my newsfeed to help gain support',NULL),(218,15,'don\'t wear a condom it\'ll be better, but at the last second',NULL),(219,15,'I\'m going to wrap my legs around you, hold you inside of me, and yell \"give me a baby, big daddy!\"',NULL),(220,15,'If you break up with me, I will tell everyone you raped me',NULL),(221,26,'No one will believe you',NULL),(222,26,'i know i\'m not your first love',NULL),(223,26,'so i want to be your last',NULL),(224,26,'Reddit under heavy load',NULL),(225,26,'must be nuclear war',NULL),(226,15,'Sees a girl who looks like my girlfriend on GW',NULL),(227,15,'Goes through all her albums to make sure it\'s not',NULL),(228,26,'Watching porn home alone',NULL),(229,26,'still keeps volume low',NULL),(230,15,'If you give a sleeping meth addict  more  meth,',NULL),(231,26,'Won\'t they just wake up?',NULL),(232,26,'Is choosing someone to lead',NULL),(233,26,'a form of leadership?',NULL),(234,15,'Does everything happen for a reason',NULL),(235,15,'Or do we just apply a reason to everything that happens',NULL),(236,15,'Doesn\'t go to the prom after party',NULL),(237,15,'Has pizza party at grandmas instead',NULL),(238,15,'Doesn\'t go to the prom after party',NULL),(239,15,'Has pizza party at grandmas instead',NULL),(240,26,'Let\'s blast a stick',NULL),(241,15,'of dynamite up for our next lab',NULL),(242,15,'i\'m looking for  rolling papers',NULL),(243,15,'the ones you were assigned to write after last week\'s wheel friction experiment ',NULL),(244,15,'me send me love to the virgin girl',NULL),(245,15,'who has yet to get her cherry mentally popped on the periodic table',NULL),(246,26,'I told him i wanted attention',NULL),(247,15,'he said to confess to murder on reddit',NULL),(248,15,'Why don\'t you want to meet my brother?',NULL),(249,15,'He confessed to be the murderer of the one you loved back then.',NULL),(250,15,'I asked him not to turn everything in to a meme',NULL),(251,15,'He told me \"You\'re gonna have a bad time\"',NULL),(252,26,'gf dumped me',NULL),(253,15,'but she said we\'d still be cousins',NULL),(254,26,'I Got really drunk\nlast night ',NULL),(255,26,'And fucked my Cousin',NULL),(256,15,'The only thing more serious than Nascar',NULL),(257,26,'is shootin\' and nascar',NULL),(258,26,'BUYS A PAINT BRUSH',NULL),(259,26,'IN CANDY CRUSH',NULL),(260,26,'sets precedent',NULL),(261,26,'for bombshells at marathons',NULL),(262,26,'Photo BOMBS marathon',NULL),(263,26,'Nobody gets hurt',NULL),(264,26,'You guys are the kindest',NULL),(265,26,'assholes i know',NULL),(266,15,'pressures you to have unprotected sex ',NULL),(267,15,'lies about being on birth control and gets pregnant',NULL),(268,15,'pressures you to have unprotected sex ',NULL),(269,15,'lies about being on birth control and gets pregnant',NULL),(270,15,'Makes fun of girls with small boobs',NULL),(271,15,'Only has boobs because she\'s fat',NULL),(272,26,'dumps girlfriend',NULL),(273,26,'on valentines day',NULL),(274,15,'Makes drinks free on Rollercoaster tycoon',NULL),(275,26,'Twenty bucks for the bathroom',NULL),(276,15,'Sees you talking to new hot chick',NULL),(277,26,'Adds her on facebook',NULL),(278,26,'want to do missionary?',NULL),(279,15,'I already volunteer at a homeless shelter',NULL),(280,26,'Lost Weight',NULL),(281,15,'Then realized the problem was my personality',NULL);
/*!40000 ALTER TABLE `meme_text` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meme_user`
--

DROP TABLE IF EXISTS `meme_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meme_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meme_user`
--

LOCK TABLES `meme_user` WRITE;
/*!40000 ALTER TABLE `meme_user` DISABLE KEYS */;
INSERT INTO `meme_user` VALUES (1,'','','','test_user',0),(15,'','','','7eb5a1fc-da3e-4c76-9f0f-f5dfc1e12090',1),(16,'',NULL,NULL,'f6d6b768-0872-472c-a3e7-9e8e6d9d3db0',0);
/*!40000 ALTER TABLE `meme_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meme_user_favorite`
--

DROP TABLE IF EXISTS `meme_user_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meme_user_favorite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `meme_background` bigint(20) DEFAULT NULL,
  `meme_user` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK16C136C112E30C47` (`meme_background`),
  KEY `FK16C136C1785EC5C1` (`meme_user`),
  CONSTRAINT `FK16C136C1785EC5C1` FOREIGN KEY (`meme_user`) REFERENCES `meme_user` (`id`),
  CONSTRAINT `FK16C136C112E30C47` FOREIGN KEY (`meme_background`) REFERENCES `meme_background` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meme_user_favorite`
--

LOCK TABLES `meme_user_favorite` WRITE;
/*!40000 ALTER TABLE `meme_user_favorite` DISABLE KEYS */;
/*!40000 ALTER TABLE `meme_user_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meme_user_favorite_memes`
--

DROP TABLE IF EXISTS `meme_user_favorite_memes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meme_user_favorite_memes` (
  `meme_user` bigint(20) NOT NULL,
  `favorite_memes` bigint(20) NOT NULL,
  PRIMARY KEY (`meme_user`,`favorite_memes`),
  KEY `FK5C5F5645941BBF3C` (`favorite_memes`),
  KEY `FK5C5F5645785EC5C1` (`meme_user`),
  CONSTRAINT `FK5C5F5645785EC5C1` FOREIGN KEY (`meme_user`) REFERENCES `meme_user` (`id`),
  CONSTRAINT `FK5C5F5645941BBF3C` FOREIGN KEY (`favorite_memes`) REFERENCES `meme` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meme_user_favorite_memes`
--

LOCK TABLES `meme_user_favorite_memes` WRITE;
/*!40000 ALTER TABLE `meme_user_favorite_memes` DISABLE KEYS */;
/*!40000 ALTER TABLE `meme_user_favorite_memes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sample_meme`
--

DROP TABLE IF EXISTS `sample_meme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sample_meme` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `background` bigint(20) DEFAULT NULL,
  `sample_meme` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK91F52A5DC37E9D8` (`background`),
  KEY `FK91F52A5F23BF901` (`sample_meme`),
  CONSTRAINT `FK91F52A5F23BF901` FOREIGN KEY (`sample_meme`) REFERENCES `meme` (`id`),
  CONSTRAINT `FK91F52A5DC37E9D8` FOREIGN KEY (`background`) REFERENCES `meme_background` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sample_meme`
--

LOCK TABLES `sample_meme` WRITE;
/*!40000 ALTER TABLE `sample_meme` DISABLE KEYS */;
INSERT INTO `sample_meme` VALUES (1,0,1,1),(2,NULL,1,6),(3,NULL,1,7),(4,NULL,1,8),(5,NULL,3,9),(6,NULL,3,10),(7,NULL,4,11),(8,NULL,4,12),(9,NULL,4,13),(10,NULL,6,14),(11,NULL,6,15),(12,NULL,6,16),(13,NULL,7,17),(14,NULL,7,18),(15,NULL,7,19),(16,NULL,10,20),(17,NULL,10,21),(18,NULL,10,22),(19,NULL,11,23),(20,NULL,11,24),(21,NULL,11,25),(22,NULL,13,26),(23,NULL,13,27),(24,NULL,13,28),(25,NULL,14,29),(26,NULL,14,30),(27,NULL,14,31),(28,NULL,15,32),(29,NULL,15,33),(30,NULL,15,34),(31,NULL,17,35),(32,NULL,17,36),(33,NULL,17,37),(34,NULL,18,38),(35,NULL,18,39),(36,NULL,18,40),(37,NULL,19,41),(38,NULL,19,42),(39,NULL,19,43),(40,NULL,22,44),(41,NULL,22,45),(42,NULL,22,46),(43,NULL,23,47),(44,NULL,23,48),(45,NULL,23,49),(46,NULL,26,50),(47,NULL,27,51),(48,NULL,27,52),(49,NULL,27,53),(50,NULL,28,54),(51,NULL,28,55),(52,NULL,28,56),(53,NULL,29,57),(54,NULL,29,58),(55,NULL,29,59),(56,NULL,30,60),(57,NULL,30,61),(58,NULL,30,62),(59,NULL,31,63),(60,NULL,31,64),(61,NULL,31,65),(62,NULL,32,66),(63,NULL,32,67),(64,NULL,32,68),(65,NULL,33,69),(66,NULL,33,70),(67,NULL,33,71),(68,NULL,34,72),(69,NULL,34,73),(70,NULL,34,74),(71,NULL,35,75),(72,NULL,35,76),(73,NULL,35,77),(74,NULL,37,78),(75,NULL,37,79),(76,NULL,37,80),(77,NULL,38,81),(78,NULL,38,82),(79,NULL,38,83),(80,NULL,40,84),(81,NULL,40,85),(82,NULL,40,86),(83,NULL,41,87),(84,NULL,41,88),(85,NULL,41,89),(86,NULL,42,90),(87,NULL,42,91),(88,NULL,42,92),(89,NULL,43,93),(90,NULL,43,94),(91,NULL,43,95),(92,NULL,44,96),(93,NULL,44,97),(94,NULL,44,98),(95,NULL,45,99),(96,NULL,45,100),(97,NULL,45,101),(98,NULL,46,102),(99,NULL,46,103),(100,NULL,46,104),(101,NULL,48,105),(102,NULL,48,106),(103,NULL,48,107),(104,NULL,49,108),(105,NULL,49,109),(106,NULL,49,110),(107,NULL,50,111),(108,NULL,50,112),(109,NULL,50,113),(110,NULL,51,114),(111,NULL,51,115),(112,NULL,51,116),(113,NULL,53,117),(114,NULL,53,118),(115,NULL,54,119),(116,NULL,54,120),(117,NULL,54,121),(118,NULL,56,122),(119,NULL,56,123),(120,NULL,56,124),(121,NULL,57,125),(122,NULL,57,126),(123,NULL,57,127),(124,NULL,58,128),(125,NULL,59,129),(126,NULL,59,130),(127,NULL,59,131),(128,NULL,61,132),(129,NULL,61,133),(130,NULL,61,134),(131,NULL,62,135),(132,NULL,62,136),(133,NULL,62,137),(134,NULL,63,138),(135,NULL,63,139);
/*!40000 ALTER TABLE `sample_meme` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-10-21 20:13:44
