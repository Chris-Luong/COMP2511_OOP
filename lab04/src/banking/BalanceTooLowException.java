package banking;

public class BalanceTooLowException extends Exception{
    public BalanceTooLowException(String message) {
        super(message);
    }
}
