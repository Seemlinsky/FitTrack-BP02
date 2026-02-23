package nl.adainf.fittrack.dao;

/*
 * UserDao.java
 *
 * Deze DAO praat met de database voor users.
 * We kunnen users ophalen, toevoegen en verwijderen.
 *
 * AD-lesstof-stijl:
 * - simpele uitleg
 * - vooral bij DB/CRUD: verbinden, query uitvoeren, ResultSet lezen
 */

import nl.adainf.fittrack.database.Database;
import nl.adainf.fittrack.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO = Data Access Object.
public class UserDao {

    // R = Read: haal alle users op.
    public List<User> getAll() {
        List<User> list = new ArrayList<>();

        // We hebben hier geen parameters, maar PreparedStatement is nog steeds prima.
        // ORDER BY id DESC = nieuwste user bovenaan.
        String sql = "SELECT id, name FROM users ORDER BY id DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User(
                        rs.getInt("id"),
                        rs.getString("name")
                );
                list.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // C = Create: maak een nieuwe user aan.
    // Return: nieuw id (of -1 als het mislukt).
    public int insert(String name) {
        String sql = "INSERT INTO users(name) VALUES(?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.executeUpdate();

            // Het id dat MySQL heeft gemaakt (AUTO_INCREMENT)
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // D = Delete: verwijder een user op id.
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}