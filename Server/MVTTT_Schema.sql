CREATE DATABASE  IF NOT EXISTS `mvttt` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `mvttt`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: mvttt
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `games` (
  `game_id` int NOT NULL AUTO_INCREMENT,
  `match_id` int NOT NULL,
  `board_current` varchar(9) DEFAULT NULL,
  `board_prev` varchar(9) DEFAULT NULL,
  `game_status` int DEFAULT NULL,
  `game_winner` int DEFAULT NULL,
  `game_lastPlayer` int DEFAULT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
INSERT INTO `games` VALUES (1,1,'OOXXXO-XX','OOXXX--XX',1,14,15),(2,1,'---------','XOX-XX---',1,14,14),(3,1,'---------','X-XX-X-X-',1,14,14),(4,1,'OXX-OOXOX','OXX-OOX-X',1,14,15),(5,1,'---------','---------',1,0,0),(6,1,'---------','--X-XO-XX',1,0,0),(7,1,'XXX-O-X-O','X-X-O-X-O',2,15,15),(8,1,'--O------','---------',1,0,15),(9,1,'--------X','---------',1,0,0);
/*!40000 ALTER TABLE `games` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matches`
--

DROP TABLE IF EXISTS `matches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matches` (
  `match_id` int NOT NULL AUTO_INCREMENT,
  `player_x_id` int NOT NULL,
  `player_o_id` int NOT NULL,
  `player_x_score` int NOT NULL,
  `player_o_score` int NOT NULL,
  `match_status` int DEFAULT NULL,
  `match_winner` int DEFAULT NULL,
  `match_exp` datetime DEFAULT NULL,
  `match_updateToken` int DEFAULT NULL,
  `match_turn` int DEFAULT NULL,
  `match_lastMoveGame` int DEFAULT NULL,
  `match_type` int DEFAULT NULL,
  PRIMARY KEY (`match_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matches`
--

LOCK TABLES `matches` WRITE;
/*!40000 ALTER TABLE `matches` DISABLE KEYS */;
INSERT INTO `matches` VALUES (1,14,15,3,0,1,0,'2023-12-15 00:00:00',4078,1,8,0),(2,2,14,0,0,2,1,'2023-12-15 00:00:00',1324,1,2,0);
/*!40000 ALTER TABLE `matches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `players`
--

DROP TABLE IF EXISTS `players`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `players` (
  `player_id` int NOT NULL AUTO_INCREMENT,
  `player_name` varchar(20) NOT NULL,
  `player_session` varchar(36) DEFAULT NULL,
  `player_salt` blob,
  `player_hash` blob,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `players`
--

LOCK TABLES `players` WRITE;
/*!40000 ALTER TABLE `players` DISABLE KEYS */;
INSERT INTO `players` VALUES (1,'JeffreySmithers',NULL,NULL,NULL),(2,'MartinSmithers',NULL,NULL,NULL),(14,'1234','c439f576-334e-4b86-a7ad-1364d59f1484',_binary '\ÈgdRá7ñ:?ù\¬',_binary 'vûQ\Ã\rpl\·\‰¸©‘∞⁄óeı\–”àU|ÉXèPpó.dlVú˘€í∫>ç\·≈û≥ÜSºF\Îs©\Ô\€'),(15,'2345','c439f576-334e-4b86-a7ad-1364d59f1484',_binary '\'¸Út\\∂VcétS•^',_binary '¡cë\⁄&ùÅ\'£HÇÒyˇˇ\'¿~W^ô\Ì°\‡åÖ\‘€ä’ë~ç:´»Ö\Î\÷!\¬9˚w\÷[i\Ó2xê*1pwW˚\\∂∏');
/*!40000 ALTER TABLE `players` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-23 23:43:28
