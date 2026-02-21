package progetto.app.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import progetto.app.controller.AppController;
import progetto.app.dto.CourseDTO;
import progetto.app.dto.NotificationDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller del dialog per inviare una nuova notifica.
 * <p>
 * Lo chef può scegliere di inviare la notifica a tutti i corsi o a uno
 * specifico. Il metodo statico {@link #showDialog(Window)} apre il dialog
 * e restituisce un {@link NotificationDTO} con i dati inseriti.
 * </p>
 */
public class AddNotificationDialogGUI {

    /** Radio button per inviare la notifica a tutti i corsi. */
    @FXML
    private RadioButton allCoursesRadio;
    /** Radio button per inviare la notifica a un corso specifico. */
    @FXML
    private RadioButton specificCourseRadio;
    /**
     * Pannello con la ComboBox del corso, visibile solo se si sceglie un corso
     * specifico.
     */
    @FXML
    private VBox courseSelectionBox;
    /** ComboBox per selezionare il corso destinatario. */
    @FXML
    private ComboBox<String> courseComboBox;
    /** Campo testo per il titolo della notifica. */
    @FXML
    private TextField titleField;
    /** Area di testo per il corpo della notifica. */
    @FXML
    private TextArea messageArea;
    /** Label che mostra messaggi di errore di validazione. */
    @FXML
    private Label errorLabel;

    /** Gruppo per i radio button (solo uno selezionabile per volta). */
    private ToggleGroup toggleGroup;
    /** Mappa nome del corso → ID, per risalire all'ID dal testo selezionato. */
    private final Map<String, Integer> courseMap = new HashMap<>(); // Nome -> ID

    /** Configura i radio button e carica l'elenco dei corsi nella ComboBox. */
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

    /** Popola la ComboBox con i titoli dei corsi dello chef. */
    private void loadCourses() {
        List<CourseDTO> courses = AppController.getInstance().getSimpleCoursesData();
        for (CourseDTO c : courses) {
            String name = c.getTitolo();
            int id = c.getId();
            courseComboBox.getItems().add(name);
            courseMap.put(name, id);
        }
    }

    /**
     * Legge i campi del form e costruisce un {@link NotificationDTO}.
     *
     * @return il DTO con i dati, oppure {@code null} se il form non è valido
     */
    public NotificationDTO getNotificationData() {
        String title = titleField.getText();
        String message = messageArea.getText();

        if (title == null || title.trim().isEmpty() || message == null || message.trim().isEmpty()) {
            return null;
        }

        Integer corsoId = null;

        if (specificCourseRadio.isSelected()) {
            String selectedCourse = courseComboBox.getValue();
            if (selectedCourse != null) {
                corsoId = courseMap.get(selectedCourse);
            } else {
                return null;
            }
        } else {
            corsoId = null;
        }

        return new NotificationDTO(title, message, corsoId);
    }

    /**
     * Mostra il dialog modale per creare una nuova notifica.
     *
     * @param owner la finestra proprietaria del dialog
     * @return un {@link Optional} con il {@link NotificationDTO}, oppure vuoto se
     *         annullato
     * @throws IOException se il file FXML non viene trovato
     */
    public static Optional<NotificationDTO> showDialog(Window owner) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                AddNotificationDialogGUI.class.getResource("/progetto/app/dialog/AddNotificationDialog.fxml"));
        DialogPane dialogPane = loader.load();
        // Poiché il metodo è statico.
        AddNotificationDialogGUI controller = loader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Invia Nuova Notifica");
        dialog.initOwner(owner);

        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            NotificationDTO data = controller.getNotificationData();
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
