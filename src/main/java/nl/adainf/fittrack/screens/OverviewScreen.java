package nl.adainf.fittrack.screens;

/*
 * OverviewScreen.java
 *
 * Dit is het overzichtsscherm van de FitTrack app.
 *
 * Op dit scherm kan de gebruiker:
 * - een datum kiezen
 * - zien hoeveel calorieën hij heeft verbrand
 * - zien hoeveel calorieën hij heeft gegeten
 * - zien hoeveel minuten hij actief is geweest
 * - maaltijden bekijken, toevoegen, aanpassen en verwijderen
 * - trainingen bekijken, toevoegen, aanpassen en verwijderen
 *
 * In dit scherm gebruik ik JavaFX controls zoals:
 * - Label voor tekst en totalen
 * - DatePicker om een datum te kiezen
 * - Button voor acties
 * - ListView om maaltijden en trainingen te tonen
 * - HBox om onderdelen naast elkaar te zetten
 * - VBox om onderdelen onder elkaar te zetten
 *
 * De databasecode staat niet direct in dit scherm.
 * Daarvoor gebruik ik DAO-klassen.
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import nl.adainf.fittrack.dao.ActivityTypeDao;
import nl.adainf.fittrack.dao.MealEntryDao;
import nl.adainf.fittrack.dao.WorkoutDao;
import nl.adainf.fittrack.dao.WorkoutEntryDao;
import nl.adainf.fittrack.model.MealEntry;
import nl.adainf.fittrack.model.WorkoutEntry;

import java.time.LocalDate;

public class OverviewScreen {

    private final Scene scene;

    private final int userId;

    // DatePicker: hiermee kiest de gebruiker welke dag hij wil bekijken.
    private final DatePicker dpDate = new DatePicker(LocalDate.now());

    // Labels: deze tonen de totalen van de gekozen dag.
    private final Label lblCaloriesBurned = new Label("-");
    private final Label lblCaloriesEaten = new Label("-");
    private final Label lblMinutesActive = new Label("-");

    // ListView: deze lijsten tonen de maaltijden en trainingen.
    private final ListView<MealEntry> lvMeals = new ListView<>();
    private final ListView<WorkoutEntry> lvTrainings = new ListView<>();

    // DAO-klassen: hiermee praat dit scherm met de database.
    private final MealEntryDao mealDao = new MealEntryDao();
    private final WorkoutDao workoutDao = new WorkoutDao();
    private final WorkoutEntryDao workoutEntryDao = new WorkoutEntryDao();
    private final ActivityTypeDao activityTypeDao = new ActivityTypeDao();

    public OverviewScreen(int userId, Runnable onBack, Runnable onLogout) {
        this.userId = userId;

        // Titel bovenaan het scherm.
        Label title = new Label("FitTrack");
        title.getStyleClass().add("app-title");

        // Kleine tekst onder de titel.
        Label sub = new Label("Daily overview");
        sub.getStyleClass().add("subtitle");

        // HBox zet de datumtekst en DatePicker naast elkaar.
        HBox dateRow = new HBox(10, new Label("Select date:"), dpDate);
        dateRow.setAlignment(Pos.CENTER);

        // Knop om terug te gaan.
        Button btnBack = new Button("Back");
        btnBack.getStyleClass().add("btn-secondary");

        // Knop om uit te loggen.
        Button btnLogout = new Button("Logout");
        btnLogout.getStyleClass().add("btn-secondary");

        // Als de gebruiker op logout klikt, gaat hij terug naar het startscherm.
        btnLogout.setOnAction(e -> onLogout.run());

        // Als de gebruiker op back klikt, gaat hij terug naar het vorige scherm.
        btnBack.setOnAction(e -> onBack.run());

        // Knop om een training toe te voegen.
        Button btnAddTraining = new Button("+ Add Training");
        btnAddTraining.getStyleClass().add("btn-primary");

        // Deze knop opent een dialog voor een nieuwe training.
        btnAddTraining.setOnAction(e -> addTraining());

        // Knop om een maaltijd toe te voegen.
        Button btnAddMeal = new Button("+ Add Meal");
        btnAddMeal.getStyleClass().add("btn-secondary");

        // Deze knop opent een dialog voor een nieuwe maaltijd.
        btnAddMeal.setOnAction(e -> addMeal());

        // HBox zet de actieknoppen naast elkaar.
        HBox actions = new HBox(10, btnAddTraining, btnAddMeal, btnLogout, btnBack);
        actions.setAlignment(Pos.CENTER);

        // Hier maak ik drie kleine kaartjes voor de totalen.
        HBox summary = new HBox(12,
                summaryCard("Calories Burned", lblCaloriesBurned),
                summaryCard("Calories Eaten", lblCaloriesEaten),
                summaryCard("Minutes Active", lblMinutesActive)
        );
        summary.setAlignment(Pos.CENTER);

        // Maaltijden gedeelte.
        Label mealsTitle = new Label("Meals");
        mealsTitle.getStyleClass().add("h2");

        Button btnEditMeal = new Button("Edit");
        btnEditMeal.getStyleClass().add("btn-secondary");

        // Deze knop past de geselecteerde maaltijd aan.
        btnEditMeal.setOnAction(e -> editMeal());

        Button btnDeleteMeal = new Button("Delete");
        btnDeleteMeal.getStyleClass().add("btn-danger");

        // Deze knop verwijdert de geselecteerde maaltijd.
        btnDeleteMeal.setOnAction(e -> deleteMeal());

        // Knoppen voor maaltijden naast elkaar.
        HBox mealButtons = new HBox(10, btnEditMeal, btnDeleteMeal);
        mealButtons.setAlignment(Pos.CENTER_LEFT);

        // VBox zet titel, lijst en knoppen van maaltijden onder elkaar.
        VBox mealsBox = new VBox(8, mealsTitle, lvMeals, mealButtons);
        mealsBox.setPadding(new Insets(10));
        mealsBox.getStyleClass().add("card");

        // Trainingen gedeelte.
        Label trTitle = new Label("Trainings");
        trTitle.getStyleClass().add("h2");

        Button btnEditTr = new Button("Edit");
        btnEditTr.getStyleClass().add("btn-secondary");

        // Deze knop past de geselecteerde training aan.
        btnEditTr.setOnAction(e -> editTraining());

        Button btnDeleteTr = new Button("Delete");
        btnDeleteTr.getStyleClass().add("btn-danger");

        // Deze knop verwijdert de geselecteerde training.
        btnDeleteTr.setOnAction(e -> deleteTraining());

        // Knoppen voor trainingen naast elkaar.
        HBox trButtons = new HBox(10, btnEditTr, btnDeleteTr);
        trButtons.setAlignment(Pos.CENTER_LEFT);

        // VBox zet titel, lijst en knoppen van trainingen onder elkaar.
        VBox trBox = new VBox(8, trTitle, lvTrainings, trButtons);
        trBox.setPadding(new Insets(10));
        trBox.getStyleClass().add("card");

        // HBox zet maaltijden en trainingen naast elkaar.
        HBox lists = new HBox(12, mealsBox, trBox);
        HBox.setHgrow(mealsBox, Priority.ALWAYS);
        HBox.setHgrow(trBox, Priority.ALWAYS);

        // VBox is de hoofd-layout van dit scherm.
        VBox root = new VBox(14, title, sub, dateRow, actions, summary, lists);
        root.setPadding(new Insets(18));
        root.setAlignment(Pos.TOP_CENTER);

        // Scene is de inhoud van dit scherm.
        scene = new Scene(root, 900, 650);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Als de datum verandert, laad ik opnieuw de data voor die dag.
        dpDate.valueProperty().addListener((obs, oldDate, newDate) -> refresh());

        // Bij het openen van het scherm haal ik meteen de data op.
        refresh();
    }

    public Scene getScene() {
        return scene;
    }

    // Dit maakt een klein kaartje voor een totaalwaarde.
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

    // Refresh haalt opnieuw alle data op voor de gekozen datum.
    private void refresh() {
        LocalDate date = dpDate.getValue();

        // SELECT via DAO: totalen ophalen uit de database.
        int burned = workoutEntryDao.getTotalCaloriesForDay(userId, date);
        int minutes = workoutEntryDao.getTotalMinutesForDay(userId, date);
        int eaten = mealDao.getTotalCaloriesForDay(userId, date);

        lblCaloriesBurned.setText(burned + " kcal");
        lblCaloriesEaten.setText(eaten + " kcal");
        lblMinutesActive.setText(minutes + " min");

        // SELECT via DAO: maaltijden ophalen voor deze gebruiker en datum.
        lvMeals.getItems().setAll(mealDao.getByUserAndDate(userId, date));

        // SELECT via DAO: trainingen ophalen voor deze gebruiker en datum.
        lvTrainings.getItems().setAll(workoutEntryDao.getEntriesForDay(userId, date));
    }

    // Maaltijd toevoegen: dialog openen en daarna opslaan via DAO.
    private void addMeal() {
        MealEntry created = MealDialogs.showCreate(dpDate.getValue(), userId);

        if (created == null) {
            return;
        }

        // INSERT via DAO: nieuwe maaltijd opslaan.
        int id = mealDao.insert(created);

        if (id == -1) {
            alert("Meal", "Insert failed (check database connection).");
            return;
        }

        refresh();
    }

    // Maaltijd aanpassen: geselecteerde maaltijd bewerken en update uitvoeren.
    private void editMeal() {
        MealEntry selected = lvMeals.getSelectionModel().getSelectedItem();

        if (selected == null) {
            return;
        }

        MealEntry updated = MealDialogs.showEdit(selected);

        if (updated == null) {
            return;
        }

        // UPDATE via DAO: bestaande maaltijd aanpassen.
        if (!mealDao.update(updated)) {
            alert("Meal", "Update failed.");
        }

        refresh();
    }

    // Maaltijd verwijderen: geselecteerde maaltijd verwijderen via DAO.
    private void deleteMeal() {
        MealEntry selected = lvMeals.getSelectionModel().getSelectedItem();

        if (selected == null) {
            return;
        }

        if (!confirm("Delete meal", "Delete selected meal?")) {
            return;
        }

        // DELETE via DAO: maaltijd verwijderen uit de database.
        if (!mealDao.delete(selected.getId(), userId)) {
            alert("Meal", "Delete failed.");
        }

        refresh();
    }

    // Training toevoegen: workout zoeken/maken, dialog openen en opslaan via DAO.
    private void addTraining() {
        LocalDate date = dpDate.getValue();

        // Hier zorg ik dat er een workout bestaat voor deze gebruiker en datum.
        int workoutId = workoutDao.findOrCreateWorkout(userId, date);

        WorkoutEntry created = TrainingDialogs.showCreate(workoutId, activityTypeDao.getAll());

        if (created == null) {
            return;
        }

        // INSERT via DAO: nieuwe training opslaan.
        int id = workoutEntryDao.insert(created);

        if (id == -1) {
            alert("Training", "Insert failed.");
            return;
        }

        refresh();
    }

    // Training aanpassen: geselecteerde training bewerken en update uitvoeren.
    private void editTraining() {
        WorkoutEntry selected = lvTrainings.getSelectionModel().getSelectedItem();

        if (selected == null) {
            return;
        }

        WorkoutEntry updated = TrainingDialogs.showEdit(selected, activityTypeDao.getAll());

        if (updated == null) {
            return;
        }

        // UPDATE via DAO: bestaande training aanpassen.
        if (!workoutEntryDao.update(updated)) {
            alert("Training", "Update failed.");
        }

        refresh();
    }

    // Training verwijderen: geselecteerde training verwijderen via DAO.
    private void deleteTraining() {
        WorkoutEntry selected = lvTrainings.getSelectionModel().getSelectedItem();

        if (selected == null) {
            return;
        }

        if (!confirm("Delete training", "Delete selected training?")) {
            return;
        }

        // DELETE via DAO: training verwijderen uit de database.
        if (!workoutEntryDao.delete(selected.getId(), selected.getWorkoutId())) {
            alert("Training", "Delete failed.");
        }

        refresh();
    }

    // Alert is een pop-up melding voor de gebruiker.
    private void alert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Confirm is een pop-up waarmee de gebruiker iets kan bevestigen.
    private boolean confirm(String title, String msg) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(title);
        confirm.setHeaderText(null);
        confirm.setContentText(msg);

        return confirm.showAndWait()
                .filter(button -> button == ButtonType.OK)
                .isPresent();
    }
}