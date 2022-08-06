package unsw.Exceptions;

public class UNSWFileAlreadyExistsException extends java.nio.file.FileAlreadyExistsException{
    public UNSWFileAlreadyExistsException(String message) {
        super(message);
    }
}
