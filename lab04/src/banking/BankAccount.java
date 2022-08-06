package banking;

/**
 * A bank account
 * @author Christopher Luong
 */
public class BankAccount {

    private double balance;

    public BankAccount() {
        this.balance = 0;
    }

    public double getBalance() {
        return balance;
    }
 
    /**
     * Depositing into account
     * @precondition deposit amount > 0
     * @postcondition balance should increase
     * @param amount the amount to deposit
     */
    public void deposit(double amount) throws InvalidInputException {
        // Precondition check
        if (!isValidAmount(amount, false)) {
            throw new InvalidInputException("Invalid input. Example of valid input: 12.90");
        }
        double originalBalance = this.balance;
        balance += amount;

        // Postcondition check
        assert(balance > originalBalance);
    }

    /**
     * Withdrawing into account
     * @precondition withdrawal amount > 0
     * @postcondition balance >= 0
     * @param amount the amount to withdraw
     */
    public void withdraw(double amount) throws InvalidInputException, BalanceTooLowException {
        // Precondition check
        if (!isValidAmount(amount, false)) {
            throw new InvalidInputException("Invalid input. Example of valid input: 10.10");
        }
        if (amount > balance) {
            throw new BalanceTooLowException("Please enter a valid amount. Withdrawal amount exceeds balance in account.");
        }
        balance -= amount;

        // Postcondition check
        assert isValidAmount(balance, true);
    }

    /**
     * Checks if the amount is valid (greater than 0) as a
     * precondition.
     * @param amount
     * @return boolean
     */
    public boolean isValidAmount(double amount, boolean isBalance) {
        if (isBalance) {
            return amount >= 0;
        }

        return amount > 0;
    }
}