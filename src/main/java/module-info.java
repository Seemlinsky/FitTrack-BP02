/*
 * module-info.java
 *
 * Hier staat welke Java modules mijn project gebruikt.
 *
 * javafx.controls is nodig voor JavaFX onderdelen zoals Button, Label en TextField.
 * javafx.fxml staat erbij voor JavaFX ondersteuning.
 * java.sql is nodig voor de databaseverbinding met MySQL.
 */

module nl.adainf.fittrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens nl.adainf.fittrack to javafx.fxml;
    opens nl.adainf.fittrack.screens to javafx.fxml;

    exports nl.adainf.fittrack;
}