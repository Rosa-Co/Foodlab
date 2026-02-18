package progetto.app.exception;

public class DuplicateCorsoException extends CourseCreationException {
    public DuplicateCorsoException(String message) {
        super(message);
    }
}
