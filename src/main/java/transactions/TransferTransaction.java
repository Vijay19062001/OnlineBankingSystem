package transactions;

import accounts.BankAccount;
import exception.InvalidAmountException;
import exception.InsufficientFundsException;

public class TransferTransaction extends Transactions {

    public TransferTransaction(BankAccount fromAccount, BankAccount toAccount, double amount) {
        super(fromAccount, amount);
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    @Override
    public void perform() throws Exception {
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive.");
        }
        if (fromAccount.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds in the account: " + fromAccount.getAccountNumber());
        }
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
        fromAccount.addTransaction("Transfer of $" + amount + " to account " + toAccount.getAccountNumber());
        toAccount.addTransaction("Transfer of $" + amount + " from account " + fromAccount.getAccountNumber());
        System.out.println("Transfer of $" + amount + " successful.");
    }

    @Override
    public void reverse() throws Exception {
        if (toAccount.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds in the destination account to reverse the transfer.");
        }
        toAccount.withdraw(amount);
        fromAccount.deposit(amount);
        fromAccount.addTransaction("Transfer reversal of $" + amount + " from account " + toAccount.getAccountNumber());
        toAccount.addTransaction("Transfer reversal of $" + amount + " to account " + fromAccount.getAccountNumber());
        System.out.println("Transfer reversed: $" + amount + " returned to sender.");
    }
}
