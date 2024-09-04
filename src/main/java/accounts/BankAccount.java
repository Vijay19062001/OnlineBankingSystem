package bank.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public abstract class BankAccount implements Serializable {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private List<String> transactionHistory;

    public BankAccount(String accountNumber, String accountHolderName) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            transactionHistory.add("Deposited: $" + amount);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            transactionHistory.add("Withdrew: $" + amount);
            return true;
        }
        return false;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    @Override
    public String toString() {
        return "AccountNumber: " + accountNumber + ", AccountHolder: " + accountHolderName + ", Balance: $" + balance;
    }


}

