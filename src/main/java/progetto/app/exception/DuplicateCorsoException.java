package progetto.app.exception;

/**
 * Eccezione lanciata quando si tenta di creare un corso con un titolo
 * già presente nel database.
 * <p>
 * Estende {@link CourseCreationException} ed è tipicamente convertita da una
 * violazione UNIQUE del database ({@code SQL State 23505}).
 * </p>
 */
public class DuplicateCorsoException extends CourseCreationException {
    /**
     * @param message descrizione del conflitto (es. titolo del corso già esistente)
     */
    public DuplicateCorsoException(String message) {
        super(message);
    }
}
