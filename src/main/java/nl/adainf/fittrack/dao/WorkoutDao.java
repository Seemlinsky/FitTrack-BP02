package nl.adainf.fittrack.dao;

/*
 * WorkoutDao.java
 *
 * Deze DAO praat met de database voor workouts (trainingsdagen).
 * Een workout hoort bij 1 user en 1 datum.
 *
 * AD-lesstof-stijl:
 * - simpele uitleg
 * - vooral bij DB/CRUD: verbinden, query uitvoeren, ResultSet lezen
 */

import nl.adainf.fittrack.database.Database;
import nl.adainf.fittrack.model.Workout;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

// DAO = Data Access Object.
public class WorkoutDao {

    // C = Create: nieuwe workout toevoegen.
    // Return: nieuw id (of -1 als het mislukt).
    public int insert(Workout w) {
        String sql = "INSERT INTO workout(user_id, workout_date, note) VALUES(?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, w.getUserId());
            ps.setDate(2, Date.valueOf(w.getWorkoutDate()));
            ps.setString(3, w.getNote());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /*
     * R = Read
     * Haal alle workouts op van 1 user.
     * ORDER BY zorgt dat de nieuwste datum bovenaan staat.
     */
    public ArrayList<Workout> getByUser(int userId) {
        ArrayList<Workout> list = new ArrayList<>();

        String sql = "SELECT * FROM workout WHERE user_id=? ORDER BY workout_date DESC, id DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Workout(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getDate("workout_date").toLocalDate(),
                            rs.getString("note")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /*
     * Deze methode zoekt een workout voor (userId + datum).
     * Bestaat hij nog niet? Dan maken we hem aan.
     * Handig zodat je later entries kan toevoegen aan "de workout van die dag".
     */
    public int findOrCreateWorkout(int userId, LocalDate date) {
        // Eerst zoeken we of het record al bestaat.
        String findSql = "SELECT id FROM workout WHERE user_id=? AND workout_date=? LIMIT 1";

        // Als het niet bestaat, maken we het aan.
        String insertSql = "INSERT INTO workout(user_id, workout_date, note) VALUES(?, ?, NULL)";

        try (Connection conn = Database.getConnection()) {

            // 1) Probeer te vinden (READ)
            try (PreparedStatement ps = conn.prepareStatement(findSql)) {
                ps.setInt(1, userId);
                ps.setDate(2, Date.valueOf(date));

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("id");
                }
            }

            // 2) Niet gevonden -> aanmaken (CREATE)
            try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setDate(2, Date.valueOf(date));

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // U = Update: update alleen de notitie van een workout.
    public boolean updateNote(int workoutId, String note) {
        String sql = "UPDATE workout SET note=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, note);
            ps.setInt(2, workoutId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // D = Delete: verwijder een workout op id.
    // Door ON DELETE CASCADE worden workout_entry records ook verwijderd.
    public boolean delete(int workoutId) {
        String sql = "DELETE FROM workout WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, workoutId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}