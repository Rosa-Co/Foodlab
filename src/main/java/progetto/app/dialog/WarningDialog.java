package progetto.app.dialog;

import javafx.scene.control.Alert;

/**
 * Dialog di avviso che mostra un messaggio con severità {@code WARNING}.
 * <p>
 * Imposta automaticamente il titolo fisso a "Attenzione".
 * </p>
 */
public class WarningDialog extends BaseAlertDialog {
    /**
     * Crea un dialog di avviso con l'header e il contenuto specificati.
     *
     * @param header  il testo dell'header del dialog
     * @param content il testo del corpo del dialog
     */
    public WarningDialog(String header, String content) {
        super();
        getAlert().setAlertType(Alert.AlertType.WARNING);
        setTitleAndHeader("Attenzione", header);
        setContent(content);
    }

    /**
     * Mostra il dialog all'utente in modalità bloccante e attende la chiusura.
     *
     * @return l'istanza corrente del dialog
     */
    @Override
    public WarningDialog show() {
        getAlert().showAndWait();
        return this;
    }
}
