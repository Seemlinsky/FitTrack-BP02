package nl.adainf.fittrack.database;



/*
 * Database.java - centrale plek om een DB-verbinding te maken
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - vooral uitleg bij DB/CRUD (verbinden, query uitvoeren, ResultSet lezen)
 * - geen moeilijke termen zonder uitleg
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    // JDBC URL: waar staat de database + welke database gebruiken we?
    // (hier: MySQL op localhost, database: fittrack)
    private static final String URL =
            "jdbc:mysql://localhost:3306/fittrack?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    // DB gebruiker (bij XAMPP is dit vaak "root")
    private static final String USER = "root";
    // DB wachtwoord (bij XAMPP is dit vaak leeg)
    private static final String PASS = ""; // XAMPP meestal leeg

    // Deze methode geeft een "open" verbinding terug.
    // Let op: de caller gebruikt try-with-resources zodat de verbinding weer dicht gaat.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}