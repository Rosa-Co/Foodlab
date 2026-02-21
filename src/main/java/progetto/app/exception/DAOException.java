package progetto.app.exception;

/**
 * Eccezione generica per i fallimenti nel layer di accesso ai dati (DAO).
 * <p>
 * Incapsula errori di basso livello (es. {@link java.sql.SQLException}) in
 * un'eccezione di dominio non controllata. I metodi DAO la lanciano ogni volta
 * che un'operazione sul database non può essere completata per ragioni non
 * coperte da eccezioni più specifiche.
 * </p>
 */
public class DAOException extends RuntimeException {
    /**
     * @param message descrizione dell'errore di accesso al database
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * @param message descrizione dell'errore di accesso al database
     * @param cause   eccezione originale che ha causato il fallimento
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
