package nl.adainf.fittrack;



/*
 * Launcher.java - kleine wrapper om JavaFX netjes te starten
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - vooral uitleg bij DB/CRUD (verbinden, query uitvoeren, ResultSet lezen)
 * - geen moeilijke termen zonder uitleg
 */

import javafx.application.Application;

// Soms helpt een aparte Launcher bij packaging/running (zeker met IDE + JavaFX).
public class Launcher {
    // main = startpunt van de applicatie
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}
