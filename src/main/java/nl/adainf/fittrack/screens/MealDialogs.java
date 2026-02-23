package nl.adainf.fittrack.screens;



/*
 * MealDialogs.java - Scherm/GUI: dialogs voor eten (toevoegen/bewerken)
 *
 * AD lesstof-stijl:
 * - korte uitleg in simpele woorden
 * - vooral uitleg bij DB/CRUD (verbinden, query uitvoeren, ResultSet lezen)
 * - geen moeilijke termen zonder uitleg
 */

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import nl.adainf.fittrack.model.MealEntry;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
// JavaFX scherm: bouwt de UI op en koppelt knoppen aan acties.
public class MealDialogs {

    public static MealEntry showCreate(LocalDate date, int userId) {
        MealEntry m = new MealEntry(0, userId, date, LocalTime.of(12, 0), "Lunch", "", 0);
        return showDialog("Add Meal", m);
    }

    public static MealEntry showEdit(MealEntry existing) {
        MealEntry copy = new MealEntry(
                existing.getId(),
                existing.getUserId(),
                existing.getMealDate(),
                existing.getMealTime(),
                existing.getMealType(),
                existing.getMealName(),
                existing.getCalories()
        );
        return showDialog("Edit Meal", copy);
    }

    private static MealEntry showDialog(String title, MealEntry m) {
        Dialog<MealEntry> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(MealDialogs.class.getResource("/styles.css")).toExternalForm()
        );

        DatePicker dpDate = new DatePicker(m.getMealDate());
        TextField tfTime = new TextField(m.getMealTime().toString()); // HH:MM

        ComboBox<String> cbType = new ComboBox<>();
        cbType.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");
        cbType.setValue(m.getMealType());

        TextField tfName = new TextField(m.getMealName());
        tfName.setPromptText("Meal name (e.g. banana)");

        TextField tfCalories = new TextField(String.valueOf(m.getCalories()));
        tfCalories.setPromptText("Calories (search online, e.g. \"banana calories\")");

        Label lblCaloriesTip = new Label("Tip: search online for calories if you are not sure.");
        lblCaloriesTip.getStyleClass().add("muted");

        Label msg = new Label();
        msg.getStyleClass().add("muted");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.addRow(0, new Label("Date"), dpDate);
        grid.addRow(1, new Label("Time (HH:MM)"), tfTime);
        grid.addRow(2, new Label("Type"), cbType);
        grid.addRow(3, new Label("Name"), tfName);
        grid.addRow(4, new Label("Calories"), tfCalories);
        grid.add(lblCaloriesTip, 1, 5);
        grid.add(msg, 0, 6, 2, 1);

        dialog.getDialogPane().setContent(grid);

        Button okBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.getStyleClass().add("btn-primary");

        Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelBtn.getStyleClass().add("btn-secondary");

        okBtn.addEventFilter(javafx.event.ActionEvent.ACTION, e -> {
            try {
                if (tfName.getText().trim().isEmpty()) {
                    msg.setText("Name is required.");
                    e.consume();
                    return;
                }

                int cal = Integer.parseInt(tfCalories.getText().trim());
                LocalTime time = LocalTime.parse(tfTime.getText().trim());

                m.setMealDate(dpDate.getValue());
                m.setMealTime(time);
                m.setMealType(cbType.getValue());
                m.setMealName(tfName.getText().trim());
                m.setCalories(cal);

            } catch (Exception ex) {
                msg.setText("Invalid input (time HH:MM, calories number).");
                e.consume();
            }
        });

        dialog.setResultConverter(bt -> bt == ButtonType.OK ? m : null);
        return dialog.showAndWait().orElse(null);
    }
}
