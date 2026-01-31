package progetto.app.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import progetto.app.controller.AppController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import progetto.app.dto.CourseDTO;

public class CoursesViewGUI implements Initializable {

    @FXML
    private Button createCourseButton;
    @FXML
    private VBox coursesContainer;

    private final AppController appController = AppController.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (createCourseButton != null) {
            createCourseButton.setOnAction(e -> openCreateCourseDialog());
        }
    }

    public void loadCourses() {
        coursesContainer.getChildren().clear();
        Label loadingLabel = new Label("Caricamento in corso...");
        loadingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #95a5a6;");
        coursesContainer.getChildren().add(loadingLabel);

        new Thread(() -> {
            List<CourseDTO> courses = appController.getCoursesData();
            Platform.runLater(() -> {
                coursesContainer.getChildren().clear();
                if (courses.isEmpty()) {
                    Label placeholder = new Label("Nessun corso presente.");
                    placeholder.setStyle("-fx-font-size: 16px; -fx-text-fill: -color-fg-muted;");
                    coursesContainer.getChildren().add(placeholder);
                } else {
                    for (CourseDTO course : courses) {
                        coursesContainer.getChildren().add(createCourseCard(course));
                    }
                }
            });
        }).start();
    }

    private Node createCourseCard(CourseDTO course) {
        VBox card = new VBox(5);
        card.setStyle(
                "-fx-background-color: white; -fx-padding: 12; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 5, 0, 0, 2); -fx-cursor: hand;");

        // Add click listener to open details
        card.setOnMouseClicked(e -> {
            int corsoId = course.getId();
            String titolo = course.getTitolo();
            appController.showCourseDetailsDialog(card.getScene().getWindow(), corsoId, titolo);
        });

        // Header: Title + Category
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(course.getTitolo());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #2c3e50;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label category = new Label(course.getCategoria().toUpperCase());
        category.setStyle(
                "-fx-background-color: #e8f6f3; -fx-text-fill: #16a085; -fx-padding: 3 8; -fx-background-radius: 4; -fx-font-size: 10px; -fx-font-weight: bold;");

        header.getChildren().addAll(title, spacer, category);

        // Details Row 1
        HBox details1 = new HBox(15);
        details1.setAlignment(Pos.CENTER_LEFT);

        Label dateLabel = createIconLabel("fas-calendar-alt", course.getDataInizio().toString());
        Label sessionsLabel = createIconLabel("fas-layer-group", course.getNumeroSessioni() + " Sessioni");

        details1.getChildren().addAll(dateLabel, sessionsLabel);

        // Details Row 2
        HBox details2 = new HBox(15);
        details2.setAlignment(Pos.CENTER_LEFT);

        Label freqLabel = createIconLabel("fas-clock", course.getFrequenza());

        details2.getChildren().addAll(freqLabel);

        card.getChildren().addAll(header, details1, details2);
        return card;
    }

    private Label createIconLabel(String iconCode, String text) {
        Label label = new Label(text);
        FontIcon icon = new FontIcon(iconCode);
        icon.setIconColor(Color.web("#7f8c8d"));
        label.setGraphic(icon);
        label.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
        label.setGraphicTextGap(6);
        return label;
    }

    private void openCreateCourseDialog() {
        boolean success = appController.showCreateCourseDialog(createCourseButton.getScene().getWindow());
        if (success) {
            loadCourses();
        }
    }
}
