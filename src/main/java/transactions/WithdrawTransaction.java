package transactions;

import accounts.BankAccount;
import exception.InsufficientFundsException;

public class WithdrawTransaction implements Transaction {
    private BankAccount account;
    private double amount;

    public WithdrawTransaction(BankAccount account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void perform() throws Exception {
        if (account.withdraw(amount)) {
            System.out.println("Withdrawal of $" + amount + " successful.");
        } else {
            throw new InsufficientFundsException("Insufficient funds in the account.");
        }
    }

    @Override
    public void reverse() throws Exception {
        account.deposit(amount);
        System.out.println("Withdrawal reversed: $" + amount + " deposited back.");
    }
}

