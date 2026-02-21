package progetto.app.exception;

/**
 * Eccezione lanciata quando si tenta di registrare un
 * {@link progetto.app.model.Chef}
 * con un username o un'email già presenti nel database.
 */
public class DuplicateChefException extends RuntimeException {
    /**
     * @param message descrizione del conflitto rilevato (es. campo duplicato)
     */
    public DuplicateChefException(String message) {
        super(message);
    }
}
