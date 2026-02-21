package progetto.app.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Window;
import progetto.app.controller.AppController;
import progetto.app.dto.RecipeDTO;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller del dialog per aggiungere una nuova ricetta.
 * <p>
 * Il metodo statico {@link #showDialog(Window)} apre il dialog e restituisce
 * un {@link RecipeDTO} con nome e descrizione della ricetta inserita.
 * </p>
 */
public class AddRecipeDialogGUI implements Initializable {

    /** Campo testo per il nome della ricetta. */
    @FXML
    private TextField nameField;
    /** Area di testo per la descrizione (ingredienti e procedimento). */
    @FXML
    private TextArea descriptionArea;

    private final AppController appController = AppController.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Legge i campi del form e restituisce un {@link RecipeDTO}.
     *
     * @return il DTO con i dati, oppure {@code null} se il form non è valido
     */
    public RecipeDTO getRecipeData() {
        if (isInputValid()) {
            // ID 0 per una nuova ricetta
            return new RecipeDTO(0, nameField.getText(), descriptionArea.getText());
        }
        return null;
    }

    /**
     * Controlla che nome e descrizione non siano vuoti; in caso contrario
     * mostra un dialog di avviso.
     *
     * @return {@code true} se i campi sono tutti compilati
     */
    private boolean isInputValid() {
        if (nameField.getText().isBlank() || descriptionArea.getText().isBlank()) {
            appController.showWarningDialog("Attenzione", "Compila tutti i campi!");
            return false;
        }
        return true;
    }

    /**
     * Mostra il dialog modale per creare una nuova ricetta.
     *
     * @param owner la finestra proprietaria del dialog
     * @return un {@link Optional} con il {@link RecipeDTO}, oppure vuoto se
     *         annullato
     * @throws IOException se il file FXML non viene trovato
     */
    public static Optional<RecipeDTO> showDialog(Window owner) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                AddRecipeDialogGUI.class.getResource("/progetto/app/dialog/AddRecipeDialog.fxml"));
        DialogPane dialogPane = loader.load();
        AddRecipeDialogGUI controller = loader.getController();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Crea Nuova Ricetta");
        dialog.initOwner(owner);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.FINISH) {
            return Optional.ofNullable(controller.getRecipeData());
        }
        return Optional.empty();
    }
}
