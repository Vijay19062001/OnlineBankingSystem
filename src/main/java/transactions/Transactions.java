package transactions;

import accounts.BankAccount;
import java.io.Serializable;

public abstract class Transactions implements Serializable, Transaction {


    protected BankAccount account;
    protected double amount;
    protected BankAccount fromAccount;
    protected BankAccount toAccount;

    public Transactions(BankAccount account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public BankAccount getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(BankAccount fromAccount) {
        this.fromAccount = fromAccount;
    }

    public BankAccount getToAccount() {
        return toAccount;
    }

    public void setToAccount(BankAccount toAccount) {
        this.toAccount = toAccount;
    }

    @Override
    public String toString() {
        return "Transaction of $" + amount + " for account " + (account != null ? account.getAccountNumber() : "N/A");
    }
}
