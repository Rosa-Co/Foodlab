package progetto.app.dialog;

import atlantafx.base.theme.Styles;
import javafx.scene.control.Alert;

/**
 * Dialog informativo che mostra un messaggio con severità {@code INFORMATION}.
 * <p>
 * Applica automaticamente lo stile {@code SUCCESS} di AtlantaFX al pannello
 * e imposta il titolo fisso a "Informazione".
 * </p>
 */
public class InfoDialog extends BaseAlertDialog {
    /**
     * Crea un dialog informativo con l'header e il contenuto specificati.
     *
     * @param header  il testo dell'header del dialog
     * @param content il testo del corpo del dialog
     */
    public InfoDialog(String header, String content) {
        super();
        getAlert().setAlertType(Alert.AlertType.INFORMATION);
        setTitleAndHeader("Informazione", header);
        setContent(content);

        getAlert().getDialogPane().getStyleClass().add(Styles.SUCCESS);
    }

    /**
     * Mostra il dialog all'utente in modalità bloccante e attende la chiusura.
     *
     * @return l'istanza corrente del dialog
     */
    @Override
    public InfoDialog show() {
        getAlert().showAndWait();
        return this;
    }
}
