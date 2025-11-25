package progetto.app.exception;

public class CourseCreationException extends Exception {
    public CourseCreationException(String message) {
        super(message);
    }

    public CourseCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
