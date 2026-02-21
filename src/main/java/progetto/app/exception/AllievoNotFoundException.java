package progetto.app.exception;

/**
 * Eccezione lanciata quando non viene trovato nessun
 * {@link progetto.app.model.Allievo}
 * corrispondente ai criteri di ricerca (es. ID, email o username).
 */
public class AllievoNotFoundException extends RuntimeException {
    /**
     * @param message descrizione del motivo per cui l'allievo non è stato trovato
     */
    public AllievoNotFoundException(String message) {
        super(message);
    }
}
