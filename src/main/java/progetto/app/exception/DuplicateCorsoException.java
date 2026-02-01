package progetto.app.exception;

public class DuplicateCorsoException extends RuntimeException {
    public DuplicateCorsoException(String message) {
        super(message);
    }
}
