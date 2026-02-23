package nl.adainf.fittrack.screens;



/*
 * StartScreen.java - Scherm/GUI: start/login: gebruiker kiezen of aanmaken
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
import nl.adainf.fittrack.dao.UserDao;
import nl.adainf.fittrack.model.User;

import java.util.Objects;
import java.util.function.IntConsumer;
// JavaFX scherm: bouwt de UI op en koppelt knoppen aan acties.
public class StartScreen {

    private final Scene scene;

    public StartScreen(IntConsumer onUserSelected) {

        Label title = new Label("FitTrack");
        title.getStyleClass().add("app-title");

        Label subtitle = new Label("Start");
        subtitle.getStyleClass().add("subtitle");

        TextField tfName = new TextField();
        tfName.setPromptText("Enter your name");

        Button btnCreate = new Button("Create user");
        btnCreate.getStyleClass().add("btn-secondary");

        Label lblExisting = new Label("Existing users:");
        lblExisting.getStyleClass().add("muted");

        ListView<User> lvUsers = new ListView<>();

        Button btnUse = new Button("Use selected user");
        btnUse.getStyleClass().add("btn-primary");

        Button btnDelete = new Button("Delete selected user");
        btnDelete.getStyleClass().add("btn-danger");

        Label msg = new Label();
        msg.getStyleClass().add("muted");

        UserDao userDao = new UserDao();

        Runnable refresh = () -> lvUsers.getItems().setAll(userDao.getAll());
        refresh.run();

        // Klik op knop -> voer actie uit


        btnCreate.setOnAction(e -> {
            String name = tfName.getText().trim();
            if (name.isEmpty()) {
                msg.setText("Enter a name.");
                return;
            }

            int newId = userDao.insert(name);
            if (newId != -1) {
                msg.setText("User created.");
                tfName.clear();
                refresh.run();
            } else {
                msg.setText("Could not create user.");
            }
        });

        // Klik op knop -> voer actie uit


        btnUse.setOnAction(e -> {
            User selected = lvUsers.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msg.setText("Select a user first.");
                return;
            }
            onUserSelected.accept(selected.getId());
        });

        // Klik op knop -> voer actie uit


        btnDelete.setOnAction(e -> {
            User selected = lvUsers.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msg.setText("Select a user first.");
                return;
            }

            Alert confirm = // Alert = pop-up melding voor de gebruiker
        new Alert(Alert.AlertType.CONFIRMATION);
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

        scene = new Scene(root, 650, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }
}
