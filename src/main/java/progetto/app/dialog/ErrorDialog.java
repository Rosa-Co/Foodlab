package progetto.app.dialog;

import javafx.scene.control.Alert;


public class ErrorDialog extends BaseAlertDialog {
    public ErrorDialog(String header, String content) {
        super();
        getAlert().setAlertType(Alert.AlertType.ERROR);
        setTitleAndHeader("Errore", header);
        setContent(content);
    }

    @Override
    public ErrorDialog show() {
        getAlert().showAndWait();
        return this;
    }
}
