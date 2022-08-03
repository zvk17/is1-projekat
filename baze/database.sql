CREATE DATABASE  IF NOT EXISTS `pametna_kuca` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `pametna_kuca`;
-- MySQL dump 10.13  Distrib 8.0.25, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: pametna_kuca
-- ------------------------------------------------------
-- Server version	8.0.25-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activities`
--

DROP TABLE IF EXISTS `activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activities` (
  `idActivity` int NOT NULL AUTO_INCREMENT,
  `idUser` int NOT NULL,
  `startDateTime` datetime NOT NULL,
  `durationSeconds` int NOT NULL,
  `destinationName` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`idActivity`),
  KEY `FK_idUser_activities_idx` (`idUser`),
  CONSTRAINT `FK_idUser_activities` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activities`
--

LOCK TABLES `activities` WRITE;
/*!40000 ALTER TABLE `activities` DISABLE KEYS */;
INSERT INTO `activities` VALUES (20,1,'2021-08-18 20:30:00',4600,'Niš'),(23,1,'2021-08-21 21:00:00',3600,'Sombor'),(24,1,'2021-08-24 10:00:00',3600,NULL),(25,2,'2022-06-06 10:00:00',3600,'Sombor'),(26,2,'2022-01-10 01:00:00',3600,'Subotica');
/*!40000 ALTER TABLE `activities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_alarm`
--

DROP TABLE IF EXISTS `activity_alarm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_alarm` (
  `idActivity` int NOT NULL,
  `idAlarm` int NOT NULL,
  PRIMARY KEY (`idActivity`,`idAlarm`),
  KEY `fk_idAlarm_activity_alarm_idx` (`idAlarm`),
  CONSTRAINT `fk_idActivity_activity_alarm` FOREIGN KEY (`idActivity`) REFERENCES `activities` (`idActivity`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_idAlarm_activity_alarm` FOREIGN KEY (`idAlarm`) REFERENCES `alarms` (`idAlarm`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_alarm`
--

LOCK TABLES `activity_alarm` WRITE;
/*!40000 ALTER TABLE `activity_alarm` DISABLE KEYS */;
INSERT INTO `activity_alarm` VALUES (25,56),(26,57);
/*!40000 ALTER TABLE `activity_alarm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alarms`
--

DROP TABLE IF EXISTS `alarms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alarms` (
  `idAlarm` int NOT NULL AUTO_INCREMENT,
  `idUser` int NOT NULL,
  `datetimeMoment` datetime NOT NULL,
  `intervalSeconds` int DEFAULT NULL,
  PRIMARY KEY (`idAlarm`),
  KEY `fk_idUser_Alarm_idx` (`idUser`),
  CONSTRAINT `fk_idUser_Alarm` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alarms`
--

LOCK TABLES `alarms` WRITE;
/*!40000 ALTER TABLE `alarms` DISABLE KEYS */;
INSERT INTO `alarms` VALUES (42,1,'2021-09-25 08:00:00',NULL),(43,1,'2021-09-21 00:00:00',NULL),(56,2,'2022-06-06 04:36:11',NULL),(57,2,'2022-01-09 19:54:00',NULL);
/*!40000 ALTER TABLE `alarms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `old_alarms`
--

DROP TABLE IF EXISTS `old_alarms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `old_alarms` (
  `idOldAlarm` int NOT NULL,
  `idUser` int NOT NULL,
  `datetimeMoment` datetime NOT NULL,
  PRIMARY KEY (`idOldAlarm`),
  KEY `fk_idUser_OldAlarm_idx` (`idUser`),
  CONSTRAINT `fk_idUser_OldAlarm` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `old_alarms`
--

LOCK TABLES `old_alarms` WRITE;
/*!40000 ALTER TABLE `old_alarms` DISABLE KEYS */;
/*!40000 ALTER TABLE `old_alarms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `songs`
--

DROP TABLE IF EXISTS `songs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `songs` (
  `idSong` int NOT NULL AUTO_INCREMENT,
  `url` varchar(200) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`idSong`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `songs`
--

LOCK TABLES `songs` WRITE;
/*!40000 ALTER TABLE `songs` DISABLE KEYS */;
INSERT INTO `songs` VALUES (1,'https://www.youtube.com/watch?v=lzFglGhPu0c','Ana Mena - Rocco Hunt - A un paso de la luna'),(2,'https://www.youtube.com/watch?v=ReX9fLKcNtM','Baby K - Roma - Bangkok'),(3,'https://www.youtube.com/watch?v=JXrmgzE8Tgg','Baby K - Da Zero A Cento'),(4,'https://www.youtube.com/watch?v=1-L3BUg4Bsk','Rocco Hunt, Ana Mena - Un bacio all\'improvviso');
/*!40000 ALTER TABLE `songs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_song`
--

DROP TABLE IF EXISTS `user_song`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_song` (
  `idUser` int NOT NULL,
  `idSong` int NOT NULL,
  PRIMARY KEY (`idUser`,`idSong`),
  KEY `fk_idSong_user_song_idx` (`idSong`),
  CONSTRAINT `fk_idSong_user_song` FOREIGN KEY (`idSong`) REFERENCES `songs` (`idSong`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_idUser_user_song` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_song`
--

LOCK TABLES `user_song` WRITE;
/*!40000 ALTER TABLE `user_song` DISABLE KEYS */;
INSERT INTO `user_song` VALUES (1,1),(2,1),(1,2),(1,3);
/*!40000 ALTER TABLE `user_song` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `idUser` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `homeLocation` varchar(70) NOT NULL,
  `idSong` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`idUser`),
  KEY `fk_idSong_users_idx` (`idSong`),
  CONSTRAINT `fk_idSong_users` FOREIGN KEY (`idSong`) REFERENCES `songs` (`idSong`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'korisnik','123','Beograd',4),(2,'user','111','Leskovac',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'pametna_kuca'
--

--
-- Dumping routines for database 'pametna_kuca'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-08-31  2:18:13
