1. The code is consistent with the preconditions and postconditions because if statements
and assert statements placed in the functions pass without throwing errors.
2. Balance >=0 is invariant for both classes because bank accounts of any type cannot have
a negative balance.
3. Yes, because LoggedBankAccount simply uses everything from BankAccount with the added
functionality of adding strings to a List that acts as the log of all deposits/withdrawals.
Thus BankAccounts can be replaced with LoggedBankAccounts without changing the functionality
of it.