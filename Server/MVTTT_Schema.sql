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
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
INSERT INTO `games` VALUES (1,1,'----O-X--','------X--',1,0,17);
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
  `match_boardLimit` int DEFAULT NULL,
  `match_scoreGoal` int DEFAULT NULL,
  PRIMARY KEY (`match_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matches`
--

LOCK TABLES `matches` WRITE;
/*!40000 ALTER TABLE `matches` DISABLE KEYS */;
INSERT INTO `matches` VALUES (1,16,17,1,0,1,0,'2023-12-15 00:00:00',1552,1,1,1,9,10);
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
  `player_email` varchar(45) DEFAULT NULL,
  `player_matchesUpdateToken` int DEFAULT NULL,
  `player_wins` int DEFAULT NULL,
  `player_losses` int DEFAULT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `players`
--

LOCK TABLES `players` WRITE;
/*!40000 ALTER TABLE `players` DISABLE KEYS */;
INSERT INTO `players` VALUES (1,'JeffreySmithers',NULL,NULL,NULL,NULL,1467,0,0),(2,'MartinSmithers',NULL,NULL,NULL,NULL,4288,0,0),(16,'PlayerX','04b2bdc0-61ea-4467-bb79-1785a18db233',_binary '\»\È\«\‘\Ôñ7◊®M\n\Êˆ?t',_binary 'ÇdP]Ωììª™üA\·ÅbL,UBYQ5ÛÆ~n≈¢\‰:\·O\ÂıBÜ¥˚Gëè\–\ÏÚú\œ\”~^MÉ™ëˆ\÷y◊ø',NULL,0,0,0),(17,'PlayerO','04b2bdc0-61ea-4467-bb79-1785a18db233',_binary '=v\Í\ÊÉ1l™¿∑¸\0\Ál\Â',_binary '8≠má•\\\Œ\rZ\◊IÜ¢\∆\\@+$¯û∂/\Ó™9\«\'e\·\Ã#\∆fqzµ\Î[m˙˚Û8E\⁄\»˚i1ø™∏Fä]\n',NULL,0,0,0),(19,'NewPlayer','04b2bdc0-61ea-4467-bb79-1785a18db233',_binary 'ÌùΩs\ﬁI\»P\Í1eãxä\n',_binary '´äCÉ,\Á™n¨\“ù≥˘\·\—\\Ú\€:ÒR\ÍAæNYû^\'h?º∫Ä˙⁄úΩ\Œ:\rúèoA))∑Ä[Ω≈£IZ','newGuy@unstoppapoenguyen.com',2056,0,0);
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

-- Dump completed on 2023-12-26 23:23:51
