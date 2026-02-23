package nl.adainf.fittrack;



/*
 * MainApp.java - JavaFX Application: maakt de stage + schakelt tussen schermen
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - vooral uitleg bij DB/CRUD (verbinden, query uitvoeren, ResultSet lezen)
 * - geen moeilijke termen zonder uitleg
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.adainf.fittrack.screens.OverviewScreen;
import nl.adainf.fittrack.screens.StartScreen;
import nl.adainf.fittrack.screens.WorkoutScreen;

import java.util.Objects;

public class MainApp extends Application {

    private Stage stage;
    private int userId;

    @Override
    // start() wordt door JavaFX aangeroepen.
// Hier zetten we de titel, scene, en wat er gebeurt bij navigatie.
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("FitTrack");
        showStart();
        stage.show();
    }

    private void applyStyles(Scene scene) {
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );
    }

    private void showStart() {
        StartScreen s = new StartScreen(id -> {
            this.userId = id;
            showWorkout();
        });

        Scene scene = s.getScene();
        applyStyles(scene);
        // Scene wisselen = ander scherm laten zien
        stage.setScene(scene);
    }

    private void showWorkout() {
        WorkoutScreen w = new WorkoutScreen(userId, this::showOverview);

        Scene scene = w.getScene();
        applyStyles(scene);
        // Scene wisselen = ander scherm laten zien
        stage.setScene(scene);
    }

    private void showOverview() {
        OverviewScreen o = new OverviewScreen(userId, this::showWorkout, this::showStart);

        Scene scene = o.getScene();
        applyStyles(scene);
        // Scene wisselen = ander scherm laten zien
        stage.setScene(scene);
    }
}