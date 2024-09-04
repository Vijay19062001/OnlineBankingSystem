package transactions;


import accounts.BankAccount;

public class DepositTransaction implements Transaction {
    private BankAccount account;
    private double amount;

    public DepositTransaction(BankAccount account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void perform()throws IllegalArgumentException{
        account.deposit(amount);
        System.out.println("Deposit of $" + amount + " successful.");
    }

    @Override
    public void reverse() throws Exception {
        account.withdraw(amount);
        System.out.println("Deposit reversed: $" + amount + " withdrawn.");
    }
}

