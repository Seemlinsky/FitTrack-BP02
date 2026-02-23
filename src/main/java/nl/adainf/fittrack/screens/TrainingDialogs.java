package nl.adainf.fittrack.screens;



/*
 * TrainingDialogs.java - Scherm/GUI: dialogs voor training (toevoegen/bewerken)
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - vooral uitleg bij DB/CRUD (verbinden, query uitvoeren, ResultSet lezen)
 * - geen moeilijke termen zonder uitleg
 */

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import nl.adainf.fittrack.model.ActivityType;
import nl.adainf.fittrack.model.WorkoutEntry;

import java.util.List;
import java.util.Objects;
// JavaFX scherm: bouwt de UI op en koppelt knoppen aan acties.
public class TrainingDialogs {

    public static WorkoutEntry showCreate(int workoutId, List<ActivityType> types) {
        ActivityType first = types.isEmpty() ? new ActivityType(0, "Gym") : types.get(0);
        WorkoutEntry e = new WorkoutEntry(0, workoutId, first.getId(), first.getName(), 30, 200);
        return showDialog("Add Training", e, types);
    }

    public static WorkoutEntry showEdit(WorkoutEntry existing, List<ActivityType> types) {
        WorkoutEntry copy = new WorkoutEntry(
                existing.getId(),
                existing.getWorkoutId(),
                existing.getActivityTypeId(),
                existing.getActivityName(),
                existing.getMinutes(),
                existing.getCalories()
        );
        return showDialog("Edit Training", copy, types);
    }

    private static WorkoutEntry showDialog(String title, WorkoutEntry e, List<ActivityType> types) {
        Dialog<WorkoutEntry> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(TrainingDialogs.class.getResource("/styles.css")).toExternalForm()
        );

        ComboBox<ActivityType> cbType = new ComboBox<>();
        cbType.getItems().setAll(types);

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

        ActivityType selected = null;
        for (ActivityType t : types) {
            if (t.getId() == e.getActivityTypeId()) {
                selected = t;
                break;
            }
        }
        cbType.setValue(selected);

        TextField tfMinutes = new TextField(String.valueOf(e.getMinutes()));
        tfMinutes.setPromptText("Minutes (e.g. 30)");

        TextField tfCalories = new TextField(String.valueOf(e.getCalories()));
        tfCalories.setPromptText("Calories (search online, e.g. \"banana calories\")");

        Label lblCaloriesTip = new Label("Tip: search online for calories if you are not sure.");
        lblCaloriesTip.getStyleClass().add("muted");

        Label msg = new Label();
        msg.getStyleClass().add("muted");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.addRow(0, new Label("Activity"), cbType);
        grid.addRow(1, new Label("Minutes"), tfMinutes);
        grid.addRow(2, new Label("Calories"), tfCalories);
        grid.add(lblCaloriesTip, 1, 3);
        grid.add(msg, 0, 4, 2, 1);

        dialog.getDialogPane().setContent(grid);

        Button okBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.getStyleClass().add("btn-primary");

        Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelBtn.getStyleClass().add("btn-secondary");

        okBtn.addEventFilter(javafx.event.ActionEvent.ACTION, ev -> {
            try {
                if (cbType.getValue() == null) {
                    msg.setText("Pick an activity.");
                    ev.consume();
                    return;
                }

                int minutes = Integer.parseInt(tfMinutes.getText().trim());
                int calories = Integer.parseInt(tfCalories.getText().trim());

                e.setActivityTypeId(cbType.getValue().getId());
                e.setActivityName(cbType.getValue().getName());
                e.setMinutes(minutes);
                e.setCalories(calories);

            } catch (Exception ex) {
                msg.setText("Invalid input (numbers).");
                ev.consume();
            }
        });

        dialog.setResultConverter(bt -> bt == ButtonType.OK ? e : null);
        return dialog.showAndWait().orElse(null);
    }
}