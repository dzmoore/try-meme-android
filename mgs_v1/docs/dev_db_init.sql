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

LOCK TABLES `meme` WRITE;
/*!40000 ALTER TABLE `meme` DISABLE KEYS */;
INSERT INTO `meme` VALUES (1,0,2,1,1,1),(2,0,3,NULL,NULL,4),(3,0,8,15,1,9),(4,0,10,15,1,11),(5,0,12,15,1,13);
/*!40000 ALTER TABLE `meme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `meme_background`
--

LOCK TABLES `meme_background` WRITE;
/*!40000 ALTER TABLE `meme_background` DISABLE KEYS */;
INSERT INTO `meme_background` VALUES (1,'','College Freshman','College_Freshman.jpg',0);
/*!40000 ALTER TABLE `meme_background` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `lv_popularity_type`
--

LOCK TABLES `lv_popularity_type` WRITE;
/*!40000 ALTER TABLE `lv_popularity_type` DISABLE KEYS */;
INSERT INTO `lv_popularity_type` VALUES (1,'popular',0);
/*!40000 ALTER TABLE `lv_popularity_type` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `meme_background_popularity`
--

LOCK TABLES `meme_background_popularity` WRITE;
/*!40000 ALTER TABLE `meme_background_popularity` DISABLE KEYS */;
INSERT INTO `meme_background_popularity` VALUES (1,1,0,1,1);
/*!40000 ALTER TABLE `meme_background_popularity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `meme_text`
--

LOCK TABLES `meme_text` WRITE;
/*!40000 ALTER TABLE `meme_text` DISABLE KEYS */;
INSERT INTO `meme_text` VALUES (1,26,'Top Text',0),(2,26,'Bottom Text',0),(3,26,NULL,0),(4,26,'',0),(8,26,'Bottom Text',0),(9,26,'Top Text',0),(10,26,'Bottom Text',0),(11,26,'Top Text',0),(12,26,'Bottom Text',0),(13,26,'Top Text',0);
/*!40000 ALTER TABLE `meme_text` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `meme_user`
--

LOCK TABLES `meme_user` WRITE;
/*!40000 ALTER TABLE `meme_user` DISABLE KEYS */;
INSERT INTO `meme_user` VALUES (1,'','','','test_user',0),(15,'','','','7eb5a1fc-da3e-4c76-9f0f-f5dfc1e12090',1);
/*!40000 ALTER TABLE `meme_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `meme_user_favorite`
--

LOCK TABLES `meme_user_favorite` WRITE;
/*!40000 ALTER TABLE `meme_user_favorite` DISABLE KEYS */;
/*!40000 ALTER TABLE `meme_user_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `meme_user_favorite_memes`
--

LOCK TABLES `meme_user_favorite_memes` WRITE;
/*!40000 ALTER TABLE `meme_user_favorite_memes` DISABLE KEYS */;
/*!40000 ALTER TABLE `meme_user_favorite_memes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sample_meme`
--

LOCK TABLES `sample_meme` WRITE;
/*!40000 ALTER TABLE `sample_meme` DISABLE KEYS */;
INSERT INTO `sample_meme` VALUES (1,0,1,1);
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

-- Dump completed on 2013-09-28 13:58:28
