package progetto.app.view;

import javafx.fxml.FXMLLoader;
import javafx.stage.Window;
import progetto.app.dto.CourseWithSessionsDTO;
import progetto.app.enums.CuisineCategory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.javafx.FontIcon;
import progetto.app.controller.AppController;
import progetto.app.dto.CourseDTO;
import progetto.app.dto.RecipeDTO;
import progetto.app.dto.SessionDTO;
import progetto.app.enums.Frequency;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class AddCourseDialogGUI implements Initializable {

    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<String> frequencyComboBox;
    @FXML
    private VBox sessionsContainer;

    private final AppController appController = AppController.getInstance();
    private final List<RecipeDTO> availableRecipes = new ArrayList<>();
    private final List<SessionUIComponents> sessionComponentsList = new ArrayList<>();
    private int sessionCounter = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCategoryComboBox();
        setupFrequencyComboBox();
        setupStartDatePicker();
        loadRecipes();
        // Add one initial session
        handleAddSession();
    }

    private void setupStartDatePicker() {
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate oggi = LocalDate.now();
                setDisable(empty || date.isBefore(oggi));
            }
        });
    }

    private void setupCategoryComboBox() {
        for (CuisineCategory category : CuisineCategory.values()) {
            categoryComboBox.getItems().add(category.name());
        }
    }

    private void setupFrequencyComboBox() {
        for (Frequency frequency : Frequency.values()) {
            frequencyComboBox.getItems().add(frequency.name()); //? capisci perchè da warning nonostante questo metodo
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

        SessionUIComponents components = new SessionUIComponents(sessionBox, sessionLabel, datePicker, modeCombo,
                durationSpinner, descriptionArea);

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
        datePicker.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        LocalDate dataInizio = startDatePicker.getValue();
                        setDisable(empty || date.isBefore(Objects.requireNonNullElseGet(dataInizio, LocalDate::now)));
                    }
                });

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

        // Recipes Container (only for In Person)
        VBox recipesContainer = new VBox(10);
        Label recipesLabel = new Label("Ricette:");
        Button addRecipeBtn = new Button("Aggiungi Ricetta");

        addRecipeBtn.setOnAction(e -> addRecipeRow(components));

        VBox recipesWrapper = new VBox(5, recipesLabel, recipesContainer, addRecipeBtn);
        recipesWrapper.setVisible(false);
        recipesWrapper.setManaged(false);

        components.recipesContainer = recipesContainer; // Link to components


        modeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isOnSite = "In Presenza".equals(newVal);
            recipesWrapper.setVisible(isOnSite);
            recipesWrapper.setManaged(isOnSite);
        });

        // Layout
        VBox dateBox = new VBox(5, new Label("Data"), datePicker);
        VBox modeBox = new VBox(5, new Label("Modalità"), modeCombo);
        VBox durationBox = new VBox(5, new Label("Durata (min)"), durationSpinner);

        grid.add(dateBox, 0, 0);
        grid.add(modeBox, 1, 0);
        grid.add(durationBox, 2, 0);

        grid.add(new Label("Descrizione"), 0, 1);
        grid.add(descriptionArea, 0, 2, 3, 1);

        grid.add(recipesWrapper, 0, 3, 3, 1);

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
        if(isCourseDTOValid()) {
            return new CourseDTO(
                    titleField.getText(),
                    categoryComboBox.getValue(),
                    startDatePicker.getValue(),
                    frequencyComboBox.getValue());
        }
        return null;
    }

    public boolean isCourseDTOValid() {
        if(!areCourseFieldsFilled()){
            appController.showWarningDialog("Attenzione!","Riempire tutti i campi del corso.");
            return false;
        }
        if(!isStartDateValid()){
            appController.showWarningDialog("Attenzione!","Il corso non può iniziare nel passato!");
            return false;
        }
        return true;
    }
    public boolean areCourseFieldsFilled() {
        return !titleField.getText().isBlank()
                && categoryComboBox.getValue() != null
                && startDatePicker.getValue() != null
                && frequencyComboBox.getValue() != null;
    }

    public boolean isStartDateValid(){
        return startDatePicker.getValue().isAfter(LocalDate.now());
    }

    public List<SessionDTO> getSessionDTOs() {
        List<SessionDTO> results = new ArrayList<>();
        for (SessionUIComponents components : sessionComponentsList) {
            List<RecipeDTO> sessionRecipes = new ArrayList<>();
            if ("In Presenza".equals(components.modeCombo.getValue())) {
                for (RecipeUIComponents rc : components.recipeRows) {
                    if ("Esistente".equals(rc.typeCombo.getValue())) {
                        if (rc.existingCombo.getValue() != null) {
                            sessionRecipes.add(rc.existingCombo.getValue());
                        }
                    } else {
                        // New Recipe
                        String name = rc.nameField.getText();
                        String desc = rc.descArea.getText();
                        if(name.isBlank() || desc.isBlank()){
                            appController.showWarningDialog("Attenzione!","Riempire tutti i campi.");
                            return null;
                        } else{
                            // ID 0 indicates new recipe
                            sessionRecipes.add(new RecipeDTO(0, name, desc, "Personalizzata"));
                        }
                    }
                }
            }
            if (!components.isSessionValid(components)) {
                appController.showWarningDialog("Attenzione!", "Riempire tutti i campi delle sessioni.");
                return null;
            }

            if(!components.isDateValid(components, startDatePicker)) {
                appController.showWarningDialog("Attenzione!","La data delle sessioni deve essere conseguente a quella di inizio del corso.");
                return null;
            }

            SessionDTO sessionDTO = new SessionDTO(
                    components.datePicker.getValue(),
                    components.modeCombo.getValue(),
                    components.durationSpinner.getValue(),
                    components.descriptionArea.getText(),
                    sessionRecipes);

            results.add(sessionDTO);
        }

        return results;
    }

    private void addRecipeRow(SessionUIComponents sessionComponents) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: rgba(0,0,0,0.05); -fx-padding: 5; -fx-background-radius: 4;");

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Esistente", "Nuova");
        typeCombo.setValue("Esistente");
        typeCombo.setPrefWidth(100);

        StackPane contentPane = new StackPane();
        contentPane.setAlignment(Pos.CENTER_LEFT);

        // Existing
        ComboBox<RecipeDTO> existingCombo = new ComboBox<>();
        existingCombo.getItems().addAll(availableRecipes);
        existingCombo.setMaxWidth(Double.MAX_VALUE);
        existingCombo.setPromptText("Scegli ricetta...");

        // New
        VBox newRecipeBox = new VBox(5);
        TextField nameField = new TextField();
        nameField.setPromptText("Nome Ricetta");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Ingredienti / Procedimento");
        descArea.setPrefRowCount(3);
        descArea.setWrapText(true);
        newRecipeBox.getChildren().addAll(nameField, descArea);

        // Logic to switch
        Runnable updateView = () -> {
            contentPane.getChildren().clear();
            if ("Esistente".equals(typeCombo.getValue())) {
                contentPane.getChildren().add(existingCombo);
            } else {
                contentPane.getChildren().add(newRecipeBox);
            }
        };

        typeCombo.setOnAction(e -> updateView.run());
        updateView.run(); // ! ...

        Button removeBtn = new Button();
        removeBtn.setGraphic(new FontIcon("fas-trash"));
        removeBtn.getStyleClass().addAll("button-icon", "danger");

        RecipeUIComponents rc = new RecipeUIComponents(typeCombo, existingCombo, nameField, descArea, row);
        sessionComponents.recipeRows.add(rc);

        removeBtn.setOnAction(e -> {
            sessionComponents.recipesContainer.getChildren().remove(row);
            sessionComponents.recipeRows.remove(rc);
        });

        row.getChildren().addAll(typeCombo, contentPane, removeBtn);
        HBox.setHgrow(contentPane, Priority.ALWAYS);
        existingCombo.prefWidthProperty().bind(contentPane.widthProperty());
        newRecipeBox.prefWidthProperty().bind(contentPane.widthProperty());

        sessionComponents.recipesContainer.getChildren().add(row);
    }

    public static Optional<CourseWithSessionsDTO> showDialog(Window owner) throws IOException,NullPointerException {
        FXMLLoader loader = new FXMLLoader(
                AddCourseDialogGUI.class.getResource("/progetto/app/dialog/AddCourseDialog.fxml")
        );
        DialogPane dialogPane = loader.load();
        AddCourseDialogGUI GUI = loader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Crea Nuovo Corso");
        dialog.initOwner(owner);  //split methods getResult

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.FINISH) {
            CourseDTO courseDTO = GUI.getCourseDTO();
            List<SessionDTO> sessionDTOs = GUI.getSessionDTOs();
            return Optional.of(new CourseWithSessionsDTO(courseDTO, sessionDTOs));
        }
        return Optional.empty();
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
        VBox recipesContainer;
        List<RecipeUIComponents> recipeRows = new ArrayList<>();

        public SessionUIComponents(VBox container, Label label, DatePicker d, ComboBox<String> m, Spinner<Integer> du,
                TextArea de) {
            this.container = container;
            this.label = label;
            this.datePicker = d;
            this.modeCombo = m;
            this.durationSpinner = du;
            this.descriptionArea = de;
        }

        public boolean isSessionValid(SessionUIComponents components) {
            return components.datePicker.getValue() != null
                    && components.modeCombo.getValue() != null
                    && !components.descriptionArea.getText().isBlank();
        }
        public boolean isDateValid(SessionUIComponents components, DatePicker date) {
            return components.datePicker.getValue().isAfter(date.getValue());
        }
    }

    private static class RecipeUIComponents {
        ComboBox<String> typeCombo;
        ComboBox<RecipeDTO> existingCombo;
        TextField nameField;
        TextArea descArea;
        HBox container;

        public RecipeUIComponents(ComboBox<String> typeCombo, ComboBox<RecipeDTO> existingCombo, TextField nameField,
                TextArea descArea, HBox container) {
            this.typeCombo = typeCombo;
            this.existingCombo = existingCombo;
            this.nameField = nameField;
            this.descArea = descArea;
            this.container = container;
        }
    }

}
