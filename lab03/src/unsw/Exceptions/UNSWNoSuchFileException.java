package unsw.Exceptions;

public class UNSWNoSuchFileException extends java.nio.file.NoSuchFileException{
    public UNSWNoSuchFileException(String message) {
        super(message);
    }
}
