package services;

import accounts.AccountType;
import transactions.Transaction;


import java.io.IOException;


public interface Bank  {

    void createAccount(String customerId, String accountHolderName, AccountType accountType) throws Exception ;
    void deposit(String accountNumber, double amount) throws Exception;
    void withdraw(String accountNumber, double amount) throws Exception;
    void transfer(String fromAccountNumber, String toAccountNumber,double transferAmount) throws Exception;
    void viewBalance(String accountNumber) throws Exception;
    void registerCustomer(String customerId, String name)throws IllegalArgumentException;
    void viewTransactionList(String customerId)throws Exception;

//    void loadTransactionHistory();
}
