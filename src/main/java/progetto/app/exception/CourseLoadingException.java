package progetto.app.exception;

public class CourseLoadingException extends Exception {
    public CourseLoadingException(String message) {
        super(message);
    }

    public CourseLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
