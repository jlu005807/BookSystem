-- MySQL dump 10.13  Distrib 8.0.33, for macos13.3 (arm64)
--
-- Host: 127.0.0.1    Database: rental
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `isbn` varchar(32) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `numLeft` int NOT NULL,
  `numAll` int NOT NULL,
  `addTime` date NOT NULL DEFAULT CURRENT_DATE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
INSERT INTO `book` (`isbn`, `title`, `author`, `numLeft`, `numAll`) VALUES
('9787020002207', '红楼梦', '曹雪芹', 5, 5),
('9787020008452', '西游记', '吴承恩', 4, 4),
('9787020008728', '三国演义', '罗贯中', 6, 6),
('9787020008735', '水浒传', '施耐庵', 3, 3),
('9780140449136', 'War and Peace', 'Leo Tolstoy', 2, 2),
('9780140449266', 'Crime and Punishment', 'Fyodor Dostoevsky', 3, 3),
('9780141439600', 'Pride and Prejudice', 'Jane Austen', 4, 4),
('9780140449181', 'The Odyssey', 'Homer', 2, 2),
('9780140449273', 'The Divine Comedy', 'Dante Alighieri', 2, 2),
('9780140449198', 'Les Misérables', 'Victor Hugo', 3, 3);
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `password` text NOT NULL,
  `registerTime` date NOT NULL DEFAULT CURRENT_DATE,
  `comment` text,
  KEY `account_id_index` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
INSERT INTO `user` VALUES (1,'root','root','2023-06-04','I''m the root!'),(2,'test','114514','2023-06-04','no');
UNLOCK TABLES;

--
-- Table structure for table `borrow_record`
--

DROP TABLE IF EXISTS `borrow_record`;
CREATE TABLE `borrow_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_id` int NOT NULL,
  `user_id` int NOT NULL,
  `borrow_time` date NOT NULL DEFAULT CURRENT_DATE,
  `return_time` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`book_id`) REFERENCES `book`(`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始化借阅记录
INSERT INTO `borrow_record` (`book_id`, `user_id`, `borrow_time`, `return_time`) VALUES
(1, 1, '2024-06-01', '2024-06-10'),
(2, 2, '2024-06-02', NULL),
(1, 2, '2024-06-05', NULL);

-- 创建视图，方便查询每本书的所有借阅记录及相关用户信息
CREATE OR REPLACE VIEW v_book_borrow AS
SELECT
  b.id AS book_id,
  b.title,
  b.author,
  br.id AS borrow_id,
  u.username,
  br.borrow_time,
  br.return_time
FROM
  book b
  LEFT JOIN borrow_record br ON b.id = br.book_id
  LEFT JOIN user u ON br.user_id = u.id;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-07 15:51:38
