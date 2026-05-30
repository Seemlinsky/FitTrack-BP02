package nl.adainf.fittrack.dao;

/*
 * MealEntryDao.java
 *
 * Deze klasse regelt het databasewerk voor maaltijden.
 *
 * In dit bestand staat SQL-code voor meal_entry.
 * Het scherm zelf hoeft daardoor geen SQL te kennen.
 *
 * Hier worden maaltijden toegevoegd, aangepast, verwijderd en opgehaald.
 */

import nl.adainf.fittrack.database.Database;
import nl.adainf.fittrack.model.MealEntry;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MealEntryDao {

    // Deze methode voegt een nieuwe maaltijd toe aan de database.
    // Als het lukt, geef ik het nieuwe id terug.
    // Als het niet lukt, geef ik -1 terug.
    public int insert(MealEntry meal) {
        // INSERT voegt een nieuwe rij toe aan de meal_entry tabel.
        String sql = "INSERT INTO meal_entry(user_id, meal_date, meal_time, meal_type, meal_name, calories) " +
                "VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Hier vul ik de vraagtekens in de SQL-query.
            statement.setInt(1, meal.getUserId());
            statement.setDate(2, Date.valueOf(meal.getMealDate()));
            statement.setTime(3, Time.valueOf(meal.getMealTime()));
            statement.setString(4, meal.getMealType());
            statement.setString(5, meal.getMealName());
            statement.setInt(6, meal.getCalories());

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

    // Deze methode past een bestaande maaltijd aan.
    public boolean update(MealEntry meal) {
        // UPDATE past de gegevens van een bestaande maaltijd aan.
        String sql = "UPDATE meal_entry SET meal_date=?, meal_time=?, meal_type=?, meal_name=?, calories=? " +
                "WHERE id=? AND user_id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setDate(1, Date.valueOf(meal.getMealDate()));
            statement.setTime(2, Time.valueOf(meal.getMealTime()));
            statement.setString(3, meal.getMealType());
            statement.setString(4, meal.getMealName());
            statement.setInt(5, meal.getCalories());
            statement.setInt(6, meal.getId());
            statement.setInt(7, meal.getUserId());

            // Als er 1 rij is aangepast, is de update gelukt.
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Deze methode verwijdert een maaltijd uit de database.
    public boolean delete(int id, int userId) {
        // DELETE verwijdert de maaltijd met het gekozen id.
        // user_id staat erbij als extra check.
        String sql = "DELETE FROM meal_entry WHERE id=? AND user_id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setInt(2, userId);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Deze methode haalt alle maaltijden op van één gebruiker op één datum.
    public List<MealEntry> getByUserAndDate(int userId, LocalDate date) {
        List<MealEntry> list = new ArrayList<>();

        // SELECT haalt de maaltijden op.
        // WHERE filtert op gebruiker en datum.
        // ORDER BY zet de maaltijden op tijdvolgorde.
        String sql = "SELECT * FROM meal_entry WHERE user_id=? AND meal_date=? ORDER BY meal_time ASC";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setDate(2, Date.valueOf(date));

            // executeQuery gebruik ik bij SELECT.
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(map(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Deze methode telt calorieën per maaltijdtype op.
    // Bijvoorbeeld hoeveel calorieën bij Lunch of Dinner horen.
    public List<String> getCaloriesPerMealType(int userId, LocalDate date) {
        List<String> result = new ArrayList<>();

        // GROUP BY groepeert de maaltijden per type.
        // SUM telt de calorieën per groep op.
        // LIMIT 5 zorgt dat er maximaal 5 resultaten terugkomen.
        String sql = "SELECT meal_type, SUM(calories) AS total_cal " +
                "FROM meal_entry WHERE user_id=? AND meal_date=? " +
                "GROUP BY meal_type " +
                "ORDER BY total_cal DESC " +
                "LIMIT 5";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setDate(2, Date.valueOf(date));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String line = resultSet.getString("meal_type") + ": " +
                            resultSet.getInt("total_cal") + " kcal";

                    result.add(line);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Deze methode telt alle gegeten calorieën op voor één dag.
    public int getTotalCaloriesForDay(int userId, LocalDate date) {
        // SUM telt alle calorieën op.
        // COALESCE zorgt dat ik 0 terugkrijg als er nog geen maaltijden zijn.
        String sql = "SELECT COALESCE(SUM(calories), 0) AS total_cal " +
                "FROM meal_entry WHERE user_id=? AND meal_date=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setDate(2, Date.valueOf(date));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total_cal");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Deze hulpmethode maakt van één database-rij een MealEntry object.
    private MealEntry map(ResultSet resultSet) throws SQLException {
        return new MealEntry(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getDate("meal_date").toLocalDate(),
                resultSet.getTime("meal_time").toLocalTime(),
                resultSet.getString("meal_type"),
                resultSet.getString("meal_name"),
                resultSet.getInt("calories")
        );
    }
}