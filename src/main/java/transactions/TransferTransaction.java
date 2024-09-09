package transactions;

import exception.*;
import accounts.BankAccount;

public class TransferTransaction implements Transaction {
    private BankAccount fromAccount;
    private BankAccount toAccount;
    private double amount;


        public TransferTransaction(BankAccount fromAccount, BankAccount toAccount, double amount) {
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
            this.amount = amount;
        }

    public void perform() {

        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be positive.");
        }

        if (fromAccount.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds in the account: " + fromAccount.getAccountNumber());
        }

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        fromAccount.addTransaction(String.format("Transfer of $%s to account %s", amount, toAccount.getAccountNumber()));
        toAccount.addTransaction("Transfer of $" + amount + " from account " + fromAccount.getAccountNumber());

        System.out.println("Transfer of $" + amount + " successful.");
    }

    @Override
    public void reverse() {

        if (toAccount.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds in the destination account to reverse the transfer.");
        }

        toAccount.withdraw(amount);
        fromAccount.deposit(amount);

        fromAccount.addTransaction("Transfer reversal of $" + amount + " from account " + toAccount.getAccountNumber());
        toAccount.addTransaction("Transfer reversal of $" + amount + " to account " + fromAccount.getAccountNumber());

        System.out.println("Transfer reversed: $" + amount + " returned to sender.");
    }


    @Override
    public String getAccountNumber() {

        return fromAccount.getAccountNumber() + " & " + toAccount.getAccountNumber();
    }

    @Override
    public String getDetails() {
        return "Transfer of $" + amount + " from account " + fromAccount.getAccountNumber() + " to account " + toAccount.getAccountNumber();
    }

}