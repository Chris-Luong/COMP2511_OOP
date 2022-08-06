package banking;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A bank account with all actions logged
 * @author Christopher Luong
 */
public class LoggedBankAccount extends BankAccount {
    private List<String> log;
    private LocalDate date;
    
    public LoggedBankAccount() {
        log = new ArrayList<>();
        // The time of the action is used to differentiate them in the log
        date = LocalDate.now();
    }

    @Override
    public void deposit(double amount) throws InvalidInputException {
        super.deposit(amount);
        // Log the necessary details
        log.add(date + " Deposited: " + amount + " Current Balance = " + getBalance());
    }

    @Override
    public void withdraw(double amount) throws InvalidInputException, BalanceTooLowException {
        super.withdraw(amount);
        // Log the necessary details
        log.add(date + " Withdrew: " + amount + " Current Balance = " + getBalance());
    }

    public List<String> getLogs() {
        return log;
    }
    
}
