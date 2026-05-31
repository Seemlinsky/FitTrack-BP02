package nl.adainf.fittrack.screens;

/*
 * TrainingDialogs.java
 *
 * Dit bestand maakt de pop-up schermen voor trainingen.
 *
 * Ik gebruik dit voor:
 * - training toevoegen
 * - training aanpassen
 *
 * In deze dialog gebruik ik JavaFX controls zoals:
 * - Dialog voor een pop-up scherm
 * - ComboBox om een activiteit te kiezen
 * - TextField om minuten en calorieën in te vullen
 * - Label voor tekst en foutmeldingen
 * - GridPane om alles netjes in rijen en kolommen te zetten
 *
 * De invoer uit TextField is eerst tekst.
 * Daarom zet ik minuten en calorieën om naar int met Integer.parseInt().
 */

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import nl.adainf.fittrack.model.ActivityType;
import nl.adainf.fittrack.model.WorkoutEntry;

import java.util.List;
import java.util.Objects;

public class TrainingDialogs {

    // Deze methode opent een lege dialog om een nieuwe training toe te voegen.
    public static WorkoutEntry showCreate(int workoutId, List<ActivityType> types) {
        ActivityType first = types.isEmpty() ? new ActivityType(0, "Gym") : types.get(0);

        // Hier maak ik alvast een WorkoutEntry object met standaardwaarden.
        WorkoutEntry entry = new WorkoutEntry(
                0,
                workoutId,
                first.getId(),
                first.getName(),
                30,
                200
        );

        return showDialog("Add Training", entry, types);
    }

    // Deze methode opent een dialog om een bestaande training aan te passen.
    public static WorkoutEntry showEdit(WorkoutEntry existing, List<ActivityType> types) {
        // Ik maak een kopie, zodat het originele object niet direct aangepast wordt.
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

    // Deze methode bouwt de dialog op. Deze wordt gebruikt voor toevoegen en aanpassen.
    private static WorkoutEntry showDialog(String title, WorkoutEntry entry, List<ActivityType> types) {
        // Dialog is een klein pop-up scherm.
        Dialog<WorkoutEntry> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(TrainingDialogs.class.getResource("/styles.css")).toExternalForm()
        );

        // ComboBox: hiermee kiest de gebruiker het soort activiteit.
        ComboBox<ActivityType> cbType = new ComboBox<>();
        cbType.getItems().setAll(types);

        // Dit zorgt ervoor dat de activiteitnaam netjes zichtbaar is in de lijst.
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

        // Hier zoek ik welke activiteit al bij deze training hoort.
        ActivityType selected = null;
        for (ActivityType type : types) {
            if (type.getId() == entry.getActivityTypeId()) {
                selected = type;
                break;
            }
        }
        cbType.setValue(selected);

        // TextField: minuten komen binnen als tekst.
        TextField tfMinutes = new TextField(String.valueOf(entry.getMinutes()));
        tfMinutes.setPromptText("Minutes (e.g. 30)");

        // TextField: calorieën komen binnen als tekst.
        TextField tfCalories = new TextField(String.valueOf(entry.getCalories()));
        tfCalories.setPromptText("Calories (search online, e.g. \"banana calories\")");

        // Korte tip voor de gebruiker.
        Label lblCaloriesTip = new Label("Tip: search online for calories if you are not sure.");
        lblCaloriesTip.getStyleClass().add("muted");

        // Label voor foutmeldingen.
        Label msg = new Label();
        msg.getStyleClass().add("muted");

        // GridPane zet labels en invoervelden netjes in rijen en kolommen.
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

        // OK-knop uit de dialog.
        Button okBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.getStyleClass().add("btn-primary");

        // Cancel-knop uit de dialog.
        Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelBtn.getStyleClass().add("btn-secondary");

        // addEventFilter controleert de invoer voordat de dialog sluit.
        okBtn.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            try {
                if (cbType.getValue() == null) {
                    msg.setText("Pick an activity.");
                    event.consume();
                    return;
                }

                // Dataconversie: tekst uit TextField wordt omgezet naar int.
                int minutes = Integer.parseInt(tfMinutes.getText().trim());
                int calories = Integer.parseInt(tfCalories.getText().trim());

                // Hier zet ik de ingevulde waarden terug in het object.
                entry.setActivityTypeId(cbType.getValue().getId());
                entry.setActivityName(cbType.getValue().getName());
                entry.setMinutes(minutes);
                entry.setCalories(calories);

            } catch (Exception ex) {
                msg.setText("Invalid input (numbers).");
                event.consume();
            }
        });

        // Als de gebruiker op OK klikt, geeft de dialog het object terug.
        // Bij Cancel geeft de dialog null terug.
        dialog.setResultConverter(button -> button == ButtonType.OK ? entry : null);

        return dialog.showAndWait().orElse(null);
    }
}