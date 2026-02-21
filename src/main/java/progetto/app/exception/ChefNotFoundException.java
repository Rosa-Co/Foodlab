package progetto.app.exception;

/**
 * Eccezione lanciata quando non viene trovato nessun
 * {@link progetto.app.model.Chef}
 * corrispondente ai criteri di ricerca (es. ID, email o username).
 */
public class ChefNotFoundException extends RuntimeException {
    /**
     * @param message descrizione del motivo per cui lo chef non è stato trovato
     */
    public ChefNotFoundException(String message) {
        super(message);
    }
}
