package transactions;

import accounts.BankAccount;

public class DepositTransaction extends Transactions implements Transaction{

    public DepositTransaction(BankAccount account, double amount) {
        super(account, amount);
    }

    @Override
    public void perform() throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        account.deposit(amount);
        account.addTransaction("Deposit of $" + amount);
        System.out.println("Deposit of $" + amount + " successful.");
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
}
