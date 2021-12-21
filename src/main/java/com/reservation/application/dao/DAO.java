package com.reservation.application.dao;

import com.reservation.application.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private static String url1;
    private static String user;
    private static String password;

    public static void registerDriver(String url, String usr, String pword) {
        try {
            url1 = url;
            user = usr;
            password = pword;
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

// METHODS TO DEVELOP:
//DONE-getAvailableReservations() → prende tutte le ripetizioni disponibili (serve al guest)
//DONE-bookRequestedReservation(int id_reservationAvailable, int id_user) → preleva grazie all'id passato come primo parametro gli attributi da caricare nella relazione requested, cancella la tupla X dalla relazione available e aggiunge una tupla Y alla relazione requested con l'aggiunta dell'id_user, modificando lo state in *prenotata*
//DONE-setReservationState(int id_reservationRequested, String stateToUpdate) → selezionare una ripetizione dalla tabella di requested, marcarla come disdetta (modificare lo stato in deleted), state è un enum che può valere: *disdetta/completata*
//DONE-getRequestedReservations(int id_user) → prende tutte le ripetizioni della tabella requested dello user passato come parametro, con tutte si intende con qualunque tipo di stato
//DONE-getRequestedReservations() → prende tutte le ripetizioni dalla tabella requested
//DONE-insertCourse(String title)
//DONE-removeCourse(int id_course)
//DONE-insertTeacher(String name, String surname)
//DONE-removeTeacher(int id_teacher)
//DONE-insertUser(String name, String surname)
//DONE-removeUser(int id_course)
//DONE-insertAvailableReservation(int id_teacher, int id_course, String date, String time)
//DONE-removeAvailableReservation(int id_reservationAvailable)
//DONE-getUserRole(String email, String password) lo mette in sessione utente

    public static List<ReservationAvailable> getAvailableReservations() {
        Connection connection = null;
        ArrayList<ReservationAvailable> out = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url1, user, password);
            if (connection != null) {
                System.out.println("Connected to the database");
            }

            String query = "" +
                    "SELECT reservation_available.id as res_id, t.id as teacher_id, name, surname, c.id as course_id ,title, date, time " +
                    "FROM reservation_available join course c on reservation_available.id_course = c.id join teacher t on t.id = reservation_available.id_teacher";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {

                ReservationAvailable reservationAvailable = new ReservationAvailable(
                        Integer.parseInt(rs.getString("res_id")),
                        new Teacher(Integer.parseInt(rs.getString("teacher_id")), rs.getString("name"), rs.getString("surname")),
                        new Course(Integer.parseInt(rs.getString("course_id")), rs.getString("title")),
                        rs.getString("date"),
                        rs.getString("time")
                );

                out.add(reservationAvailable);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return out;
    }

    /**
     * Preleva, grazie all'id passato come primo parametro, gli attributi da caricare nella relazione requested,
     * cancella la tupla X dalla relazione available e aggiunge una tupla Y alla relazione requested con
     * l'aggiunta dell'id_user, modificando lo state in prenotata
     */
    public static void bookRequestedReservation(int id_reservationAvailable, int id_user) throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(url1, user, password);
        int count = 0;
        if (connection != null) {
            System.out.println("Connected to the database");
        }

        String queryFromResAvailable = String.format("SELECT `id_teacher`, `id_course`, `date`, `time` FROM `reservation_available` WHERE id = %d;", id_reservationAvailable);
        String queryDeleteResAvailable = String.format("DELETE FROM `reservation_available` WHERE id = %d;", id_reservationAvailable);
        String insertToResRequested = "";
        Statement st = connection.createStatement();
        Statement stDML = connection.createStatement();
        ResultSet rsReservationAvailable = st.executeQuery(queryFromResAvailable);

        while (rsReservationAvailable.next()) {
            count++;
            insertToResRequested = String.format("INSERT INTO `reservation_requested`(`id_user`, `id_teacher`, `id_course`, `rdate`, `rtime`, `status`) VALUES (%d,%d,%d,'%s','%s','%s')",
                    id_user,
                    Integer.parseInt(rsReservationAvailable.getString("id_teacher")),
                    Integer.parseInt(rsReservationAvailable.getString("id_course")),
                    rsReservationAvailable.getString("date"),
                    rsReservationAvailable.getString("time"),
                    "booked");
        }

        if (count == 0) {
            connection.close();
            throw new SQLException("noresfound");
        }

        if (stDML.executeUpdate(insertToResRequested) != 0)
            System.out.println("La tupla è stata inserita nella tabella reservation requested!");
        else {
            connection.close();
            throw new SQLException();
        }
        if (stDML.executeUpdate(queryDeleteResAvailable) != 0)
            System.out.println("La tupla è stata eliminata dalla tabella reservation available!");

        if (connection != null) {
            connection.close();
        }
    }

    public static void setReservationState(int idReservationRequested, String stateToUpdate) throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(url1, user, password);
        if (connection != null) {
            System.out.println("Connected to the database");
        }

        String queryUpdateResRequested = String.format("UPDATE reservation_requested SET status = '%s' WHERE id = %d;", stateToUpdate, idReservationRequested);

        Statement st = connection.createStatement();

        if (st.executeUpdate(queryUpdateResRequested) != 0)
            System.out.println("Lo stato della prenotazione è stato correttamente modificato!");
        else {
            connection.close();
            throw new SQLException();
        }

        if (connection != null) {
            connection.close();
        }

    }

    public static List<ReservationRequested> getRequestedReservations(int idUser) {
        Connection connection = null;
        ArrayList<ReservationRequested> out = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url1, user, password);
            if (connection != null) {
                System.out.println("Connected to the database");
            }

            String query = String.format(
                    "SELECT reservation_requested.id as res_id, u.id as user_id, email, t.id as teacher_id, name, surname, c.id as course_id ,title, rdate, rtime, status " +
                    "FROM reservation_requested join course c on c.id = reservation_requested.id_course join teacher t on reservation_requested.id_teacher = t.id join user u on reservation_requested.id_user = u.id " +
                    "WHERE id_user = '%d'", idUser);

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {

                ReservationRequested reservationRequested = new ReservationRequested(
                        Integer.parseInt(rs.getString("res_id")),
                        new User(Integer.parseInt(rs.getString("user_id")), rs.getString("email")),
                        new Teacher(Integer.parseInt(rs.getString("teacher_id")), rs.getString("name"), rs.getString("surname")),
                        new Course(Integer.parseInt(rs.getString("course_id")), rs.getString("title")),
                        rs.getString("rdate"),
                        rs.getString("rtime"),
                        rs.getString("status")
                );

                out.add(reservationRequested);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return out;
    }

    public static List<ReservationRequested> getRequestedReservations() {
        Connection connection = null;
        ArrayList<ReservationRequested> out = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url1, user, password);
            if (connection != null) {
                System.out.println("Connected to the database");
            }

            String query = "" +
                    "SELECT reservation_requested.id as res_id, u.id as user_id, email, t.id as teacher_id, name, surname, c.id as course_id ,title, rdate, rtime, status " +
                    "FROM reservation_requested join course c on c.id = reservation_requested.id_course join teacher t on reservation_requested.id_teacher = t.id join user u on reservation_requested.id_user = u.id;";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {

                ReservationRequested reservationRequested = new ReservationRequested(
                        Integer.parseInt(rs.getString("res_id")),
                        new User(Integer.parseInt(rs.getString("user_id")), rs.getString("email")),
                        new Teacher(Integer.parseInt(rs.getString("teacher_id")), rs.getString("name"), rs.getString("surname")),
                        new Course(Integer.parseInt(rs.getString("course_id")), rs.getString("title")),
                        rs.getString("rdate"),
                        rs.getString("rtime"),
                        rs.getString("status")
                );

                out.add(reservationRequested);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return out;
    }

    public static List<Course> getCourses() {
        Connection connection = null;
        ArrayList<Course> out = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url1, user, password);
            if (connection != null) {
                System.out.println("Connected to the database");
            }

            String query = "SELECT * FROM course;";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Course course = new Course(
                        Integer.parseInt(rs.getString("id")),
                        rs.getString("title")
                );
                out.add(course);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return out;
    }

    public static void insertCourses(String title) throws SQLException{
        Connection connection = null;
        connection = DriverManager.getConnection(url1, user, password);
        if (connection != null) {
            System.out.println("Connected to the database");
        }
        String query = String.format("INSERT INTO course(title) VALUES ('%s')", title);
        Statement st = connection.createStatement();
        if (st.executeUpdate(query) != 0)
            System.out.println(title + " è stato aggiunto al database!");
        if (connection != null) {
                connection.close();
        }
    }

    public static void removeCourses(int courseId) throws SQLException{
        Connection connection = null;
        connection = DriverManager.getConnection(url1, user, password);
        if (connection != null) {
            System.out.println("Connected to the database");
        }
        String query = String.format("DELETE FROM course WHERE id = %d", courseId);
        Statement st = connection.createStatement();
        if (st.executeUpdate(query) != 0)
            System.out.println("Il corso con id = " + courseId + " è stato eliminato dal database!");
        if (connection != null) {
                connection.close();
        }
    }

    public static List<Teacher> getTeachers() {
        Connection connection = null;
        ArrayList<Teacher> out = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url1, user, password);
            if (connection != null) {
                System.out.println("Connected to the database");
            }

            String query = "SELECT * FROM teacher;";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Teacher teacher = new Teacher(
                        Integer.parseInt(rs.getString("id")),
                        rs.getString("name"),
                        rs.getString("surname")
                );
                out.add(teacher);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return out;
    }

    public static void insertTeacher(String name, String surname) throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(url1, user, password);
        if (connection != null) {
            System.out.println("Connected to the database");
        }
        String query = String.format("INSERT INTO `teacher`(`name`, `surname`) VALUES ('%s','%s')", name, surname);
        Statement st = connection.createStatement();
        if (st.executeUpdate(query) != 0)
            System.out.println("Il professore è stato aggiunto al database!");
        if (connection != null) {
                connection.close();
        }
    }

    public static void removeTeacher(int teacherId) throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(url1, user, password);
        if (connection != null) {
            System.out.println("Connected to the database");
            String query = String.format("DELETE FROM teacher WHERE id = %d", teacherId);
            Statement st = connection.createStatement();
            if (st.executeUpdate(query) != 0)
                System.out.println("Il professore con id = " + teacherId + " è stato rimosso dal database!");
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void insertAvailableReservation(int id_teacher, int id_course, String date, String time) throws SQLException{
        Connection connection = null;
        connection = DriverManager.getConnection(url1, user, password);
        if (connection != null) {
            System.out.println("Connected to the database");
        }
        String query = String.format("INSERT INTO reservation_available (id_teacher, id_course, date, time) VALUES (%d,%d,'%s','%s')", id_teacher, id_course, date, time);
        Statement st = connection.createStatement();
        if (st.executeUpdate(query) != 0)
            System.out.println("La prenotazione è stata aggiunta al database!");
        if (connection != null)
            connection.close();
    }

    public static void removeAvailableReservation(int idAvailableReservation) throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(url1, user, password);
        if (connection != null) {
            System.out.println("Connected to the database");
        }
        String query = String.format("DELETE FROM reservation_available WHERE id = %d", idAvailableReservation);
        Statement st = connection.createStatement();
        if (st.executeUpdate(query) != 0)
            System.out.println("La prenotazione con id = " + idAvailableReservation + " è stato rimossa dal database!");
        if (connection != null)
            connection.close();
    }

    public static String getUserRole(int userId) {
        Connection connection = null;
        String role = "";
        try {
            connection = DriverManager.getConnection(url1, user, password);
            if (connection != null) {
                System.out.println("Connected to the database");
            }

            String query = String.format("SELECT `role` FROM `user` WHERE id = %d", userId);

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                role = rs.getString("role");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return role;
    }
}
