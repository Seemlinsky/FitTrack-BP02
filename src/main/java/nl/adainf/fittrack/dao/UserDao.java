package nl.adainf.fittrack.dao;

/*
 * UserDao.java
 *
 * Deze klasse regelt het databasewerk voor gebruikers.
 *
 * In dit bestand staat SQL-code voor users.
 * Het StartScreen gebruikt deze DAO om gebruikers op te halen,
 * toe te voegen en te verwijderen.
 *
 * Hierdoor staat de SQL niet direct in het scherm.
 */

import nl.adainf.fittrack.database.Database;
import nl.adainf.fittrack.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // Deze methode haalt alle gebruikers op uit de database.
    public List<User> getAll() {
        List<User> list = new ArrayList<>();

        // SELECT haalt id en name op uit de users tabel.
        // ORDER BY id DESC zorgt dat de nieuwste gebruiker bovenaan staat.
        String sql = "SELECT id, name FROM users ORDER BY id DESC";

        // PreparedStatement voert de SQL-query uit.
        // ResultSet bevat de rijen die uit de database terugkomen.
        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Met resultSet.next() loop ik rij voor rij door de resultaten.
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );

                list.add(user);
            }

        } catch (SQLException e) {
            // Als de databasequery fout gaat, print ik de foutmelding.
            e.printStackTrace();
        }

        return list;
    }

    // Deze methode voegt een nieuwe gebruiker toe aan de database.
    // Als het lukt, geef ik het nieuwe id terug.
    // Als het niet lukt, geef ik -1 terug.
    public int insert(String name) {
        // INSERT voegt een nieuwe rij toe aan de users tabel.
        // Het vraagteken wordt later gevuld met de naam.
        String sql = "INSERT INTO users(name) VALUES(?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Hier vul ik het vraagteken in de SQL-query met de gebruikersnaam.
            statement.setString(1, name);

            // executeUpdate gebruik ik bij INSERT, UPDATE en DELETE.
            statement.executeUpdate();

            // MySQL maakt zelf een id aan met AUTO_INCREMENT.
            // Met getGeneratedKeys haal ik dat nieuwe id op.
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Deze methode verwijdert een gebruiker uit de database.
    public boolean delete(int id) {
        // DELETE verwijdert de gebruiker met het gekozen id.
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            // Hier vul ik het vraagteken met het id van de gebruiker.
            statement.setInt(1, id);

            // executeUpdate geeft terug hoeveel rijen aangepast/verwijderd zijn.
            // Als dit 1 is, is het verwijderen gelukt.
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}