package nl.adainf.fittrack.dao;

/*
 * WorkoutDao.java
 *
 * Deze klasse regelt het databasewerk voor workouts.
 *
 * Een workout is bij mij eigenlijk de trainingsdag van een gebruiker.
 * Een workout hoort dus bij:
 * - een gebruiker
 * - een datum
 * - eventueel een notitie
 *
 * In dit bestand staat SQL-code.
 * Het scherm zelf hoeft daardoor geen SQL te kennen.
 */

import nl.adainf.fittrack.database.Database;
import nl.adainf.fittrack.model.Workout;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class WorkoutDao {

    // Deze methode voegt een nieuwe workout toe aan de database.
    // Als het lukt, geef ik het nieuwe id terug.
    // Als het niet lukt, geef ik -1 terug.
    public int insert(Workout workout) {
        // INSERT voegt een nieuwe rij toe aan de workout tabel.
        String sql = "INSERT INTO workout(user_id, workout_date, note) VALUES(?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Hier vul ik de vraagtekens in de SQL-query.
            statement.setInt(1, workout.getUserId());
            statement.setDate(2, Date.valueOf(workout.getWorkoutDate()));
            statement.setString(3, workout.getNote());

            // executeUpdate gebruik ik bij INSERT, UPDATE en DELETE.
            statement.executeUpdate();

            // MySQL maakt zelf een id aan. Die haal ik hier op.
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

    // Deze methode haalt alle workouts op van één gebruiker.
    public ArrayList<Workout> getByUser(int userId) {
        ArrayList<Workout> list = new ArrayList<>();

        // SELECT haalt alle workouts op van de gekozen gebruiker.
        // ORDER BY zorgt dat de nieuwste workout bovenaan staat.
        String sql = "SELECT * FROM workout WHERE user_id=? ORDER BY workout_date DESC, id DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            // Hier vul ik het vraagteken met het id van de gebruiker.
            statement.setInt(1, userId);

            // executeQuery gebruik ik bij SELECT.
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Workout workout = new Workout(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getDate("workout_date").toLocalDate(),
                            resultSet.getString("note")
                    );

                    list.add(workout);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Deze methode zoekt de workout van een gebruiker op een bepaalde datum.
    // Als die workout nog niet bestaat, maak ik hem meteen aan.
    public int findOrCreateWorkout(int userId, LocalDate date) {
        // Eerst kijk ik of er al een workout bestaat voor deze gebruiker en datum.
        String findSql = "SELECT id FROM workout WHERE user_id=? AND workout_date=? LIMIT 1";

        // Als er nog geen workout bestaat, voeg ik er één toe.
        String insertSql = "INSERT INTO workout(user_id, workout_date, note) VALUES(?, ?, NULL)";

        try (Connection conn = Database.getConnection()) {

            // Stap 1: workout zoeken met SELECT.
            try (PreparedStatement statement = conn.prepareStatement(findSql)) {
                statement.setInt(1, userId);
                statement.setDate(2, Date.valueOf(date));

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }

            // Stap 2: workout bestaat nog niet, dus ik maak hem aan met INSERT.
            try (PreparedStatement statement = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, userId);
                statement.setDate(2, Date.valueOf(date));

                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Deze methode past alleen de notitie van een workout aan.
    public boolean updateNote(int workoutId, String note) {
        // UPDATE past een bestaande workout aan.
        String sql = "UPDATE workout SET note=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, note);
            statement.setInt(2, workoutId);

            // Als er 1 rij is aangepast, is de update gelukt.
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Deze methode verwijdert een workout uit de database.
    public boolean delete(int workoutId) {
        // DELETE verwijdert de workout met het gekozen id.
        String sql = "DELETE FROM workout WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, workoutId);

            // Door ON DELETE CASCADE worden gekoppelde workout_entry records ook verwijderd.
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}