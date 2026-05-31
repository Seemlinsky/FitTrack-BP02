package nl.adainf.fittrack.screens;

/*
 * MealDialogs.java
 *
 * Dit bestand maakt de pop-up schermen voor maaltijden.
 *
 * Ik gebruik dit voor:
 * - maaltijd toevoegen
 * - maaltijd aanpassen
 *
 * In deze dialog gebruik ik JavaFX controls zoals:
 * - Dialog voor een pop-up scherm
 * - DatePicker om een datum te kiezen
 * - TextField om tijd, naam en calorieën in te vullen
 * - ComboBox om het soort maaltijd te kiezen
 * - Label voor tekst en foutmeldingen
 * - GridPane om alles netjes in rijen en kolommen te zetten
 *
 * De invoer uit TextField is eerst tekst.
 * Daarom zet ik calorieën om naar int met Integer.parseInt().
 * Tijd zet ik om naar LocalTime met LocalTime.parse().
 */

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import nl.adainf.fittrack.model.MealEntry;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class MealDialogs {

    // Deze methode opent een lege dialog om een nieuwe maaltijd toe te voegen.
    public static MealEntry showCreate(LocalDate date, int userId) {
        // Hier maak ik alvast een MealEntry object met standaardwaarden.
        MealEntry meal = new MealEntry(
                0,
                userId,
                date,
                LocalTime.of(12, 0),
                "Lunch",
                "",
                0
        );

        return showDialog("Add Meal", meal);
    }

    // Deze methode opent een dialog om een bestaande maaltijd aan te passen.
    public static MealEntry showEdit(MealEntry existing) {
        // Ik maak een kopie, zodat het originele object niet direct aangepast wordt.
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

    // Deze methode bouwt de dialog op. Deze wordt gebruikt voor toevoegen en aanpassen.
    private static MealEntry showDialog(String title, MealEntry meal) {
        // Dialog is een klein pop-up scherm.
        Dialog<MealEntry> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(MealDialogs.class.getResource("/styles.css")).toExternalForm()
        );

        // DatePicker: hiermee kiest de gebruiker de datum van de maaltijd.
        DatePicker dpDate = new DatePicker(meal.getMealDate());

        // TextField: tijd komt binnen als tekst in formaat HH:MM.
        TextField tfTime = new TextField(meal.getMealTime().toString());

        // ComboBox: hiermee kiest de gebruiker het soort maaltijd.
        ComboBox<String> cbType = new ComboBox<>();
        cbType.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");
        cbType.setValue(meal.getMealType());

        // TextField: hiermee vult de gebruiker de naam van de maaltijd in.
        TextField tfName = new TextField(meal.getMealName());
        tfName.setPromptText("Meal name (e.g. banana)");

        // TextField: calorieën komen binnen als tekst.
        TextField tfCalories = new TextField(String.valueOf(meal.getCalories()));
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

        grid.addRow(0, new Label("Date"), dpDate);
        grid.addRow(1, new Label("Time (HH:MM)"), tfTime);
        grid.addRow(2, new Label("Type"), cbType);
        grid.addRow(3, new Label("Name"), tfName);
        grid.addRow(4, new Label("Calories"), tfCalories);
        grid.add(lblCaloriesTip, 1, 5);
        grid.add(msg, 0, 6, 2, 1);

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
                if (tfName.getText().trim().isEmpty()) {
                    msg.setText("Name is required.");
                    event.consume();
                    return;
                }

                // Dataconversie: tekst uit TextField wordt omgezet naar int.
                int calories = Integer.parseInt(tfCalories.getText().trim());

                // Dataconversie: tekst wordt omgezet naar een tijd.
                LocalTime time = LocalTime.parse(tfTime.getText().trim());

                // Hier zet ik de ingevulde waarden terug in het MealEntry object.
                meal.setMealDate(dpDate.getValue());
                meal.setMealTime(time);
                meal.setMealType(cbType.getValue());
                meal.setMealName(tfName.getText().trim());
                meal.setCalories(calories);

            } catch (Exception ex) {
                msg.setText("Invalid input (time HH:MM, calories number).");
                event.consume();
            }
        });

        // Als de gebruiker op OK klikt, geeft de dialog het object terug.
        // Bij Cancel geeft de dialog null terug.
        dialog.setResultConverter(button -> button == ButtonType.OK ? meal : null);

        return dialog.showAndWait().orElse(null);
    }
}