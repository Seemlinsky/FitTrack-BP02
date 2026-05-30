package nl.adainf.fittrack.database;

/*
 * Database.java
 *
 * Dit bestand gebruik ik om verbinding te maken met de MySQL database.
 *
 * Alle DAO-klassen gebruiken deze methode.
 * Daardoor hoef ik de databasegegevens niet in elke DAO opnieuw te zetten.
 *
 * In de reader gaat dit over:
 * - JDBC
 * - Connection
 * - verbinden met een externe database
 *
 * Hier staan nog geen SELECT, INSERT, UPDATE of DELETE queries.
 * Die staan in de DAO-klassen.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    // URL van de database.
    // localhost betekent dat de database op mijn eigen computer draait.
    // fittrack is de naam van de database.
    private static final String URL =
            "jdbc:mysql://localhost:3306/fittrack?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    // Gebruikersnaam van MySQL. Bij XAMPP is dit meestal root.
    private static final String USER = "root";

    // Wachtwoord van MySQL. Bij XAMPP is dit vaak leeg.
    private static final String PASS = "";

    // Deze methode maakt een verbinding met de database en geeft die terug.
    // De DAO-klassen gebruiken deze verbinding om SQL uit te voeren.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}