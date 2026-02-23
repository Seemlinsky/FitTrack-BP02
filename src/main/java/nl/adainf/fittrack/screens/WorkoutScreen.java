package nl.adainf.fittrack.screens;



/*
 * WorkoutScreen.java - Scherm/GUI: training scherm: workouts tonen + entries beheren
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - vooral uitleg bij DB/CRUD (verbinden, query uitvoeren, ResultSet lezen)
 * - geen moeilijke termen zonder uitleg
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import nl.adainf.fittrack.dao.ActivityTypeDao;
import nl.adainf.fittrack.dao.WorkoutDao;
import nl.adainf.fittrack.dao.WorkoutEntryDao;
import nl.adainf.fittrack.model.ActivityType;
import nl.adainf.fittrack.model.Workout;
import nl.adainf.fittrack.model.WorkoutEntry;

import java.time.LocalDate;
import java.util.Objects;
// JavaFX scherm: bouwt de UI op en koppelt knoppen aan acties.
public class WorkoutScreen {

    private final Scene scene;

    public WorkoutScreen(int userId, Runnable onNext) {

        // TITLE
        Label title = new Label("FitTrack");
        title.getStyleClass().add("app-title");

        Label subtitle = new Label("Add workout");
        subtitle.getStyleClass().add("muted");

        // DATE
        DatePicker dpDate = new DatePicker(LocalDate.now());

        // NOTE
        TextField tfNote = new TextField();
        tfNote.setPromptText("Note (optional)");

        // ACTIVITY
        ComboBox<ActivityType> cbType = new ComboBox<>();
        cbType.setPromptText("Choose activity");
        cbType.getItems().setAll(new ActivityTypeDao().getAll());

        cbType.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ActivityType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });

        cbType.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ActivityType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Choose activity" : item.getName());
            }
        });

        // MINUTES
        TextField tfMinutes = new TextField();
        tfMinutes.setPromptText("Minutes (e.g. 30)");

        // CALORIES
        TextField tfCalories = new TextField();
        tfCalories.setPromptText("Calories (search online, e.g. \"banana calories\")");

        Label infoCalories = new Label("Tip: search online for calories if you are not sure.");
        infoCalories.getStyleClass().add("muted");

        // BUTTONS
        Button btnSave = new Button("Save workout + entry");
        btnSave.getStyleClass().add("btn-primary");

        Button btnOverview = new Button("Go to overview");
        btnOverview.getStyleClass().add("btn-secondary");

        Label msg = new Label();
        msg.getStyleClass().add("muted");

        WorkoutDao workoutDao = new WorkoutDao();
        WorkoutEntryDao entryDao = new WorkoutEntryDao();

        // Klik op knop -> voer actie uit


        btnSave.setOnAction(e -> {
            try {

                if (cbType.getValue() == null) {
                    msg.setText("Pick an activity type.");
                    return;
                }

                int minutes = Integer.parseInt(tfMinutes.getText().trim());
                int calories = Integer.parseInt(tfCalories.getText().trim());

                // Insert workout
                Workout w = new Workout(userId, dpDate.getValue(), tfNote.getText().trim());
                int workoutId = workoutDao.insert(w);

                if (workoutId == -1) {
                    msg.setText("Workout insert failed.");
                    return;
                }

                // Insert workout entry
                WorkoutEntry entry = new WorkoutEntry(
                        workoutId,
                        cbType.getValue().getId(),
                        minutes,
                        calories
                );

                int entryId = entryDao.insert(entry);

                msg.setText(
                        entryId != -1
                                ? "Saved! Go to Overview for more details."
                                : "Entry insert failed."
                );

            } catch (Exception ex) {
                msg.setText("Fill in numbers correctly.");
            }
        });

        // Klik op knop -> voer actie uit


        btnOverview.setOnAction(e -> onNext.run());

        // LAYOUT
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

        scene = new Scene(root, 600, 700);
        scene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/styles.css")
                ).toExternalForm()
        );
    }

    public Scene getScene() {
        return scene;
    }
}
