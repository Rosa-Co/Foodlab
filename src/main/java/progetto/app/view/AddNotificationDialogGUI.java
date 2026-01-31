package progetto.app.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import progetto.app.controller.AppController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AddNotificationDialogGUI {

    @FXML
    private RadioButton allCoursesRadio;
    @FXML
    private RadioButton specificCourseRadio;
    @FXML
    private VBox courseSelectionBox;
    @FXML
    private ComboBox<String> courseComboBox;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea messageArea;
    @FXML
    private Label errorLabel;

    private ToggleGroup toggleGroup;
    private final Map<String, Integer> courseMap = new HashMap<>(); // Name -> ID

    @FXML
    public void initialize() {
        toggleGroup = new ToggleGroup();
        allCoursesRadio.setToggleGroup(toggleGroup);
        specificCourseRadio.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean specific = (newVal == specificCourseRadio);
            courseSelectionBox.setVisible(specific);
            courseSelectionBox.setManaged(specific);
        });

        loadCourses();
    }

    private void loadCourses() {
        List<Map<String, Object>> courses = AppController.getInstance().getSimpleCoursesData();
        for (Map<String, Object> c : courses) {
            String name = (String) c.get("titolo");
            int id = (Integer) c.get("id");
            courseComboBox.getItems().add(name);
            courseMap.put(name, id);
        }
    }

    public Map<String, Object> getNotificationData() {
        Map<String, Object> data = new HashMap<>();
        String title = titleField.getText();
        String message = messageArea.getText();

        if (title == null || title.trim().isEmpty() || message == null || message.trim().isEmpty()) {
            return null; // Validation handled in showDialog
        }

        data.put("titolo", title);
        data.put("contenuto", message);

        if (specificCourseRadio.isSelected()) {
            String selectedCourse = courseComboBox.getValue();
            if (selectedCourse != null) {
                data.put("corsoId", courseMap.get(selectedCourse));
            } else {
                return null; // Invalid state
            }
        } else {
            data.put("corsoId", null);
        }

        return data;
    }

    public static Optional<Map<String, Object>> showDialog(Window owner) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                AddNotificationDialogGUI.class.getResource("/progetto/app/dialog/AddNotificationDialog.fxml"));
        DialogPane dialogPane = loader.load();
        AddNotificationDialogGUI controller = loader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Invia Nuova Notifica");
        dialog.initOwner(owner);

        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            Map<String, Object> data = controller.getNotificationData();
            if (data == null) {
                controller.errorLabel.setText("Compila tutti i campi obbligatori.");
                controller.errorLabel.setVisible(true);
                event.consume();
            } else if (controller.specificCourseRadio.isSelected() && controller.courseComboBox.getValue() == null) {
                controller.errorLabel.setText("Seleziona un corso.");
                controller.errorLabel.setVisible(true);
                event.consume();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return Optional.ofNullable(controller.getNotificationData());
        }
        return Optional.empty();
    }
}
