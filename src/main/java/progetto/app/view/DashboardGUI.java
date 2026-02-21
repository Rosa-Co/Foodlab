package progetto.app.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import progetto.app.controller.AppController;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller principale della Dashboard.
 * Gestisce solo la sidebar e il container centrale.
 * Le view specifiche sono file FXML separati caricati dinamicamente.
 */
public class DashboardGUI implements Initializable {

    @FXML
    private StackPane contentContainer;
    @FXML
    private ImageView logoImage;
    // Sidebar buttons
    @FXML
    private Button homeButton;
    @FXML
    private Button coursesButton;
    @FXML
    private Button recipesButton;
    @FXML
    private Button notificationsButton;
    @FXML
    private Button reportButton;

    // Header
    @FXML
    private Label chefNameLabel;
    @FXML
    private MenuItem logoutMenuItem;

    private final AppController appController = AppController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Carica il logo
        try {
            Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/progetto/app/logo.png")));
            logoImage.setImage(logo);
        } catch (Exception e) {
            System.err.println("Logo non trovato, uso icona di default");
            // Se il logo non viene trovato, non fa nulla (ImageView resta vuota)
        }

        Platform.runLater(() -> {
            setupEventHandlers();
            loadInitialView();
        });
    }

    private void setupEventHandlers() {

        if (homeButton != null) {
            homeButton.setOnAction(e -> loadHomeView());
        }
        if (coursesButton != null) {
            coursesButton.setOnAction(e -> loadCoursesView());
        }
        if (recipesButton != null) {
            recipesButton.setOnAction(e -> loadRecipesView());
        }
        if (notificationsButton != null) {
            notificationsButton.setOnAction(e -> loadNotificationsView());
        }
        if (reportButton != null) {
            reportButton.setOnAction(e -> loadReportView());
        }

        // Logout
        if (logoutMenuItem != null) {
            logoutMenuItem.setOnAction(e -> handleLogout());
        }
    }

    private void loadInitialView() {
        // Carica la home di default
        loadHomeView();
    }

    /**
     * Carica la vista Home nel container centrale
     */
    private void loadHomeView() {
        // Display welcome message instead of loading a view
        contentContainer.getChildren().clear();

        VBox welcomeBox = new VBox(20);
        welcomeBox.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Benvenuto Chef!");
        welcomeLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label subLabel = new Label("Seleziona una voce dal menu laterale per iniziare.");
        subLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");

        welcomeBox.getChildren().addAll(welcomeLabel, subLabel);
        contentContainer.getChildren().add(welcomeBox);

        updateSidebarSelection(homeButton);
    }

    /**
     * Carica la vista Corsi nel container centrale
     */
    private void loadCoursesView() {
        loadViewInContainer("courses");
        updateSidebarSelection(coursesButton);

        CoursesViewGUI controller = (CoursesViewGUI) appController.getController("courses");
        controller.loadCourses();
    }

    /**
     * Carica la vista Ricette nel container centrale
     */
    private void loadRecipesView() {
        loadViewInContainer("recipes");
        updateSidebarSelection(recipesButton);

        RecipesViewGUI controller = (RecipesViewGUI) appController.getController("recipes");
        if (controller != null) {
            controller.loadRecipes();
        }
    }

    /**
     * Carica dinamicamente una view FXML nel container centrale
     */
    private void loadViewInContainer(String nameView) {
        Parent view = appController.getView(nameView);
        if (view == null)
            return;
        try {
            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(view);
        } catch (Exception e) {
            System.err.println("Errore nel caricamento della view: " + nameView);
        }
    }

    // NOTIFICHE

    private void loadNotificationsView() {
        loadViewInContainer("notifications");
        updateSidebarSelection(notificationsButton);

        NotificationsViewGUI controller = (NotificationsViewGUI) appController
                .getController("notifications");
        if (controller != null) {
            controller.loadNotifications();
        }
    }

    private void loadReportView() {
        loadViewInContainer("report");
        updateSidebarSelection(reportButton);

        ReportViewGUI controller = (ReportViewGUI) appController.getController("report");
        if (controller != null) {
            controller.loadData();
        }
    }

    /**
     * Aggiorna lo stile della sidebar per evidenziare la sezione attiva
     */
    private void updateSidebarSelection(Button selectedButton) {
        // Rimuovi accent da tutti
        if (homeButton != null) {
            homeButton.getStyleClass().removeAll("accent");
            if (!homeButton.getStyleClass().contains("flat")) {
                homeButton.getStyleClass().add("flat");
            }
        }
        if (coursesButton != null) {
            coursesButton.getStyleClass().removeAll("accent");
            if (!coursesButton.getStyleClass().contains("flat")) {
                coursesButton.getStyleClass().add("flat");
            }
        }
        if (recipesButton != null) {
            recipesButton.getStyleClass().removeAll("accent");
            if (!recipesButton.getStyleClass().contains("flat")) {
                recipesButton.getStyleClass().add("flat");
            }
        }
        if (notificationsButton != null) {
            notificationsButton.getStyleClass().removeAll("accent");
            if (!notificationsButton.getStyleClass().contains("flat")) {
                notificationsButton.getStyleClass().add("flat");
            }
        }
        if (reportButton != null) {
            reportButton.getStyleClass().removeAll("accent");
            if (!reportButton.getStyleClass().contains("flat")) {
                reportButton.getStyleClass().add("flat");
            }
        }

        // Aggiungi accent al selezionato
        if (selectedButton != null) {
            selectedButton.getStyleClass().remove("flat");
            selectedButton.getStyleClass().add("accent");
        }
    }

    /**
     * Gestisce il logout
     */
    private void handleLogout() {
        appController.logout();
    }

    /**
     * Imposta il nome dello chef nell'header
     */
    public void updateUsername(String username) {
        if (chefNameLabel != null) {
            chefNameLabel.setText("Ciao, " + username);
        }
    }
}