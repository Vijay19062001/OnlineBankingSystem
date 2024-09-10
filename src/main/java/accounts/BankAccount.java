package accounts;

import transactions.Transactions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BankAccount implements Serializable {


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
            this.balance = balance + amount;
            addTransaction("Deposited: $" + amount + " | Balance: $" + balance);

        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance = balance - amount;
            addTransaction("Withdrawn: $" + amount + " | Balance: $" + balance);
            return true;
        }
        return false;
    }
    public void addTransaction(String transactionDetail) {
        transactionHistory.add(transactionDetail);
    }


    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    @Override
    public String toString() {
        return "AccountNumber: " + accountNumber + ", AccountHolder: " + accountHolderName + ", Balance: $" + balance;
    }


}

