package nl.adainf.fittrack.dao;

/*
 * WorkoutEntryDao.java
 *
 * Deze DAO praat met de database voor workout entries (de details in een workout).
 * Voorbeeld: per workout heb je meerdere entries (activiteit + minuten + calories).
 *
 * AD-lesstof-stijl:
 * - simpele uitleg
 * - uitleg bij DB/CRUD: query uitvoeren + ResultSet lezen
 */

import nl.adainf.fittrack.database.Database;
import nl.adainf.fittrack.model.WorkoutEntry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// DAO = Data Access Object.
// In deze class staat alle SQL voor de tabel workout_entry.
public class WorkoutEntryDao {

    // C = Create: nieuwe workout entry toevoegen.
    // Return: nieuw id (of -1 als het mislukt).
    public int insert(WorkoutEntry e) {
        String sql = "INSERT INTO workout_entry (workout_id, activity_type_id, minutes, calories) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, e.getWorkoutId());
            ps.setInt(2, e.getActivityTypeId());
            ps.setInt(3, e.getMinutes());
            ps.setInt(4, e.getCalories());

            ps.executeUpdate();

            // Het id dat MySQL heeft gemaakt (AUTO_INCREMENT)
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    // U = Update: bestaande entry aanpassen.
    public boolean update(WorkoutEntry e) {
        String sql = "UPDATE workout_entry SET activity_type_id = ?, minutes = ?, calories = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, e.getActivityTypeId());
            ps.setInt(2, e.getMinutes());
            ps.setInt(3, e.getCalories());
            ps.setInt(4, e.getId());

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /*
     * D = Delete: verwijder 1 entry.
     * We geven ook workoutId mee als extra check:
     * zo verwijderen we niet per ongeluk een entry die bij een andere workout hoort.
     */
    public boolean delete(int id, int workoutId) {
        String sql = "DELETE FROM workout_entry WHERE id = ? AND workout_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, workoutId);

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /*
     * R = Read: alle workout entries voor een user op een bepaalde dag.
     * We gebruiken JOIN zodat we ook de naam van de activity_type kunnen tonen.
     */
    public List<WorkoutEntry> getEntriesForDay(int userId, LocalDate date) {
        List<WorkoutEntry> list = new ArrayList<>();

        String sql = "SELECT we.id, we.workout_id, we.activity_type_id, we.minutes, we.calories, at.name AS activity_name " +
                "FROM workout_entry we " +
                "JOIN workout w ON w.id = we.workout_id " +
                "JOIN activity_type at ON at.id = we.activity_type_id " +
                "WHERE w.user_id = ? AND w.workout_date = ? " +
                "ORDER BY we.id DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WorkoutEntry e = new WorkoutEntry(
                            rs.getInt("id"),
                            rs.getInt("workout_id"),
                            rs.getInt("activity_type_id"),
                            rs.getString("activity_name"),
                            rs.getInt("minutes"),
                            rs.getInt("calories")
                    );
                    list.add(e);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    // Extra query: totaal calorieën van alle entries op een dag (JOIN + WHERE + SUM).
    public int getTotalCaloriesForDay(int userId, LocalDate date) {
        String sql = "SELECT COALESCE(SUM(we.calories), 0) AS total " +
                "FROM workout_entry we " +
                "JOIN workout w ON w.id = we.workout_id " +
                "WHERE w.user_id = ? AND w.workout_date = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    // Extra query: totaal minuten van alle entries op een dag (JOIN + WHERE + SUM).
    public int getTotalMinutesForDay(int userId, LocalDate date) {
        String sql = "SELECT COALESCE(SUM(we.minutes), 0) AS total " +
                "FROM workout_entry we " +
                "JOIN workout w ON w.id = we.workout_id " +
                "WHERE w.user_id = ? AND w.workout_date = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }
}