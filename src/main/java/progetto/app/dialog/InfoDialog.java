package progetto.app.dialog;

import javafx.scene.control.Alert;

public class InfoDialog extends BaseAlertDialog{
    public InfoDialog(String header, String content) {
        super();
        getAlert().setAlertType(Alert.AlertType.INFORMATION);
        setTitleAndHeader("Informazione", header);
        setContent(content);
    }

    @Override
    public InfoDialog show() {
        getAlert().showAndWait();
        return this;
    }
}
