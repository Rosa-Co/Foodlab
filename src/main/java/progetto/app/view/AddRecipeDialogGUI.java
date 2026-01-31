package progetto.app.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Window;
import progetto.app.controller.AppController;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddRecipeDialogGUI implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionArea;

    private final AppController appController = AppController.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Map<String, Object> getRecipeData() {
        if (isInputValid()) {
            Map<String, Object> data = new HashMap<>();
            data.put("nome", nameField.getText());
            data.put("descrizione", descriptionArea.getText());
            return data;
        }
        return null;
    }

    private boolean isInputValid() {
        if (nameField.getText().isBlank() || descriptionArea.getText().isBlank()) {
            appController.showWarningDialog("Attenzione", "Compila tutti i campi!");
            return false;
        }
        return true;
    }

    public static Optional<Map<String, Object>> showDialog(Window owner) throws IOException {
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
