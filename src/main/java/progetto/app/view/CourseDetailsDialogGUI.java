package progetto.app.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;
import org.kordamp.ikonli.javafx.FontIcon;
import progetto.app.controller.AppController;

import java.io.IOException;
import java.time.LocalDate;


import java.util.List;
import java.util.Optional;
import progetto.app.dto.SessionDTO;
import javafx.scene.control.DateCell;


public class CourseDetailsDialogGUI {

    @FXML
    private Label courseTitleLabel;
    @FXML
    private VBox sessionsContainer;
    @FXML
    private Button closeButton;

    private int corsoId;
    private final AppController appController = AppController.getInstance();
    private boolean dataChanged = false;

    public static boolean showDialog(Window owner, int corsoId, String corsoTitolo) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                CourseDetailsDialogGUI.class.getResource("/progetto/app/dialog/CourseDetailsDialog.fxml"));
        Parent root = loader.load();

        CourseDetailsDialogGUI controller = loader.getController();
        controller.initData(corsoId, corsoTitolo);

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        stage.setScene(new Scene(root));
        stage.setTitle("Dettagli Corso");
        stage.showAndWait();

        return controller.dataChanged;
    }

    public void initData(int corsoId, String corsoTitolo) {
        this.corsoId = corsoId;
        courseTitleLabel.setText("Corso: " + corsoTitolo);
        loadSessions();
        closeButton.setOnAction(e -> closeButton.getScene().getWindow().hide());
    }

    private void loadSessions() {
        sessionsContainer.getChildren().clear();
        List<SessionDTO> sessions = appController.getSessioniByCorso(corsoId);

        if (sessions.isEmpty()) {
            sessionsContainer.getChildren().add(new Label("Nessuna sessione trovata."));
            return;
        }

        for (SessionDTO session : sessions) {
            sessionsContainer.getChildren().add(createSessionCard(session));
        }
    }

    private VBox createSessionCard(SessionDTO session) {
        VBox card = new VBox(5);
        card.setStyle(
                "-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #ddd; -fx-border-radius: 5;");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label sessionNumLabel = new Label("Sessione " + session.getNumeroSessione());
        sessionNumLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Edit/Delete buttons (Icons)
        Button editBtn = new Button();
        editBtn.setGraphic(new FontIcon("fas-edit"));
        editBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: #3498db;");
        editBtn.setOnAction(e -> handleEditSession(session));

        Button deleteBtn = new Button();
        deleteBtn.setGraphic(new FontIcon("fas-trash"));
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: #e74c3c;");
        deleteBtn.setOnAction(e -> handleDeleteSession(session));

        header.getChildren().addAll(sessionNumLabel, spacer, editBtn, deleteBtn);

        Label dateLabel = new Label("Data: " + session.getDataSessione());
        Label durationLabel = new Label("Durata: " + session.getDurata() + " min (" + session.getModalita() + ")");
        Label descLabel = new Label("Descrizione: " + session.getDescrizione());
        descLabel.setWrapText(true);

        card.getChildren().addAll(header, dateLabel, durationLabel, descLabel);
        return card;
    }

    private void handleDeleteSession(SessionDTO session) {
        int sessionId = session.getId();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Eliminazione");
        alert.setHeaderText("Eliminare la sessione?");
        alert.setContentText("Questa azione non può essere annullata.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (appController.deleteSession(sessionId)) {
                dataChanged = true;
                loadSessions(); // Refresh list
            }
        }
    }

    private void handleEditSession(SessionDTO session) {
        // Simple dialog to edit date, modality, duration, description
        Dialog<SessionDTO> dialog = new Dialog<>();
        dialog.setTitle("Modifica Sessione");
        dialog.setHeaderText("Modifica i dettagli della sessione");

        ButtonType saveButtonType = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        DatePicker datePicker = new DatePicker(session.getDataSessione());
        datePicker.setEditable(false);
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        Spinner<Integer> durationSpinner = new Spinner<>(30, 480, session.getDurata());
        durationSpinner.setEditable(true);

        durationSpinner.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), durationSpinner.getValue(), change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));

        TextArea descArea = new TextArea(session.getDescrizione());
        descArea.setPromptText("Descrizione");
        descArea.setPrefRowCount(3);

        content.getChildren().addAll(
                new Label("Data:"), datePicker,
                new Label("Durata (min):"), durationSpinner,
                new Label("Descrizione:"), descArea);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                SessionDTO newData = new SessionDTO();
                // Preserve ID and session number, update others
                newData.setId(session.getId());
                newData.setNumeroSessione(session.getNumeroSessione());

                newData.setDataSessione(datePicker.getValue());
                newData.setModalita(session.getModalita());
                newData.setDurata(durationSpinner.getValue());
                newData.setDescrizione(descArea.getText());
                return newData;
            }
            return null;
        });

        Optional<SessionDTO> result = dialog.showAndWait();
        result.ifPresent(newData -> {
            if (appController.updateSession(session.getId(), newData)) {
                dataChanged = true;
                loadSessions();
            }
        });
    }
}
