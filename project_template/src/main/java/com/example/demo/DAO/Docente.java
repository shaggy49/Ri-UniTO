package com.example.demo.DAO;

import java.sql.*;

public class Docente {
    private static final String url1 = "jdbc:mysql://localhost:3306/test";
    private static final String user = "root";
    private static final String password = "";

    private String nome;
    private String cognome;

    public Docente(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    /**
     * @return Id's teacher if present in the database!!
     */
    public int getId(){
        Connection conn1 = null;
        int id = 0;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test");
            }

            String query = String.format("SELECT id FROM docenti where nome = '%s' and cognome = '%s'", this.nome, this.cognome);

            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("ID");
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

    @Override
    public String toString() {
        return nome + " " + cognome + " ";
    }
}
