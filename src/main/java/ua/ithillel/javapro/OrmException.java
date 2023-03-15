package ua.ithillel.javapro;

public class OrmException extends RuntimeException {
    public OrmException() {
    }

    public OrmException(String message) {
        super(message);
    }

    public OrmException(String message, Throwable cause) {
        super(message, cause);
    }
}
