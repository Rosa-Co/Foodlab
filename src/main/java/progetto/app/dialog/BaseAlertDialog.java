package progetto.app.dialog;

import atlantafx.base.theme.Styles;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;

/**
 * Classe base astratta per i dialog di tipo alert dell'applicazione.
 * <p>
 * Incapsula un {@link javafx.scene.control.Alert} JavaFX e ne definisce
 * lo stile comune (bordi arrotondati tramite AtlantaFX, larghezza fissa,
 * pulsante OK con stile accent). Le sottoclassi specializzano il tipo
 * di alert ({@code ERROR}, {@code WARNING}, {@code INFORMATION}) e
 * implementano il metodo astratto {@link #show()}.
 * </p>
 */
public abstract class BaseAlertDialog {

    /** L'alert JavaFX sottostante gestito da questa classe. */
    private Alert alert;

    /**
     * Inizializza l'alert con tipo {@code NONE}, aggiunge il pulsante OK
     * e applica lo stile comune tramite {@link #styleDialog()}.
     */
    protected BaseAlertDialog() {
        alert = new Alert(Alert.AlertType.NONE);
        alert.getButtonTypes().add(ButtonType.OK);
        styleDialog();
    }

    /**
     * Applica lo stile grafico condiviso al {@link DialogPane} dell'alert:
     * bordi arrotondati (AtlantaFX {@code ROUNDED}), larghezza preferita di 450px
     * e stile accent + rounded sul pulsante OK.
     */
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
                Styles.ROUNDED);
    }

    /**
     * Imposta un nodo grafico personalizzato come icona del dialog.
     *
     * @param icon il {@link javafx.scene.Node} da usare come grafico
     */
    protected void setCustomIcon(Node icon) {
        alert.setGraphic(icon);
    }

    /**
     * Mostra il dialog all'utente.
     *
     * @return l'istanza corrente del dialog (per facilitare il method chaining)
     */
    abstract public BaseAlertDialog show();

    /**
     * Imposta titolo e header text del dialog.
     *
     * @param title      il titolo della finestra del dialog
     * @param headerText il testo dell'header (sottotitolo visibile nell'alert)
     */
    public void setTitleAndHeader(String title, String headerText) {
        alert.setTitle(title);
        alert.setHeaderText(headerText);
    }

    /**
     * Imposta il testo del corpo del dialog.
     *
     * @param content il testo descrittivo da mostrare nel corpo dell'alert
     */
    public void setContent(String content) {
        alert.setContentText(content);
    }

    /**
     * Restituisce l'istanza dell'alert JavaFX sottostante.
     * <p>
     * Usato dalle sottoclassi per personalizzare tipo e stile dell'alert.
     * </p>
     *
     * @return l'oggetto {@link Alert} gestito da questa classe
     */
    public Alert getAlert() {
        return alert;
    }
}
