package progetto.app.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardGUI implements Initializable {

    // Views
    @FXML private VBox homeView;
    @FXML private VBox coursesView;
    @FXML private VBox reportView;

    // Sidebar buttons (dai file inclusi)
    @FXML private Button homeButton;
    @FXML private Button coursesButton;
    @FXML private Button reportButton;

    // Header elements
    @FXML private Label chefNameLabel;
    @FXML private MenuItem logoutMenuItem;

    // Stats labels
    @FXML private Label activeCoursesCount;
    @FXML private Label sessionsThisMonth;
    @FXML private Label recipesCount;
    @FXML private Label studentsCount;

    // Report
    @FXML private ComboBox<String> monthSelector;
    @FXML private Button generateReportButton;
    @FXML private Label totalCoursesLabel;
    @FXML private Label onlineSessionsLabel;
    @FXML private Label practicalSessionsLabel;
    @FXML private Label avgRecipesLabel;
    @FXML private Label minRecipesLabel;
    @FXML private Label maxRecipesLabel;
    @FXML private StackPane chartContainer;

    // Tables
    @FXML private TableView recentCoursesTable;
    @FXML private VBox coursesContainer;
    @FXML private Button addCourseButton;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private ComboBox<String> statusFilter;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Usa Platform.runLater per aspettare che fx:include carichi tutti gli elementi
        javafx.application.Platform.runLater(() -> {
            setupEventHandlers();
            loadDashboardData();
        });
    }

    private void setupEventHandlers() {
        // Navigation buttons (potrebbero essere null se fx:include non è ancora caricato)
        if (homeButton != null) {
            homeButton.setOnAction(e -> showHomeView());
        }
        if (coursesButton != null) {
            coursesButton.setOnAction(e -> showCoursesView());
        }
        if (reportButton != null) {
            reportButton.setOnAction(e -> showReportView());
        }

        // Header actions
        if (logoutMenuItem != null) {
            logoutMenuItem.setOnAction(e -> handleLogout());
        }

        // Course actions
        if (addCourseButton != null) {
            addCourseButton.setOnAction(e -> showAddCourseDialog());
        }

        // Report actions
        if (generateReportButton != null) {
            generateReportButton.setOnAction(e -> generateReport());
        }
    }

    private void loadDashboardData() {
        // Carica dati dal database
        if (chefNameLabel != null) {
            chefNameLabel.setText("Chef " + getCurrentChefName());
        }
        // ... altre operazioni di caricamento
    }

    private String getCurrentChefName() {
        // Recupera nome chef dal session manager
        return "Mario Rossi";
    }

    private void showHomeView() {
        switchView(homeView);
        updateSidebarSelection(homeButton);
    }

    private void showCoursesView() {
        switchView(coursesView);
        updateSidebarSelection(coursesButton);
    }

    private void showReportView() {
        switchView(reportView);
        updateSidebarSelection(reportButton);
    }

    private void switchView(VBox targetView) {
        homeView.setVisible(false);
        homeView.setManaged(false);
        coursesView.setVisible(false);
        coursesView.setManaged(false);
        reportView.setVisible(false);
        reportView.setManaged(false);

        targetView.setVisible(true);
        targetView.setManaged(true);
    }

    private void updateSidebarSelection(Button selected) {
        homeButton.getStyleClass().removeAll("accent");
        coursesButton.getStyleClass().removeAll("accent");
        reportButton.getStyleClass().removeAll("accent");

        homeButton.getStyleClass().add("flat");
        coursesButton.getStyleClass().add("flat");
        reportButton.getStyleClass().add("flat");

        selected.getStyleClass().remove("flat");
        selected.getStyleClass().add("accent");
    }

    private void showAddCourseDialog() {
        // Mostra dialog per aggiungere corso
        System.out.println("Apertura dialog nuovo corso...");
    }

    private void generateReport() {
        // Genera report con JFreeChart
        System.out.println("Generazione report...");
    }

    private void handleLogout() {
        // Torna al login
        System.out.println("Logout...");
    }
}