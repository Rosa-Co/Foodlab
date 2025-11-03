package progetto.app.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public abstract class BaseAlertDialog {

    private Alert alert;

    protected BaseAlertDialog() {
        alert = new Alert(Alert.AlertType.NONE);
        alert.getButtonTypes().add(ButtonType.OK);
    }

    /** Mostra l'alert e ritorna il pulsante premuto */
    abstract public BaseAlertDialog show();

    /** Setta titolo e contenuto generico */
    public void setTitleAndHeader(String title, String headerText) {
        alert.setTitle(title);
        alert.setHeaderText(headerText);
    }

    public void setContent(String content) {
        alert.setContentText(content);
    }

    Alert getAlert() {
        return alert;
    }
}
