package progetto.app.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import progetto.app.controller.AppController;
import java.net.URL;
import java.util.ResourceBundle;

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
        loadCourses();
    }

    private void loadCourses() {
        // Here we would load the courses from the DB and populate the container.
    }

    private void openCreateCourseDialog() {
        boolean success = appController.showCreateCourseDialog(createCourseButton.getScene().getWindow());
        if (success) {
            loadCourses();
        }
    }
}
