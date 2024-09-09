package transactions;


import accounts.BankAccount;
import services.BankMethods;

import java.io.IOException;

public abstract class DepositTransaction implements Transaction {
    private BankAccount account;
    private double amount;

    public DepositTransaction(BankAccount account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void perform() throws IllegalArgumentException {

        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        account.deposit(amount);
        account.addTransaction("Deposit of $" + amount);
        System.out.println("Deposit of $" + amount + " successful.");

        try {
            BankMethods bank = new BankMethods();
            bank.saveTransaction(this);  // Save the transaction
        } catch (IOException e) {
            System.out.println("Failed to save transaction: " + e.getMessage());
        }
    }


    @Override
    public void reverse() throws Exception {

        if (account.getBalance() < amount) {
            throw new Exception("Insufficient funds to reverse the deposit of $" + amount);
        }

        account.withdraw(amount);
        account.addTransaction("Reversal of deposit of $" + amount);
        System.out.println("Deposit reversed: $" + amount + " withdrawn.");
    }

    @Override
    public String getAccountNumber() {
        return account.getAccountNumber();
    }

    @Override
    public String toString() {
        return "Deposit of $" + amount + " to account " + account.getAccountNumber();
    }

}

