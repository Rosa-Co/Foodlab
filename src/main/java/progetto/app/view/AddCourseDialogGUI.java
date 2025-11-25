package progetto.app.view;

import progetto.app.enums.CuisineCategory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import progetto.app.controller.AppController;
import progetto.app.dto.CourseDTO;
import progetto.app.dto.RecipeDTO;
import progetto.app.dto.SessionDTO;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddCourseDialogGUI implements Initializable {

    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField frequencyField;
    @FXML
    private VBox sessionsContainer;

    private final AppController appController = AppController.getInstance();
    private final List<RecipeDTO> availableRecipes = new ArrayList<>();
    private final List<SessionUIComponents> sessionComponentsList = new ArrayList<>();
    private int sessionCounter = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCategoryComboBox();
        loadRecipes();
        // Add one initial session
        handleAddSession();
    }

    private void setupCategoryComboBox() {
        for (CuisineCategory category : CuisineCategory.values()) {
            categoryComboBox.getItems().add(category.name());
        }
    }

    private void loadRecipes() {
        availableRecipes.clear();
        availableRecipes.addAll(appController.getAllRecipesDTO());
    }

    @FXML
    private void handleAddSession() {
        sessionCounter++;
        SessionUIComponents sessionComponents = createSessionNode(sessionCounter);
        sessionComponentsList.add(sessionComponents);
        sessionsContainer.getChildren().add(sessionComponents.container);
    }

    private SessionUIComponents createSessionNode(int sessionNumber) {
        VBox sessionBox = new VBox(10);
        sessionBox.setStyle(
                "-fx-background-color: -color-bg-default; -fx-padding: 15; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        // Header with remove button
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label sessionLabel = new Label("Sessione " + sessionNumber);
        sessionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button removeButton = new Button();
        removeButton.setGraphic(new FontIcon("fas-trash"));
        removeButton.getStyleClass().addAll("button-icon", "danger");

        // Components for data gathering
        DatePicker datePicker = new DatePicker();
        ComboBox<String> modeCombo = new ComboBox<>();
        Spinner<Integer> durationSpinner = new Spinner<>(30, 480, 60);
        TextArea descriptionArea = new TextArea();
        ComboBox<RecipeDTO> recipeCombo = new ComboBox<>();

        SessionUIComponents components = new SessionUIComponents(sessionBox, sessionLabel, datePicker, modeCombo,
                durationSpinner, descriptionArea, recipeCombo);

        removeButton.setOnAction(e -> {
            sessionsContainer.getChildren().remove(sessionBox);
            sessionComponentsList.remove(components);
            updateSessionNumbers();
        });

        header.getChildren().addAll(sessionLabel, spacer, removeButton);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        // Date
        datePicker.setPromptText("Data Sessione");
        datePicker.setMaxWidth(Double.MAX_VALUE);

        // Mode
        modeCombo.getItems().addAll("Online", "In Presenza");
        modeCombo.setPromptText("Modalità");
        modeCombo.setMaxWidth(Double.MAX_VALUE);

        // Duration
        durationSpinner.setEditable(true);
        durationSpinner.setMaxWidth(Double.MAX_VALUE);

        // Description
        descriptionArea.setPromptText("Descrizione dettagliata della sessione...");
        descriptionArea.setPrefRowCount(2);
        descriptionArea.setWrapText(true);

        // Recipe (only for In Person)
        recipeCombo.getItems().addAll(availableRecipes);
        recipeCombo.setPromptText("Seleziona Ricetta da preparare");
        recipeCombo.setMaxWidth(Double.MAX_VALUE);
        recipeCombo.setVisible(false);
        recipeCombo.setManaged(false);

        modeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isPerson = "In Presenza".equals(newVal);
            recipeCombo.setVisible(isPerson);
            recipeCombo.setManaged(isPerson);
        });

        // Layout
        VBox dateBox = new VBox(5, new Label("Data"), datePicker);
        VBox modeBox = new VBox(5, new Label("Modalità"), modeCombo);
        VBox durationBox = new VBox(5, new Label("Durata (min)"), durationSpinner);
        VBox recipeBox = new VBox(5, new Label("Ricetta"), recipeCombo);

        grid.add(dateBox, 0, 0);
        grid.add(modeBox, 1, 0);
        grid.add(durationBox, 2, 0);

        grid.add(new Label("Descrizione"), 0, 1);
        grid.add(descriptionArea, 0, 2, 3, 1);

        grid.add(recipeBox, 0, 3, 3, 1);
        recipeBox.visibleProperty().bind(recipeCombo.visibleProperty());
        recipeBox.managedProperty().bind(recipeCombo.managedProperty());

        sessionBox.getChildren().addAll(header, grid);

        return components;
    }

    private void updateSessionNumbers() {
        sessionCounter = 0;
        for (SessionUIComponents components : sessionComponentsList) {
            sessionCounter++;
            components.label.setText("Sessione " + sessionCounter);
        }
    }

    public CourseDTO getCourseDTO() {
        return new CourseDTO(
                titleField.getText(),
                categoryComboBox.getValue(),
                startDatePicker.getValue(),
                frequencyField.getText());
    }

    public List<SessionDTO> getSessionDTOs() {
        List<SessionDTO> results = new ArrayList<>();
        for (SessionUIComponents components : sessionComponentsList) {
            SessionDTO sessionDTO = new SessionDTO(
                    components.datePicker.getValue(),
                    components.modeCombo.getValue(),
                    components.durationSpinner.getValue(),
                    components.descriptionArea.getText(),
                    components.recipeCombo.getValue());
            results.add(sessionDTO);
        }
        return results;
    }

    /**
     * Helper class to hold UI components for a session
     * Static to avoid implicit reference to outer class
     */
    private static class SessionUIComponents {
        VBox container;
        Label label;
        DatePicker datePicker;
        ComboBox<String> modeCombo;
        Spinner<Integer> durationSpinner;
        TextArea descriptionArea;
        ComboBox<RecipeDTO> recipeCombo;

        public SessionUIComponents(VBox container, Label label, DatePicker d, ComboBox<String> m, Spinner<Integer> du,
                TextArea de, ComboBox<RecipeDTO> r) {
            this.container = container;
            this.label = label;
            this.datePicker = d;
            this.modeCombo = m;
            this.durationSpinner = du;
            this.descriptionArea = de;
            this.recipeCombo = r;
        }
    }
}
