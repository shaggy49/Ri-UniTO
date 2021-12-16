-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Creato il: Dic 16, 2021 alle 16:38
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
-- Struttura della tabella `course`
--

CREATE TABLE `course` (
  `id` int(11) NOT NULL,
  `title` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `course`
--

INSERT INTO `course` (`id`, `title`) VALUES
(1, 'TWeb'),
(2, 'Reti'),
(3, 'Architettura'),
(4, 'IUM'),
(5, 'Programazione3'),
(6, 'Database');

-- --------------------------------------------------------

--
-- Struttura della tabella `reservation_available`
--

CREATE TABLE `reservation_available` (
  `id` int(11) NOT NULL,
  `id_teacher` int(11) NOT NULL,
  `id_course` int(11) NOT NULL,
  `date` enum('lun','mar','mer','gio','ven') NOT NULL,
  `time` enum('15','16','17','18') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `reservation_available`
--

INSERT INTO `reservation_available` (`id`, `id_teacher`, `id_course`, `date`, `time`) VALUES
(4, 4, 3, 'lun', '18'),
(5, 4, 3, 'ven', '18'),
(6, 7, 6, 'gio', '18'),
(7, 7, 6, 'lun', '18'),
(8, 5, 5, 'mer', '15');

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
(2, 1, 1, 1, 'mar', '15', 'deleted'),
(3, 2, 2, 2, 'mar', '15', 'booked'),
(4, 3, 2, 2, 'mar', '17', 'booked'),
(5, 1, 4, 3, 'lun', '18', 'completed'),
(6, 4, 5, 5, 'mer', '15', 'booked'),
(7, 5, 7, 6, 'lun', '18', 'deleted');

-- --------------------------------------------------------

--
-- Struttura della tabella `teacher`
--

CREATE TABLE `teacher` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `surname` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `teacher`
--

INSERT INTO `teacher` (`id`, `name`, `surname`) VALUES
(1, 'Liliana', 'Ardissono'),
(2, 'Marco', 'Botta'),
(3, 'Marino', 'Segnan'),
(4, 'Marco', 'Aldinucci'),
(5, 'Rossano', 'Schifanella'),
(6, 'Viviana', 'Patti'),
(7, 'Luca', 'Anselma');

-- --------------------------------------------------------

--
-- Struttura della tabella `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(60) NOT NULL,
  `password` varchar(60) NOT NULL,
  `role` enum('admin','user') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `user`
--

INSERT INTO `user` (`id`, `email`, `password`, `role`) VALUES
(1, 'mc@unito.it', 'f', 'admin'),
(2, 'ff@unito.it', 'f', 'admin'),
(3, 'aa@unito.it', 'f', 'user'),
(4, 'af@unito.it', 'f', 'user'),
(5, 'ldc@unito.it', 'f', 'user');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `reservation_available`
--
ALTER TABLE `reservation_available`
  ADD PRIMARY KEY (`id`),
  ADD KEY `avb_teacher_teacher_id` (`id_teacher`) USING BTREE,
  ADD KEY `avb_course_course_id` (`id_course`) USING BTREE;

--
-- Indici per le tabelle `reservation_requested`
--
ALTER TABLE `reservation_requested`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_user` (`id_user`,`rdate`,`rtime`,`status`),
  ADD UNIQUE KEY `id_teacher` (`id_teacher`,`rdate`,`rtime`),
  ADD KEY `req_user_user_id` (`id_user`) USING BTREE,
  ADD KEY `req_teacher_teacher_id` (`id_teacher`) USING BTREE,
  ADD KEY `req_course_course_id` (`id_course`) USING BTREE;

--
-- Indici per le tabelle `teacher`
--
ALTER TABLE `teacher`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `course`
--
ALTER TABLE `course`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT per la tabella `reservation_available`
--
ALTER TABLE `reservation_available`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT per la tabella `reservation_requested`
--
ALTER TABLE `reservation_requested`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT per la tabella `teacher`
--
ALTER TABLE `teacher`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT per la tabella `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `reservation_available`
--
ALTER TABLE `reservation_available`
  ADD CONSTRAINT `reservation_course_course_id` FOREIGN KEY (`id_course`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reservation_teacher_teacher_id` FOREIGN KEY (`id_teacher`) REFERENCES `teacher` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `reservation_requested`
--
ALTER TABLE `reservation_requested`
  ADD CONSTRAINT `reservation_teacherteacher_id` FOREIGN KEY (`id_teacher`) REFERENCES `teacher` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
