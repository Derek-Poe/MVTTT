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
  PRIMARY KEY (`game_id`),
  KEY `match_id` (`match_id`),
  CONSTRAINT `match_id` FOREIGN KEY (`match_id`) REFERENCES `matches` (`match_id`)
) ENGINE=InnoDB AUTO_INCREMENT=194 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
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
  PRIMARY KEY (`match_id`),
  KEY `player_id_idx` (`player_x_id`,`player_o_id`),
  KEY `player_o_id` (`player_o_id`),
  CONSTRAINT `player_o_id` FOREIGN KEY (`player_o_id`) REFERENCES `players` (`player_id`),
  CONSTRAINT `player_x_id` FOREIGN KEY (`player_x_id`) REFERENCES `players` (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matches`
--

LOCK TABLES `matches` WRITE;
/*!40000 ALTER TABLE `matches` DISABLE KEYS */;
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
  `player_lastLogon` datetime DEFAULT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `players`
--

LOCK TABLES `players` WRITE;
/*!40000 ALTER TABLE `players` DISABLE KEYS */;
INSERT INTO `players` VALUES (1,'JeffreySmithers',NULL,NULL,NULL,NULL,7108,0,0,NULL),(2,'MartinSmithers',NULL,NULL,NULL,NULL,4288,0,0,NULL),(16,'PlayerX','c8597a47-0bfe-4cdc-81c0-6e8580afc29d',_binary '\»\È\«\‘\Ôñ7◊®M\n\Êˆ?t',_binary 'ÇdP]Ωììª™üA\·ÅbL,UBYQ5ÛÆ~n≈¢\‰:\·O\ÂıBÜ¥˚Gëè\–\ÏÚú\œ\”~^MÉ™ëˆ\÷y◊ø',NULL,5336,0,0,'2024-01-26 14:13:59'),(17,'PlayerO','f2445016-12eb-44b5-b644-1060a11bc6a0',_binary '=v\Í\ÊÉ1l™¿∑¸\0\Ál\Â',_binary '8≠má•\\\Œ\rZ\◊IÜ¢\∆\\@+$¯û∂/\Ó™9\«\'e\·\Ã#\∆fqzµ\Î[m˙˚Û8E\⁄\»˚i1ø™∏Fä]\n',NULL,1078,0,0,'2024-01-19 19:04:31'),(19,'NewPlayer',NULL,_binary 'ÌùΩs\ﬁI\»P\Í1eãxä\n',_binary '´äCÉ,\Á™n¨\“ù≥˘\·\—\\Ú\€:ÒR\ÍAæNYû^\'h?º∫Ä˙⁄úΩ\Œ:\rúèoA))∑Ä[Ω≈£IZ','newGuy@unstoppapoenguyen.com',1622,0,0,'2024-01-19 19:01:57'),(25,'Derek',NULL,_binary 'cúå:r4iüäh79\Õ<',_binary '\Á¥G{K:\Â/dπ7\Ã¯w©\‘_ü?^\„W\√ªV\’\–\ÿ_\”nøz™\–\ﬁ≤ò\¬2h.Z˜(\Àj¶πΩÆ6','',1598,0,0,'2024-01-25 17:51:53'),(26,'Nicole',NULL,_binary '3©aΩ®p@@{,ˆç≥&C?',_binary '\ÓÎ≠æˆØMs8+ÑîûOàcéΩMOx\Á\ÏY≠ˇ¢\”\≈ÀëíQi.ò\"\…·≠πÅ∑m˜∏°]•mØd\„â!˝ï','',4179,0,0,'2024-01-25 13:24:14');
/*!40000 ALTER TABLE `players` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'mvttt'
--
/*!50003 DROP PROCEDURE IF EXISTS `cleanupMatches` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `cleanupMatches`()
BEGIN
    SET @now = NOW();
    DELETE FROM games WHERE match_id IN (SELECT match_id FROM matches WHERE match_exp < @now) LIMIT 10000;
    DELETE FROM matches WHERE match_exp < @now LIMIT 10000;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-19 19:09:58
