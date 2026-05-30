package nl.adainf.fittrack.dao;

/*
 * WorkoutEntryDao.java
 *
 * Deze klasse regelt het databasewerk voor workout entries.
 *
 * Een workout entry is één activiteit binnen een workout.
 * Bijvoorbeeld:
 * - activiteit: hardlopen
 * - minuten: 30
 * - calorieën: 250
 *
 * In dit bestand staat SQL-code voor workout_entry.
 * Het scherm zelf hoeft daardoor geen SQL te kennen.
 */

import nl.adainf.fittrack.database.Database;
import nl.adainf.fittrack.model.WorkoutEntry;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkoutEntryDao {

    // Deze methode voegt een nieuwe workout entry toe aan de database.
    // Als het lukt, geef ik het nieuwe id terug.
    // Als het niet lukt, geef ik -1 terug.
    public int insert(WorkoutEntry entry) {
        // INSERT voegt een nieuwe rij toe aan de workout_entry tabel.
        String sql = "INSERT INTO workout_entry (workout_id, activity_type_id, minutes, calories) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Hier vul ik de vraagtekens in de SQL-query.
            statement.setInt(1, entry.getWorkoutId());
            statement.setInt(2, entry.getActivityTypeId());
            statement.setInt(3, entry.getMinutes());
            statement.setInt(4, entry.getCalories());

            // executeUpdate gebruik ik bij INSERT, UPDATE en DELETE.
            statement.executeUpdate();

            // MySQL maakt zelf een id aan. Die haal ik hier op.
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    // Deze methode past een bestaande workout entry aan.
    public boolean update(WorkoutEntry entry) {
        // UPDATE past activiteit, minuten en calorieën aan.
        String sql = "UPDATE workout_entry SET activity_type_id = ?, minutes = ?, calories = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, entry.getActivityTypeId());
            statement.setInt(2, entry.getMinutes());
            statement.setInt(3, entry.getCalories());
            statement.setInt(4, entry.getId());

            // Als er 1 rij aangepast is, is de update gelukt.
            return statement.executeUpdate() == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // Deze methode verwijdert één workout entry.
    public boolean delete(int id, int workoutId) {
        // DELETE verwijdert de entry met het gekozen id.
        // workout_id staat erbij als extra check, zodat ik niet per ongeluk iets verkeerds verwijder.
        String sql = "DELETE FROM workout_entry WHERE id = ? AND workout_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setInt(2, workoutId);

            return statement.executeUpdate() == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // Deze methode haalt alle trainingen op van één gebruiker op één dag.
    public List<WorkoutEntry> getEntriesForDay(int userId, LocalDate date) {
        List<WorkoutEntry> list = new ArrayList<>();

        // SELECT haalt trainingregels op.
        // JOIN gebruik ik om ook de naam van de activiteit uit activity_type op te halen.
        String sql = "SELECT we.id, we.workout_id, we.activity_type_id, we.minutes, we.calories, at.name AS activity_name " +
                "FROM workout_entry we " +
                "JOIN workout w ON w.id = we.workout_id " +
                "JOIN activity_type at ON at.id = we.activity_type_id " +
                "WHERE w.user_id = ? AND w.workout_date = ? " +
                "ORDER BY we.id DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setDate(2, Date.valueOf(date));

            // executeQuery gebruik ik bij SELECT.
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    WorkoutEntry entry = new WorkoutEntry(
                            resultSet.getInt("id"),
                            resultSet.getInt("workout_id"),
                            resultSet.getInt("activity_type_id"),
                            resultSet.getString("activity_name"),
                            resultSet.getInt("minutes"),
                            resultSet.getInt("calories")
                    );

                    list.add(entry);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    // Deze methode telt alle verbrande calorieën op voor één dag.
    public int getTotalCaloriesForDay(int userId, LocalDate date) {
        // SUM telt calorieën bij elkaar op.
        // COALESCE zorgt dat ik 0 terugkrijg als er nog geen trainingen zijn.
        String sql = "SELECT COALESCE(SUM(we.calories), 0) AS total " +
                "FROM workout_entry we " +
                "JOIN workout w ON w.id = we.workout_id " +
                "WHERE w.user_id = ? AND w.workout_date = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setDate(2, Date.valueOf(date));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    // Deze methode telt alle actieve minuten op voor één dag.
    public int getTotalMinutesForDay(int userId, LocalDate date) {
        // SUM telt minuten bij elkaar op.
        // COALESCE zorgt dat ik 0 terugkrijg als er nog geen trainingen zijn.
        String sql = "SELECT COALESCE(SUM(we.minutes), 0) AS total " +
                "FROM workout_entry we " +
                "JOIN workout w ON w.id = we.workout_id " +
                "WHERE w.user_id = ? AND w.workout_date = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setDate(2, Date.valueOf(date));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }
}