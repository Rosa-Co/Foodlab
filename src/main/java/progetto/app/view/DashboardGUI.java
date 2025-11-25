package progetto.app.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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

    @FXML private StackPane contentContainer;
    @FXML private ImageView logoImage;
    // Sidebar buttons
    @FXML private Button homeButton;
    @FXML private Button coursesButton;
    @FXML private Button reportButton;

    // Header
    @FXML private Label chefNameLabel;
    @FXML private MenuItem logoutMenuItem;

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
        loadViewInContainer("home");
        updateSidebarSelection(homeButton);
    }

    /**
     * Carica la vista Corsi nel container centrale
     */
    private void loadCoursesView() {
        loadViewInContainer("courses");
        updateSidebarSelection(coursesButton);
    }

    /**
     * Carica la vista Report nel container centrale
     */
    private void loadReportView() {
        loadViewInContainer("report");
        updateSidebarSelection(reportButton);
    }

    /**
     * Carica dinamicamente una view FXML nel container centrale
     */
    private void loadViewInContainer(String nameView) {
        Parent view = appController.getView(nameView);
        try{
            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(view);
        } catch (Exception e) {
            System.err.println("Errore nel caricamento della view: " + nameView);
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
        System.out.println("Logout...");
        appController.navigateToLogin();
    }

    /**
     * Imposta il nome dello chef nell'header
     */
    public void setChefName() {
        if (chefNameLabel != null) {
            chefNameLabel.setText("Chef " + "PierLuigi");
        }
    }
}