package progetto.app.exception;

/**
 * Eccezione lanciata quando un valore testuale non rispetta
 * i limiti di lunghezza previsti dalla validazione.
 */
public class LengthException extends RuntimeException {
    /**
     * @param message descrizione del vincolo di lunghezza violato
     */
    public LengthException(String message) {
        super(message);
    }
}
