-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Dic 19, 2021 alle 19:16
-- Versione del server: 10.4.21-MariaDB
-- Versione PHP: 7.3.31

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

CREATE TABLE `reservation_requested` (
  `id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_teacher` int(11) NOT NULL,
  `id_course` int(11) NOT NULL,
  `rdate` enum('lun','mar','mer','gio','ven') NOT NULL,
  `rtime` enum('15','16','17','18') NOT NULL,
  `status` enum('booked','deleted','completed') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `reservation_requested`
--

INSERT INTO `reservation_requested` (`id`, `id_user`, `id_teacher`, `id_course`, `rdate`, `rtime`, `status`) VALUES
(2, 1, 1, 1, 'mar', '15', 'booked'),
(3, 2, 2, 2, 'mar', '15', 'booked'),
(4, 3, 2, 2, 'mar', '17', 'booked'),
(5, 1, 4, 3, 'lun', '18', 'completed'),
(6, 4, 5, 5, 'mer', '15', 'booked'),
(7, 5, 7, 6, 'lun', '18', 'deleted'),
(10, 2, 4, 3, 'ven', '18', 'deleted'),
(12, 5, 7, 6, 'gio', '18', 'booked');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `reservation_requested`
--
ALTER TABLE `reservation_requested`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `$userunique$` (`id_user`,`rdate`,`rtime`,`status`),
  ADD UNIQUE KEY `$teacherunique$` (`id_teacher`,`rdate`,`rtime`),
  ADD KEY `req_user_user_id` (`id_user`) USING BTREE,
  ADD KEY `req_teacher_teacher_id` (`id_teacher`) USING BTREE,
  ADD KEY `req_course_course_id` (`id_course`) USING BTREE;

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `reservation_requested`
--
ALTER TABLE `reservation_requested`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `reservation_requested`
--
ALTER TABLE `reservation_requested`
  ADD CONSTRAINT `requested_res_course_fk` FOREIGN KEY (`id_course`) REFERENCES `course` (`id`),
  ADD CONSTRAINT `requested_res_user_fk` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `reservation_teacherteacher_id` FOREIGN KEY (`id_teacher`) REFERENCES `teacher` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
