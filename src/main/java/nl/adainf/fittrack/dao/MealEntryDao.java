package nl.adainf.fittrack.dao;



/*
 * MealEntryDao.java - DAO: praat met de database voor meal entries (eten)
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - vooral uitleg bij DB/CRUD (verbinden, query uitvoeren, ResultSet lezen)
 * - geen moeilijke termen zonder uitleg
 */

import nl.adainf.fittrack.database.Database;
import nl.adainf.fittrack.model.MealEntry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// DAO = Data Access Object.
// Hier doen we alle SQL (CRUD) en zetten we DB-rijen om naar model-objecten.
public class MealEntryDao {

    public int insert(MealEntry m) {
        // SQL die we naar MySQL sturen (met ? zodat we values veilig kunnen invullen)

        String sql = "INSERT INTO meal_entry(user_id, meal_date, meal_time, meal_type, meal_name, calories) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        // try-with-resources: Java sluit conn/ps/rs automatisch (scheelt fouten/lekken)
        try (Connection conn = Database.getConnection();
             // PreparedStatement: werkt met ? placeholders (makkelijker en netter dan strings aan elkaar plakken)
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, m.getUserId());
            ps.setDate(2, Date.valueOf(m.getMealDate()));
            ps.setTime(3, Time.valueOf(m.getMealTime()));
            ps.setString(4, m.getMealType());
            ps.setString(5, m.getMealName());
            ps.setInt(6, m.getCalories());
            // executeUpdate = INSERT/UPDATE/DELETE uitvoeren

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            // Bij fouten printen we de melding (voor school is dit genoeg).
            e.printStackTrace();
        }
        return -1;
    }

    public boolean update(MealEntry m) {
        // SQL die we naar MySQL sturen (met ? zodat we values veilig kunnen invullen)

        String sql = "UPDATE meal_entry SET meal_date=?, meal_time=?, meal_type=?, meal_name=?, calories=? " +
                "WHERE id=? AND user_id=?";
        // try-with-resources: Java sluit conn/ps/rs automatisch (scheelt fouten/lekken)
        try (Connection conn = Database.getConnection();
             // PreparedStatement: werkt met ? placeholders (makkelijker en netter dan strings aan elkaar plakken)
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(m.getMealDate()));
            ps.setTime(2, Time.valueOf(m.getMealTime()));
            ps.setString(3, m.getMealType());
            ps.setString(4, m.getMealName());
            ps.setInt(5, m.getCalories());
            ps.setInt(6, m.getId());
            ps.setInt(7, m.getUserId());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            // Bij fouten printen we de melding (voor school is dit genoeg).
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id, int userId) {
        // SQL die we naar MySQL sturen (met ? zodat we values veilig kunnen invullen)

        String sql = "DELETE FROM meal_entry WHERE id=? AND user_id=?";
        // try-with-resources: Java sluit conn/ps/rs automatisch (scheelt fouten/lekken)
        try (Connection conn = Database.getConnection();
             // PreparedStatement: werkt met ? placeholders (makkelijker en netter dan strings aan elkaar plakken)
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            // Bij fouten printen we de melding (voor school is dit genoeg).
            e.printStackTrace();
        }
        return false;
    }

    // WHERE + ORDER BY
    public List<MealEntry> getByUserAndDate(int userId, LocalDate date) {
        List<MealEntry> list = new ArrayList<>();
        // SQL die we naar MySQL sturen (met ? zodat we values veilig kunnen invullen)

        String sql = "SELECT * FROM meal_entry WHERE user_id=? AND meal_date=? ORDER BY meal_time ASC";
        // try-with-resources: Java sluit conn/ps/rs automatisch (scheelt fouten/lekken)
        try (Connection conn = Database.getConnection();
             // PreparedStatement: werkt met ? placeholders (makkelijker en netter dan strings aan elkaar plakken)
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(date));

            // executeQuery = SELECT uitvoeren (ResultSet = de tabel met resultaten)


            try (ResultSet rs = ps.executeQuery()) {
                // rs.next() = ga naar de volgende rij in het resultaat
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            // Bij fouten printen we de melding (voor school is dit genoeg).
            e.printStackTrace();
        }
        return list;
    }

    // GROUP BY + ORDER BY (query clause requirement)
    public List<String> getCaloriesPerMealType(int userId, LocalDate date) {
        List<String> out = new ArrayList<>();
        // SQL die we naar MySQL sturen (met ? zodat we values veilig kunnen invullen)

        String sql = "SELECT meal_type, SUM(calories) AS total_cal " +
                "FROM meal_entry WHERE user_id=? AND meal_date=? " +
                "GROUP BY meal_type " +
                "ORDER BY total_cal DESC " +
                "LIMIT 5";
        // try-with-resources: Java sluit conn/ps/rs automatisch (scheelt fouten/lekken)
        try (Connection conn = Database.getConnection();
             // PreparedStatement: werkt met ? placeholders (makkelijker en netter dan strings aan elkaar plakken)
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(date));

            // executeQuery = SELECT uitvoeren (ResultSet = de tabel met resultaten)


            try (ResultSet rs = ps.executeQuery()) {
                // rs.next() = ga naar de volgende rij in het resultaat
                while (rs.next()) {
                    out.add(rs.getString("meal_type") + ": " + rs.getInt("total_cal") + " kcal");
                }
            }
        } catch (SQLException e) {
            // Bij fouten printen we de melding (voor school is dit genoeg).
            e.printStackTrace();
        }
        return out;
    }

    public int getTotalCaloriesForDay(int userId, LocalDate date) {
        // SQL die we naar MySQL sturen (met ? zodat we values veilig kunnen invullen)

        String sql = "SELECT COALESCE(SUM(calories), 0) AS total_cal FROM meal_entry WHERE user_id=? AND meal_date=?";
        // try-with-resources: Java sluit conn/ps/rs automatisch (scheelt fouten/lekken)
        try (Connection conn = Database.getConnection();
             // PreparedStatement: werkt met ? placeholders (makkelijker en netter dan strings aan elkaar plakken)
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(date));

            // executeQuery = SELECT uitvoeren (ResultSet = de tabel met resultaten)


            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total_cal");
            }
        } catch (SQLException e) {
            // Bij fouten printen we de melding (voor school is dit genoeg).
            e.printStackTrace();
        }
        return 0;
    }

    private MealEntry map(ResultSet rs) throws SQLException {
        return new MealEntry(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getDate("meal_date").toLocalDate(),
                rs.getTime("meal_time").toLocalTime(),
                rs.getString("meal_type"),
                rs.getString("meal_name"),
                rs.getInt("calories")
        );
    }
}
