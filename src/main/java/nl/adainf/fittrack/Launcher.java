package nl.adainf.fittrack;

/*
 * Launcher.java
 *
 * Dit is het startpunt van de applicatie.
 *
 * Deze klasse start de JavaFX applicatie door MainApp te openen.
 * MainApp regelt daarna de Stage, Scene en de schermen.
 */

import javafx.application.Application;

public class Launcher {

    // main is het beginpunt als je de app start.
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}