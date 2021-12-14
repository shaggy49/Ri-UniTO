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
-- Struttura della tabella `reservation_available`
--

CREATE TABLE IF NOT EXISTS `reservation_available` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_teacher` int(11) NOT NULL,
  `id_course` int(11) NOT NULL,
  `date` enum('lun','mar','mer','gio','ven') NOT NULL,
  `time` enum('15','16','17','18') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `avb_teacher_teacher_id` (`id_teacher`) USING BTREE,
  KEY `avb_course_course_id` (`id_course`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `reservation_available`
--

INSERT INTO `reservation_available` (`id`, `id_teacher`, `id_course`, `date`, `time`) VALUES
(1, 1, 1, 'mar', '15'),
(2, 2, 2, 'mar', '15'),
(3, 2, 2, 'mar', '17'),
(4, 4, 3, 'lun', '18'),
(5, 4, 3, 'ven', '18'),
(6, 7, 6, 'gio', '18'),
(7, 7, 6, 'lun', '18'),
(8, 5, 5, 'mer', '15');

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `reservation_available`
--
ALTER TABLE `reservation_available`
  ADD CONSTRAINT `reservation_course_course_id` FOREIGN KEY (`id_course`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reservation_teacher_teacher_id` FOREIGN KEY (`id_teacher`) REFERENCES `teacher` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
