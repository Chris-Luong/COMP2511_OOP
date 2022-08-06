package banking;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankTest {
    @Test
    public void testBankAccount() {
        BankAccount account = new BankAccount();
        assertDoesNotThrow(() -> {
            account.deposit(300);
            account.withdraw(300);
            account.deposit(1000);
            account.withdraw(200);
        });
    }

    @Test
    public void testInvalidInputException() {
        BankAccount account = new BankAccount();
        assertThrows(InvalidInputException.class, () -> {
            account.deposit(200);
            account.withdraw(-10);
        });
    }

    @Test
    public void testBalanceException() {
        BankAccount account = new BankAccount();
        assertThrows(BalanceTooLowException.class, () -> {
            account.deposit(200);
            account.withdraw(200.01);
        });
    }

    @Test
    public void testLoggedBankAccount() {
        LoggedBankAccount account = new LoggedBankAccount();
        try {
            account.deposit(500);
            account.withdraw(250);
            account.withdraw(250);
            account.withdraw(-200);
        } catch (InvalidInputException | BalanceTooLowException Exception) {
            System.out.println("Invalid Input");
        }
        List<String> logs = account.getLogs();
        for (String log : logs) {
            System.out.println(log);
        }
    }
}  