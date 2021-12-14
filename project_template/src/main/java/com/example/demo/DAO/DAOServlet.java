package com.example.demo.DAO;

import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


/*
 * Per ora il DAO ha come metodo di Servlet soltanto le get per restituire tutti
 * i docenti
 * */
@WebServlet(name = "DAOServlet", value = "/dao-servlet")
public class DAOServlet extends HttpServlet {
    private static String url1;
    private static String user;
    private static String password;

    public void init(ServletConfig config) {
        try {
            super.init(config);
            ServletContext ctx = config.getServletContext();
            String url = ctx.getInitParameter("url");
            String user = ctx.getInitParameter("user");
            String password = ctx.getInitParameter("password");
            registerDriver(url, user, password);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        Corso corso = (Corso) req.getAttribute("corso");
        ArrayList<Docente> docenti = showDocentiForCourse(corso);
        Gson gson = new Gson(); // traduttore da e verso formato JSON
        String listaCorsi = gson.toJson(docenti);
        System.out.println("La stringa JSON passata è la seguente: "+listaCorsi);
        req.setAttribute("listaCorsiDocente", listaCorsi);
    }

    public static void registerDriver(String url, String usr, String pword) {
        try {
            url1 = url;
            user = usr;
            password = pword;
            DriverManager.registerDriver(new com.mysql.jdbc.Driver()); //era rossa perché non erano stati caricati i driver
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    public static int getUserId(String account, String pw){
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
        }
        finally {
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
        }
        finally {
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

    public static void insertCourses(Corso course){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("INSERT INTO corsi(titolo) VALUES ('%s')", course.getTitolo());
            Statement st = conn.createStatement();
            if(st.executeUpdate(query) != 0)
                System.out.println(course.getTitolo()+" è stato aggiunto al database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
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
            if(st.executeUpdate(query) != 0)
                System.out.println(course.getTitolo()+" è stato eliminato dal database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void insertTeacher(Docente docente){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("INSERT INTO `docenti`(`nome`, `cognome`) VALUES ('%s','%s')", docente.getNome(), docente.getCognome());
            Statement st = conn.createStatement();
            if(st.executeUpdate(query) != 0)
                System.out.println("L'elemento è stato aggiunto al database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void removeTeacher(Docente docente){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("DELETE FROM docenti WHERE nome = '%s' and cognome = '%s'", docente.getNome(), docente.getCognome());
            Statement st = conn.createStatement();
            if(st.executeUpdate(query) != 0)
                System.out.println("L'elemento è stato rimosso dal database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void insertInsegnamento(Insegnamento insegnamento){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("INSERT INTO `insegnamento`(`docente`, `corso`) VALUES ('%s','%s')", insegnamento.getDocente(), insegnamento.getCorso());
            Statement st = conn.createStatement();
            if(st.executeUpdate(query) != 0)
                System.out.println("L'elemento è stato aggiunto al database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void removeInsegnamento(Insegnamento insegnamento){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("DELETE FROM insegnamento WHERE docente = '%d' and corso = '%s'", insegnamento.getDocente(), insegnamento.getCorso());
            Statement st = conn.createStatement();
            if(st.executeUpdate(query) != 0)
                System.out.println("L'elemento è stato rimosso dal database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static ArrayList<Docente> showDocentiForCourse(Corso course){
        Connection conn1 = null;
        ArrayList<Docente> out = new ArrayList<>();
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }

            String query = String.format("SELECT d.nome, d.cognome FROM insegnamento i join docenti d on i.docente = d.id WHERE i.corso = '%s'",course.getTitolo());

            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Docente p = new Docente(rs.getString("d.nome"), rs.getString("d.cognome"));
                out.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
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

    public static void insertPrenotation(Prenotazione prenotazione){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("INSERT INTO `prenotazioni`(`corso`, `docente`, `utente`) VALUES ('%s','%d','%d')", prenotazione.getCorso(), prenotazione.getDocente(), prenotazione.getUtente());
            Statement st = conn.createStatement();
            if(st.executeUpdate(query) != 0)
                System.out.println("L'elemento è stato aggiunto al database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void removePrenotation(Prenotazione prenotazione){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test");
            }
            String query = String.format("DELETE FROM `prenotazioni` WHERE corso = '%s' and docente ='%d' and utente = '%d'", prenotazione.getCorso(), prenotazione.getDocente(), prenotazione.getUtente());
            Statement st = conn.createStatement();
            if(st.executeUpdate(query) != 0)
                System.out.println("L'elemento è stato rimosso dal database!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }
}
