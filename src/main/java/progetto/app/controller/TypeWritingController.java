package progetto.app.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Controller per l'animazione di scrittura progressiva (typewriter effect) su
 * un {@link Label} JavaFX.
 * <p>
 * Aggiunge i caratteri del testo fornito uno alla volta al label specificato,
 * con un intervallo di millisecondi configurabile tra un carattere e l'altro.
 * Viene utilizzato per animare messaggi nella schermata di splash o in altre
 * viste
 * dell'applicazione Foodlab.
 * </p>
 *
 * <p>
 * Esempio d'uso:
 * </p>
 * 
 * <pre>{@code
 * TypeWritingController tw = new TypeWritingController(myLabel, "Benvenuto in Foodlab!", 50);
 * tw.play();
 * }</pre>
 */
public class TypeWritingController {

    /** Il {@link Label} JavaFX sul quale viene eseguita l'animazione. */
    private final Label label;

    /** Il testo completo da scrivere carattere per carattere sul label. */
    private final String fullText;

    /**
     * Intervallo in millisecondi tra la comparsa di un carattere e il successivo.
     */
    private final int speedMillis;

    /** Indice del carattere correntemente in fase di scrittura. */
    private int currentIndex = 0;

    /**
     * Costruisce un nuovo {@code TypeWritingController}.
     *
     * @param label       il {@link Label} JavaFX su cui verrà visualizzata
     *                    l'animazione
     * @param fullText    il testo completo da mostrare carattere per carattere
     * @param speedMillis l'intervallo in millisecondi tra l'aggiunta di un
     *                    carattere e il successivo
     */
    public TypeWritingController(Label label, String fullText, int speedMillis) {
        this.label = label;
        this.fullText = fullText;
        this.speedMillis = speedMillis;
    }

    /**
     * Avvia l'animazione typewriter.
     * <p>
     * Azzera il contenuto del label, lo rende visibile e inizia ad aggiungere
     * un carattere alla volta di {@link #fullText}, con la cadenza definita da
     * {@link #speedMillis}.
     * L'animazione si conclude automaticamente quando tutti i caratteri sono stati
     * aggiunti.
     * </p>
     */
    public void play() {
        label.setText("");
        label.setVisible(true);
        currentIndex = 0;

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(speedMillis), event -> {
            if (currentIndex < fullText.length()) {
                label.setText(label.getText() + fullText.charAt(currentIndex));
                currentIndex++;
            }
        }));
        timeline.setCycleCount(fullText.length()); // ripete animation per quanti sono i caratteri
        timeline.play();
    }
}
