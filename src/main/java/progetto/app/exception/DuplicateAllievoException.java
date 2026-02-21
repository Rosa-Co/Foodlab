package progetto.app.exception;

/**
 * Eccezione lanciata quando si tenta di registrare un
 * {@link progetto.app.model.Allievo}
 * con un username o un'email già presenti nel database.
 */
public class DuplicateAllievoException extends RuntimeException {
    /**
     * @param message descrizione del conflitto rilevato (es. campo duplicato)
     */
    public DuplicateAllievoException(String message) {
        super(message);
    }
}
