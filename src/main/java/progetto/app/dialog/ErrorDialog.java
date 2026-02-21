package progetto.app.dialog;

import atlantafx.base.theme.Styles;
import javafx.scene.control.Alert;

/**
 * Dialog di errore che mostra un messaggio con severità {@code ERROR}.
 * <p>
 * Applica automaticamente lo stile {@code DANGER} di AtlantaFX al pannello
 * e imposta il titolo fisso a "Errore".
 * </p>
 */
public class ErrorDialog extends BaseAlertDialog {
    /**
     * Crea un dialog di errore con l'header e il contenuto specificati.
     *
     * @param header  il testo dell'header del dialog (es. descrizione breve
     *                dell'errore)
     * @param content il testo del corpo del dialog (es. dettaglio o suggerimento)
     */
    public ErrorDialog(String header, String content) {
        super();
        getAlert().setAlertType(Alert.AlertType.ERROR);
        setTitleAndHeader("Errore", header);
        setContent(content);

        getAlert().getDialogPane().getStyleClass().add(Styles.DANGER);
    }

    /**
     * Mostra il dialog all'utente in modalità bloccante e attende la chiusura.
     *
     * @return l'istanza corrente del dialog
     */
    @Override
    public ErrorDialog show() {
        getAlert().showAndWait();
        return this;
    }
}
