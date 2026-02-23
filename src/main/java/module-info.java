/*
 * module-info.java - module settings voor JavaFX / dependencies
 * Studenten-comment versie (simpel uitgelegd).
 */

module nl.adainf.fittrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens nl.adainf.fittrack to javafx.fxml;
    opens nl.adainf.fittrack.screens to javafx.fxml;

    exports nl.adainf.fittrack;
}