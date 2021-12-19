package com.reservation.application.dao;

import com.reservation.application.entities.ReservationAvailable;
import com.reservation.application.entities.ReservationRequested;

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
//TO-TEST-setReservationState(int id_reservationRequested, String stateToUpdate) → selezionare una ripetizione dalla tabella di requested, marcarla come disdetta (modificare lo stato in deleted), state è un enum che può valere: *disdetta/completata*
//getRequestedReservations(int id_user) → prende tutte le ripetizioni della tabella requested dello user passato come parametro, con tutte si intende con qualunque tipo di stato
//getRequestedReservations( ) → prende tutte le ripetizioni dalla tabella requested
//insertCourse(String title)
//removeCourse(int id_course)
//insertTeacher(String name, String surname)
//removeTeacher(int id_teacher)
//insertStudent(String name, String surname)
//removeStudent(int id_course)
//getUserRole(String email, String password) lo mette in sessione utente

    //TODO fare in modo di stampare il nome del teahcer e del corso al posto dell'id
    public static List<ReservationAvailable> getAvailableReservations() {
        Connection connection = null;
        ArrayList<ReservationAvailable> out = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url1, user, password);
            if (connection != null) {
                System.out.println("Connected to the database");
            }

            String query = "SELECT * FROM `reservation_available`";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {

                ReservationAvailable reservationAvailable = new ReservationAvailable(Integer.parseInt(
                        rs.getString("id_teacher")),
                        Integer.parseInt(rs.getString("id_course")),
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

    public static List<ReservationRequested> getRequestedReservations() {
        Connection connection = null;
        ArrayList<ReservationRequested> out = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url1, user, password);
            if (connection != null) {
                System.out.println("Connected to the database");
            }

            String query = "SELECT * FROM `reservation_requested`";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {

                ReservationRequested reservationRequested = new ReservationRequested(Integer.parseInt(
                        rs.getString("id_user")),
                        Integer.parseInt(rs.getString("id_teacher")),
                        Integer.parseInt(rs.getString("id_course")),
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

    /**
     * Preleva, grazie all'id passato come primo parametro, gli attributi da caricare nella relazione requested,
     * cancella la tupla X dalla relazione available e aggiunge una tupla Y alla relazione requested con
     * l'aggiunta dell'id_user, modificando lo state in prenotata
     */
    public static void bookRequestedReservation(int id_reservationAvailable, int id_user) throws SQLException{
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

        if(count == 0){
            connection.close();
            throw new SQLException("Invalid row selection");
        }

        if (stDML.executeUpdate(insertToResRequested) != 0)
            System.out.println("La tupla è stata inserita nella tabella reservation requested!");
        else{
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

        String queryFromResRequested = String.format("SELECT `id_teacher`, `id_course`, `date`, `time` FROM `reservation_requested` WHERE id = %d;", idReservationRequested);
        String queryUpdateResRequested = String.format("UPDATE reservation_requested SET status = '%s' WHERE id = %d;", stateToUpdate, idReservationRequested);

        Statement st = connection.createStatement();

        if (st.executeUpdate(queryUpdateResRequested) != 0)
            System.out.println("Lo stato della prenotazione è stato correttamente modificato!");
        else{
            connection.close();
            throw new SQLException();
        }

        if (connection != null) {
            connection.close();
        }

    }

    /*public static int getUserId(String account, String pw) {
        Connection conn1 = null;
        int id = 0;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }

            String query = String.format("SELECT `id` FROM `utente` WHERE `account` = '%s' and `pword` = '%s'", account, pw);

            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return id;
    }

    public static String getUserRole(String account, String pw) {
        Connection conn1 = null;
        String role = ""; //qui sta il problema (si visulizza dalla Servlet sempre questo valore)
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }

            String query = String.format("SELECT `ruolo` FROM `utente` WHERE `account` = '%s' and `pword` = '%s'", account, pw);

            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                role = rs.getString("ruolo");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return role;
    }

    public static void insertCourses(Corso course) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("INSERT INTO corsi(titolo) VALUES ('%s')", course.getTitolo());
            Statement st = conn.createStatement();
            if (st.executeUpdate(query) != 0)
                System.out.println(course.getTitolo() + " è stato aggiunto al database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void removeCourses(Corso course) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("DELETE FROM corsi WHERE titolo = '%s'", course.getTitolo());
            Statement st = conn.createStatement();
            if (st.executeUpdate(query) != 0)
                System.out.println(course.getTitolo() + " è stato eliminato dal database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void insertTeacher(Docente docente) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("INSERT INTO `docenti`(`nome`, `cognome`) VALUES ('%s','%s')", docente.getNome(), docente.getCognome());
            Statement st = conn.createStatement();
            if (st.executeUpdate(query) != 0)
                System.out.println("L'elemento è stato aggiunto al database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void removeTeacher(Docente docente) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("DELETE FROM docenti WHERE nome = '%s' and cognome = '%s'", docente.getNome(), docente.getCognome());
            Statement st = conn.createStatement();
            if (st.executeUpdate(query) != 0)
                System.out.println("L'elemento è stato rimosso dal database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void insertInsegnamento(Insegnamento insegnamento) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("INSERT INTO `insegnamento`(`docente`, `corso`) VALUES ('%s','%s')", insegnamento.getDocente(), insegnamento.getCorso());
            Statement st = conn.createStatement();
            if (st.executeUpdate(query) != 0)
                System.out.println("L'elemento è stato aggiunto al database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void removeInsegnamento(Insegnamento insegnamento) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("DELETE FROM insegnamento WHERE docente = '%d' and corso = '%s'", insegnamento.getDocente(), insegnamento.getCorso());
            Statement st = conn.createStatement();
            if (st.executeUpdate(query) != 0)
                System.out.println("L'elemento è stato rimosso dal database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static ArrayList<Docente> showDocentiForCourse(Corso course) {
        Connection conn1 = null;
        ArrayList<Docente> out = new ArrayList<>();
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }

            String query = String.format("SELECT d.nome, d.cognome FROM insegnamento i join docenti d on i.docente = d.id WHERE i.corso = '%s'", course.getTitolo());

            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Docente p = new Docente(rs.getString("d.nome"), rs.getString("d.cognome"));
                out.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return out;
    }

    public static void insertPrenotation(Prenotazione prenotazione) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("INSERT INTO `prenotazioni`(`corso`, `docente`, `utente`) VALUES ('%s','%d','%d')", prenotazione.getCorso(), prenotazione.getDocente(), prenotazione.getUtente());
            Statement st = conn.createStatement();
            if (st.executeUpdate(query) != 0)
                System.out.println("L'elemento è stato aggiunto al database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void removePrenotation(Prenotazione prenotazione) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("DELETE FROM `prenotazioni` WHERE corso = '%s' and docente ='%d' and utente = '%d'", prenotazione.getCorso(), prenotazione.getDocente(), prenotazione.getUtente());
            Statement st = conn.createStatement();
            if (st.executeUpdate(query) != 0)
                System.out.println("L'elemento è stato rimosso dal database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }*/
}
