package nl.adainf.fittrack;

/*
 * MainApp.java
 *
 * Dit is de hoofdklasse van de JavaFX applicatie.
 *
 * Hier wordt het hoofdvenster gemaakt met Stage.
 * Daarna wissel ik tussen de verschillende schermen:
 * - StartScreen
 * - WorkoutScreen
 * - OverviewScreen
 *
 * Deze class doet zelf geen databasewerk.
 * De databasecode staat in de DAO-klassen.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.adainf.fittrack.screens.OverviewScreen;
import nl.adainf.fittrack.screens.StartScreen;
import nl.adainf.fittrack.screens.WorkoutScreen;

import java.util.Objects;

public class MainApp extends Application {

    // Stage is het hoofdvenster van de JavaFX app.
    private Stage stage;

    // Hier bewaar ik de gekozen gebruiker.
    private int userId;

    // start() wordt automatisch door JavaFX aangeroepen bij het starten van de app.
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        stage.setTitle("FitTrack");
        showStart();
        stage.show();
    }

    // Deze methode voegt de CSS-stijl toe aan een scherm.
    private void applyStyles(Scene scene) {
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );
    }

    // Dit laat het beginscherm zien.
    private void showStart() {
        StartScreen startScreen = new StartScreen(id -> {
            this.userId = id;
            showWorkout();
        });

        Scene scene = startScreen.getScene();
        applyStyles(scene);

        // Scene wisselen betekent dat ik een ander scherm laat zien.
        stage.setScene(scene);
    }

    // Dit laat het workoutscherm zien.
    private void showWorkout() {
        WorkoutScreen workoutScreen = new WorkoutScreen(userId, this::showOverview);

        Scene scene = workoutScreen.getScene();
        applyStyles(scene);

        // Hier vervang ik het huidige scherm door het workoutscherm.
        stage.setScene(scene);
    }

    // Dit laat het overzichtsscherm zien.
    private void showOverview() {
        OverviewScreen overviewScreen = new OverviewScreen(userId, this::showWorkout, this::showStart);

        Scene scene = overviewScreen.getScene();
        applyStyles(scene);

        // Hier vervang ik het huidige scherm door het overzichtsscherm.
        stage.setScene(scene);
    }
}