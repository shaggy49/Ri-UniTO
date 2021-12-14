-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Creato il: Dic 14, 2021 alle 17:37
-- Versione del server: 5.7.34
-- Versione PHP: 7.4.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tweb_reservations`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `reservation_requested`
--

CREATE TABLE IF NOT EXISTS `reservation_requested` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `id_teacher` int(11) NOT NULL,
  `id_course` int(11) NOT NULL,
  `rdate` enum('lun','mar','mer','gio','ven') NOT NULL,
  `rtime` enum('15','16','17','18') NOT NULL,
  `status` enum('booked','deleted','completed') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `req_user_user_id` (`id_user`) USING BTREE,
  KEY `req_teacher_teacher_id` (`id_teacher`) USING BTREE,
  KEY `req_course_course_id` (`id_course`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `reservation_requested`
--

INSERT INTO `reservation_requested` (`id`, `id_user`, `id_teacher`, `id_course`, `rdate`, `rtime`, `status`) VALUES
(1, 1, 1, 1, 'mar', '15', 'booked'),
(2, 1, 1, 1, 'mar', '15', 'deleted'),
(3, 2, 2, 2, 'mar', '15', 'booked'),
(4, 3, 2, 2, 'mar', '17', 'booked'),
(5, 1, 4, 3, 'lun', '18', 'completed'),
(6, 4, 5, 5, 'mer', '15', 'booked'),
(7, 5, 7, 6, 'lun', '18', 'deleted');

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `reservation_requested`
--
ALTER TABLE `reservation_requested`
  ADD CONSTRAINT `reservation_teacherteacher_id` FOREIGN KEY (`id_teacher`) REFERENCES `teacher` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
