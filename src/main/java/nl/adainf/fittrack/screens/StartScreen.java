package nl.adainf.fittrack.screens;

/*
 * StartScreen.java
 *
 * Dit is het eerste scherm van de FitTrack app.
 *
 * Op dit scherm kan de gebruiker:
 * - een nieuwe gebruiker aanmaken
 * - een bestaande gebruiker kiezen
 * - een gebruiker verwijderen
 *
 * Ik gebruik hier JavaFX controls zoals:
 * - Label voor tekst
 * - TextField om een naam in te vullen
 * - Button om ergens op te klikken
 * - ListView om bestaande gebruikers te tonen
 * - VBox om alles netjes onder elkaar te zetten
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nl.adainf.fittrack.dao.UserDao;
import nl.adainf.fittrack.model.User;

import java.util.Objects;
import java.util.function.IntConsumer;

public class StartScreen {

    private final Scene scene;

    public StartScreen(IntConsumer onUserSelected) {
        UserDao userDao = new UserDao();

        // Titel bovenaan het scherm.
        Label title = new Label("FitTrack");
        title.getStyleClass().add("app-title");

        // Kleine tekst onder de titel.
        Label subtitle = new Label("Start");
        subtitle.getStyleClass().add("subtitle");

        // Hier vult de gebruiker zijn naam in.
        TextField tfName = new TextField();
        tfName.setPromptText("Enter your name");

        // Knop om een nieuwe gebruiker aan te maken.
        Button btnCreate = new Button("Create user");
        btnCreate.getStyleClass().add("btn-secondary");

        // Tekst boven de lijst met bestaande gebruikers.
        Label lblExisting = new Label("Existing users:");
        lblExisting.getStyleClass().add("muted");

        // Hier worden de gebruikers uit de database getoond.
        ListView<User> lvUsers = new ListView<>();

        // Knop om verder te gaan met de gekozen gebruiker.
        Button btnUse = new Button("Use selected user");
        btnUse.getStyleClass().add("btn-primary");

        // Knop om een gekozen gebruiker te verwijderen.
        Button btnDelete = new Button("Delete selected user");
        btnDelete.getStyleClass().add("btn-danger");

        // Hier zet ik korte meldingen voor de gebruiker.
        Label msg = new Label();
        msg.getStyleClass().add("muted");

        // Met refresh haal ik opnieuw alle gebruikers op uit de database.
        Runnable refresh = () -> lvUsers.getItems().setAll(userDao.getAll());
        refresh.run();

        // Als de gebruiker op deze knop klikt, wordt de naam opgeslagen.
        btnCreate.setOnAction(e -> {
            // getText haalt de tekst uit het invoerveld.
            String name = tfName.getText().trim();

            if (name.isEmpty()) {
                msg.setText("Enter a name.");
                return;
            }

            // UserDao doet het databasewerk. Het scherm zelf schrijft geen SQL.
            int newId = userDao.insert(name);

            if (newId != -1) {
                msg.setText("User created.");
                tfName.clear();
                refresh.run();
            } else {
                msg.setText("Could not create user.");
            }
        });

        // Deze knop gebruikt de geselecteerde gebruiker.
        btnUse.setOnAction(e -> {
            User selected = lvUsers.getSelectionModel().getSelectedItem();

            if (selected == null) {
                msg.setText("Select a user first.");
                return;
            }

            // Ik geef het id van de gekozen gebruiker door aan de rest van de app.
            onUserSelected.accept(selected.getId());
        });

        // Deze knop verwijdert de geselecteerde gebruiker.
        btnDelete.setOnAction(e -> {
            User selected = lvUsers.getSelectionModel().getSelectedItem();

            if (selected == null) {
                msg.setText("Select a user first.");
                return;
            }

            // Eerst vraag ik bevestiging, zodat je niet per ongeluk iemand verwijdert.
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm delete");
            confirm.setHeaderText("Delete user: " + selected.getName() + " ?");
            confirm.setContentText("This will also delete workouts and meals for this user.");

            ButtonType yes = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirm.getButtonTypes().setAll(yes, no);

            confirm.showAndWait().ifPresent(result -> {
                if (result == yes) {
                    boolean ok = userDao.delete(selected.getId());

                    if (ok) {
                        msg.setText("User deleted.");
                        refresh.run();
                    } else {
                        msg.setText("Could not delete user.");
                    }
                }
            });
        });

        // VBox zet alle onderdelen onder elkaar.
        VBox root = new VBox(10,
                title,
                subtitle,
                tfName,
                btnCreate,
                lblExisting,
                lvUsers,
                btnUse,
                btnDelete,
                msg
        );

        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        // Scene is de inhoud van dit scherm.
        scene = new Scene(root, 650, 700);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()
        );
    }

    public Scene getScene() {
        return scene;
    }
}