package progetto.app.exception;

/**
 * Eccezione lanciata quando il numero di sessioni pianificate non è compatibile
 * con la frequenza specificata per il corso.
 */
public class FrequencyException extends RuntimeException {
    /**
     * @param message descrizione dell'inconsistenza tra frequenza e numero di
     *                sessioni
     */
    public FrequencyException(String message) {
        super(message);
    }
}
