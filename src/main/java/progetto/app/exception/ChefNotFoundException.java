package progetto.app.exception;

public class ChefNotFoundException extends RuntimeException {
    public ChefNotFoundException(String message) {
        super(message);
    }
}
