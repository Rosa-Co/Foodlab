package progetto.app.dialog;

import atlantafx.base.theme.Styles;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;

public abstract class BaseAlertDialog {

    private Alert alert;

    protected BaseAlertDialog() {
        alert = new Alert(Alert.AlertType.NONE);
        alert.getButtonTypes().add(ButtonType.OK);
        styleDialog();
    }

    private void styleDialog() {
        DialogPane dialogPane = alert.getDialogPane();

        // Applica le classi di stile AtlantaFX
        dialogPane.getStyleClass().add(Styles.ROUNDED);

        // Rendi il dialog più grande e leggibile
        dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        dialogPane.setPrefWidth(450);

        // Stile per i bottoni
        dialogPane.lookupButton(ButtonType.OK).getStyleClass().addAll(
                Styles.ACCENT,
                Styles.ROUNDED
        );
    }

    /** Metodo per impostare un'icona personalizzata */
    protected void setCustomIcon(Node icon) {
        alert.setGraphic(icon);
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

    public Alert getAlert() {
        return alert;
    }
}
