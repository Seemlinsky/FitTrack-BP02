package nl.adainf.fittrack.dao;

/*
 * ActivityTypeDao.java
 *
 * Deze DAO praat met de database voor activity types (soorten trainingen).
 * Hier doen we de SQL en zetten we de rijen uit de database om naar ActivityType objecten.
 *
 * AD-lesstof-stijl:
 * - simpele uitleg
 * - uitleg bij: query uitvoeren + ResultSet lezen
 */

import nl.adainf.fittrack.database.Database;
import nl.adainf.fittrack.model.ActivityType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DAO = Data Access Object.
// In een DAO staat alle database-code (SQL). De rest van je app hoeft geen SQL te kennen.
public class ActivityTypeDao {

    // READ: haal alle activity types op (gesorteerd op naam).
    public List<ActivityType> getAll() {
        List<ActivityType> list = new ArrayList<>();

        // We halen id en name op uit de tabel activity_type.
        // ORDER BY zorgt dat de lijst alfabetisch is.
        String sql = "SELECT id, name FROM activity_type ORDER BY name ASC";

        // try-with-resources: Connection/Statement/ResultSet worden automatisch gesloten.
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // ResultSet = tabel met resultaten.
            // rs.next() gaat rij voor rij door de resultaten heen.
            while (rs.next()) {
                list.add(new ActivityType(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            // Voor school is printen van de foutmelding meestal genoeg.
            e.printStackTrace();
        }

        return list;
    }
}