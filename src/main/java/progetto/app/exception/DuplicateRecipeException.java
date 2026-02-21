package progetto.app.exception;

/**
 * Eccezione lanciata quando si tenta di aggiungere una ricetta
 * già esistente nel database.
 */
public class DuplicateRecipeException extends RuntimeException {
    /**
     * @param message descrizione del conflitto rilevato
     */
    public DuplicateRecipeException(String message) {
        super(message);
    }
}
