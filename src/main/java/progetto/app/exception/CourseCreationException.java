package progetto.app.exception;

/**
 * Eccezione base per i fallimenti durante la creazione di un corso.
 * <p>
 * Le sottoclassi specializzano il tipo di errore (es.
 * {@link DuplicateCorsoException}
 * per un titolo già esistente).
 * </p>
 */
public class CourseCreationException extends RuntimeException {
    /**
     * @param message descrizione del problema riscontrato durante la creazione del
     *                corso
     */
    public CourseCreationException(String message) {
        super(message);
    }
}
