package com.reservation.application;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {

    /*public static void registerDriver(String url, String usr, String pword) {
        try {
            url1 = url;
            user = usr;
            password = pword;
            DriverManager.registerDriver(new com.mysql.jdbc.Driver()); //era rossa perché non erano stati caricati i driver
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }*/

    public static Connection connectToDB() {
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:reservation.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return connection;
    }

}
