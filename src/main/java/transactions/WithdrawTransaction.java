package transactions;

import accounts.BankAccount;
import exception.InvalidAmountException;
import exception.InsufficientFundsException;

public class WithdrawTransaction extends Transactions {

    public WithdrawTransaction(BankAccount account, double amount) {
        super(account, amount);
    }

    @Override
    public void perform() throws Exception {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }
        if (!account.withdraw(amount)) {
            throw new InsufficientFundsException("Insufficient funds in the account.");
        }
        account.addTransaction("Withdraw of $" + amount);
        System.out.println("Withdrawal of $" + amount + " successful.");
    }

    @Override
    public void reverse() throws Exception {
        account.deposit(amount);
        account.addTransaction("Reversal of withdrawal of $" + amount);
        System.out.println("Withdrawal reversed: $" + amount + " deposited back.");
    }
}
