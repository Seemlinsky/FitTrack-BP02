package nl.adainf.fittrack.screens;

/*
 * WorkoutScreen.java
 *
 * Dit scherm gebruik ik om een workout/training toe te voegen.
 *
 * Op dit scherm kan de gebruiker:
 * - een datum kiezen
 * - een korte notitie invullen
 * - een activiteit kiezen
 * - minuten invullen
 * - calorieën invullen
 * - de workout opslaan
 * - naar het overzicht gaan
 *
 * In dit scherm gebruik ik JavaFX controls zoals:
 * - Label voor tekst
 * - DatePicker voor een datum
 * - TextField voor invoer
 * - ComboBox voor een keuze uit activiteiten
 * - Button voor acties
 * - VBox om alles onder elkaar te zetten
 *
 * De databasecode staat niet in dit scherm zelf.
 * Daarvoor gebruik ik DAO-klassen.
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nl.adainf.fittrack.dao.ActivityTypeDao;
import nl.adainf.fittrack.dao.WorkoutDao;
import nl.adainf.fittrack.dao.WorkoutEntryDao;
import nl.adainf.fittrack.model.ActivityType;
import nl.adainf.fittrack.model.Workout;
import nl.adainf.fittrack.model.WorkoutEntry;

import java.time.LocalDate;
import java.util.Objects;

public class WorkoutScreen {

    private final Scene scene;

    public WorkoutScreen(int userId, Runnable onNext) {
        WorkoutDao workoutDao = new WorkoutDao();
        WorkoutEntryDao entryDao = new WorkoutEntryDao();
        ActivityTypeDao activityTypeDao = new ActivityTypeDao();

        // Titel bovenaan het scherm.
        Label title = new Label("FitTrack");
        title.getStyleClass().add("app-title");

        // Kleine uitleg onder de titel.
        Label subtitle = new Label("Add workout");
        subtitle.getStyleClass().add("muted");

        // DatePicker: hiermee kiest de gebruiker de datum van de workout.
        DatePicker dpDate = new DatePicker(LocalDate.now());

        // TextField: hier kan de gebruiker een korte notitie invullen.
        TextField tfNote = new TextField();
        tfNote.setPromptText("Note (optional)");

        // ComboBox: hiermee kiest de gebruiker een activiteit uit de database.
        ComboBox<ActivityType> cbType = new ComboBox<>();
        cbType.setPromptText("Choose activity");
        cbType.getItems().setAll(activityTypeDao.getAll());

        // Dit zorgt ervoor dat de naam van de activiteit netjes zichtbaar is in de lijst.
        cbType.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ActivityType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });

        // Dit zorgt ervoor dat de gekozen activiteit netjes zichtbaar blijft in de ComboBox.
        cbType.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ActivityType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Choose activity" : item.getName());
            }
        });

        // TextField: minuten komen eerst binnen als tekst.
        TextField tfMinutes = new TextField();
        tfMinutes.setPromptText("Minutes (e.g. 30)");

        // TextField: calorieën komen eerst binnen als tekst.
        TextField tfCalories = new TextField();
        tfCalories.setPromptText("Calories (search online, e.g. \"banana calories\")");

        // Extra uitleg voor de gebruiker.
        Label infoCalories = new Label("Tip: search online for calories if you are not sure.");
        infoCalories.getStyleClass().add("muted");

        // Knop om de workout op te slaan.
        Button btnSave = new Button("Save workout + entry");
        btnSave.getStyleClass().add("btn-primary");

        // Knop om naar het overzicht te gaan.
        Button btnOverview = new Button("Go to overview");
        btnOverview.getStyleClass().add("btn-secondary");

        // Hier toon ik meldingen, bijvoorbeeld als invoer fout is.
        Label msg = new Label();
        msg.getStyleClass().add("muted");

        // Als de gebruiker op opslaan klikt, wordt deze code uitgevoerd.
        btnSave.setOnAction(e -> {
            try {
                if (cbType.getValue() == null) {
                    msg.setText("Pick an activity type.");
                    return;
                }

                // Dataconversie: tekst uit TextField wordt omgezet naar int.
                int minutes = Integer.parseInt(tfMinutes.getText().trim());
                int calories = Integer.parseInt(tfCalories.getText().trim());

                // OOP: hier maak ik een Workout object van de ingevulde gegevens.
                Workout workout = new Workout(
                        userId,
                        dpDate.getValue(),
                        tfNote.getText().trim()
                );

                // DAO: WorkoutDao slaat de workout op in de database.
                int workoutId = workoutDao.insert(workout);

                if (workoutId == -1) {
                    msg.setText("Workout insert failed.");
                    return;
                }

                // OOP: hier maak ik een WorkoutEntry object voor activiteit, minuten en calorieën.
                WorkoutEntry entry = new WorkoutEntry(
                        workoutId,
                        cbType.getValue().getId(),
                        minutes,
                        calories
                );

                // DAO: WorkoutEntryDao slaat de workoutregel op in de database.
                int entryId = entryDao.insert(entry);

                if (entryId != -1) {
                    msg.setText("Saved! Go to Overview for more details.");
                } else {
                    msg.setText("Entry insert failed.");
                }

            } catch (Exception ex) {
                msg.setText("Fill in numbers correctly.");
            }
        });

        // Deze knop gaat naar het overzichtsscherm.
        btnOverview.setOnAction(e -> onNext.run());

        // VBox zet alle onderdelen onder elkaar.
        VBox root = new VBox(12,
                title,
                subtitle,
                new Label("Date:"), dpDate,
                new Label("Note:"), tfNote,
                new Label("Activity:"), cbType,
                tfMinutes,
                tfCalories,
                infoCalories,
                btnSave,
                btnOverview,
                msg
        );

        root.setPadding(new Insets(25));
        root.setAlignment(Pos.TOP_CENTER);
        root.setFillWidth(true);

        // Scene is de inhoud van dit scherm.
        scene = new Scene(root, 600, 700);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );
    }

    public Scene getScene() {
        return scene;
    }
}