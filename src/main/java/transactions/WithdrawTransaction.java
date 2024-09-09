package transactions;

import exception.*;
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

        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }


        if (account.withdraw(amount)) {
            account.addTransaction("Withdraw of $" + amount);
            System.out.println("Withdrawal of $" + amount + " successful.");
        } else {

            throw new InsufficientFundsException("Insufficient funds in the account.");
        }
    }

    @Override
    public void reverse() throws Exception {

        account.deposit(amount);
        account.addTransaction("Reversal of withdrawal of $" + amount);
        System.out.println("Withdrawal reversed: $" + amount + " deposited back.");
    }

    @Override
    public String getAccountNumber() {
        return account.getAccountNumber();
    }

    @Override
    public String getDetails() {
        return "Withdrawal of $" + amount + " from account " + account.getAccountNumber();
    }


}

