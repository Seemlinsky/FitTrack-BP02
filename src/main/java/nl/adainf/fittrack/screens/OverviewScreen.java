package nl.adainf.fittrack.screens;



/*
 * OverviewScreen.java - Scherm/GUI: overzicht: navigatie naar eten/training
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
import javafx.scene.layout.*;
import nl.adainf.fittrack.dao.*;
import nl.adainf.fittrack.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
// JavaFX scherm: bouwt de UI op en koppelt knoppen aan acties.
public class OverviewScreen {

    private final Scene scene;

    private final int userId;

    private final DatePicker dpDate = new DatePicker(LocalDate.now());

    private final Label lblCaloriesBurned = new Label("-");
    private final Label lblCaloriesEaten = new Label("-");
    private final Label lblMinutesActive = new Label("-");

    private final ListView<MealEntry> lvMeals = new ListView<>();
    private final ListView<WorkoutEntry> lvTrainings = new ListView<>();

    private final MealEntryDao mealDao = new MealEntryDao();
    private final WorkoutDao workoutDao = new WorkoutDao();
    private final WorkoutEntryDao workoutEntryDao = new WorkoutEntryDao();
    private final ActivityTypeDao activityTypeDao = new ActivityTypeDao();

    public OverviewScreen(int userId, Runnable onBack, Runnable onLogout) {
        this.userId = userId;

        Label title = new Label("FitTrack");
        title.getStyleClass().add("app-title");

        Label sub = new Label("Daily overview");
        sub.getStyleClass().add("subtitle");

        HBox dateRow = new HBox(10, new Label("Select date:"), dpDate);
        dateRow.setAlignment(Pos.CENTER);

        Button btnBack = new Button("Back");
        Button btnLogout = new Button("Logout");
        btnLogout.getStyleClass().add("btn-secondary");
        // Klik op knop -> voer actie uit

        btnLogout.setOnAction(e -> onLogout.run());

        btnBack.getStyleClass().add("btn-secondary");
        // Klik op knop -> voer actie uit

        btnBack.setOnAction(e -> onBack.run());

        Button btnAddTraining = new Button("+ Add Training");
        btnAddTraining.getStyleClass().add("btn-primary");
        // Klik op knop -> voer actie uit

        btnAddTraining.setOnAction(e -> addTraining());

        Button btnAddMeal = new Button("+ Add Meal");
        btnAddMeal.getStyleClass().add("btn-secondary");
        // Klik op knop -> voer actie uit

        btnAddMeal.setOnAction(e -> addMeal());

        HBox actions = new HBox(10, btnAddTraining, btnAddMeal, btnLogout, btnBack);
        actions.setAlignment(Pos.CENTER);

        HBox summary = new HBox(12,
                summaryCard("Calories Burned", lblCaloriesBurned),
                summaryCard("Calories Eaten", lblCaloriesEaten),
                summaryCard("Minutes Active", lblMinutesActive)
        );
        summary.setAlignment(Pos.CENTER);

        // Meals section
        Label mealsTitle = new Label("Meals");
        mealsTitle.getStyleClass().add("h2");

        Button btnEditMeal = new Button("Edit");
        btnEditMeal.getStyleClass().add("btn-secondary");
        // Klik op knop -> voer actie uit

        btnEditMeal.setOnAction(e -> editMeal());

        Button btnDeleteMeal = new Button("Delete");
        btnDeleteMeal.getStyleClass().add("btn-danger");
        // Klik op knop -> voer actie uit

        btnDeleteMeal.setOnAction(e -> deleteMeal());

        HBox mealButtons = new HBox(10, btnEditMeal, btnDeleteMeal);
        mealButtons.setAlignment(Pos.CENTER_LEFT);

        VBox mealsBox = new VBox(8, mealsTitle, lvMeals, mealButtons);
        mealsBox.setPadding(new Insets(10));
        mealsBox.getStyleClass().add("card");

        // Trainings section
        Label trTitle = new Label("Trainings");
        trTitle.getStyleClass().add("h2");

        Button btnEditTr = new Button("Edit");
        btnEditTr.getStyleClass().add("btn-secondary");
        // Klik op knop -> voer actie uit

        btnEditTr.setOnAction(e -> editTraining());

        Button btnDeleteTr = new Button("Delete");
        btnDeleteTr.getStyleClass().add("btn-danger");
        // Klik op knop -> voer actie uit

        btnDeleteTr.setOnAction(e -> deleteTraining());

        HBox trButtons = new HBox(10, btnEditTr, btnDeleteTr);
        trButtons.setAlignment(Pos.CENTER_LEFT);

        VBox trBox = new VBox(8, trTitle, lvTrainings, trButtons);
        trBox.setPadding(new Insets(10));
        trBox.getStyleClass().add("card");

        HBox lists = new HBox(12, mealsBox, trBox);
        HBox.setHgrow(mealsBox, Priority.ALWAYS);
        HBox.setHgrow(trBox, Priority.ALWAYS);

        VBox root = new VBox(14, title, sub, dateRow, actions, summary, lists);
        root.setPadding(new Insets(18));
        root.setAlignment(Pos.TOP_CENTER);

        scene = new Scene(root, 900, 650);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        dpDate.valueProperty().addListener((obs, o, n) -> refresh());
        refresh();
    }

    public Scene getScene() {
        return scene;
    }

    private VBox summaryCard(String title, Label value) {
        Label t = new Label(title);
        t.getStyleClass().add("muted");
        value.getStyleClass().add("h2");
        VBox box = new VBox(6, t, value);
        box.getStyleClass().add("card");
        box.setPadding(new Insets(12));
        box.setMinWidth(180);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private void refresh() {
        LocalDate date = dpDate.getValue();

        // summary
        int burned = workoutEntryDao.getTotalCaloriesForDay(userId, date);
        int minutes = workoutEntryDao.getTotalMinutesForDay(userId, date);
        int eaten = mealDao.getTotalCaloriesForDay(userId, date);

        lblCaloriesBurned.setText(burned + " kcal");
        lblCaloriesEaten.setText(eaten + " kcal");
        lblMinutesActive.setText(minutes + " min");

        // meals
        lvMeals.getItems().setAll(mealDao.getByUserAndDate(userId, date));

        // trainings
        lvTrainings.getItems().setAll(workoutEntryDao.getEntriesForDay(userId, date));
    }

    private void addMeal() {
        MealEntry created = MealDialogs.showCreate(dpDate.getValue(), userId);
        if (created == null) return;

        int id = mealDao.insert(created);
        if (id == -1) {
            alert("Meal", "Insert failed (check database connection).");
            return;
        }
        refresh();
    }

    private void editMeal() {
        MealEntry selected = lvMeals.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        MealEntry updated = MealDialogs.showEdit(selected);
        if (updated == null) return;

        if (!mealDao.update(updated)) {
            alert("Meal", "Update failed.");
        }
        refresh();
    }

    private void deleteMeal() {
        MealEntry selected = lvMeals.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (!confirm("Delete meal", "Delete selected meal?")) return;

        if (!mealDao.delete(selected.getId(), userId)) {
            alert("Meal", "Delete failed.");
        }
        refresh();
    }

    private void addTraining() {
        // ensure workout exists for selected date (1 workout per day per user)
        LocalDate date = dpDate.getValue();
        int workoutId = workoutDao.findOrCreateWorkout(userId, date);

        WorkoutEntry created = TrainingDialogs.showCreate(workoutId, activityTypeDao.getAll());
        if (created == null) return;

        int id = workoutEntryDao.insert(created);
        if (id == -1) {
            alert("Training", "Insert failed.");
            return;
        }
        refresh();
    }

    private void editTraining() {
        WorkoutEntry selected = lvTrainings.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        WorkoutEntry updated = TrainingDialogs.showEdit(selected, activityTypeDao.getAll());
        if (updated == null) return;

        if (!workoutEntryDao.update(updated)) {
            alert("Training", "Update failed.");
        }
        refresh();
    }

    private void deleteTraining() {
        WorkoutEntry selected = lvTrainings.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (!confirm("Delete training", "Delete selected training?")) return;

        if (!workoutEntryDao.delete(selected.getId(), selected.getWorkoutId())) {
            alert("Training", "Delete failed.");
        }
        refresh();
    }

    private void alert(String title, String msg) {
        Alert a = // Alert = pop-up melding voor de gebruiker
        new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private boolean confirm(String title, String msg) {
        Alert a = // Alert = pop-up melding voor de gebruiker
        new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        return a.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }
}