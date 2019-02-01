package hello.storage;

public class StorageResolverException extends RuntimeException {

    public StorageResolverException(String message) {
        super(message);
    }

    public StorageResolverException(String message, Throwable cause) {
        super(message, cause);
    }
}
