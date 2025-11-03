package progetto.app.exception;

public class DAOException extends RuntimeException {
    public DAOException() {
        super();
    }
    public DAOException(String message) {
        super(message);
    }
}
