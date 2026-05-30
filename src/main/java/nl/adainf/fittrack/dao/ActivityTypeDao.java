package nl.adainf.fittrack.dao;

/*
 * ActivityTypeDao.java
 *
 * Deze klasse regelt het databasewerk voor activity types.
 * Activity types zijn soorten trainingen, zoals hardlopen of fitness.
 *
 * In dit bestand staat SQL-code.
 * Het scherm zelf hoeft daardoor geen SQL te kennen.
 *
 * Deze DAO haalt activity types op uit de database
 * en maakt daar ActivityType objecten van.
 */

import nl.adainf.fittrack.database.Database;
import nl.adainf.fittrack.model.ActivityType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ActivityTypeDao {

    // Deze methode haalt alle activity types op uit de database.
    public List<ActivityType> getAll() {
        List<ActivityType> list = new ArrayList<>();

        // SELECT haalt data op uit de tabel activity_type.
        // ORDER BY zorgt dat de activiteiten op naam gesorteerd worden.
        String sql = "SELECT id, name FROM activity_type ORDER BY name ASC";

        // try-with-resources sluit Connection, Statement en ResultSet automatisch.
        try (Connection conn = Database.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // ResultSet bevat de rijen die uit de database terugkomen.
            // Met resultSet.next() loop ik rij voor rij door het resultaat.
            while (resultSet.next()) {
                ActivityType activityType = new ActivityType(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );

                list.add(activityType);
            }

        } catch (SQLException e) {
            // Als de databasequery fout gaat, print ik de foutmelding.
            e.printStackTrace();
        }

        return list;
    }
}