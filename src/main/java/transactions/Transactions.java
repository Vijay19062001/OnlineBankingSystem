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
    public void getAccount() {
    }
    public void getAccountNumber(){

    }

    public String toString() {
        return "Transaction of Rs:" + amount + " for account " +  account.getAccountNumber() ;
    }


}
