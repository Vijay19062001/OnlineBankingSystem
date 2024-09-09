package services;

import accounts.AccountType;
import transactions.Transaction;


import java.io.IOException;
import java.io.Serializable;

public interface Bank  extends Serializable {


    void createAccount(String customerId, String accountHolderName, AccountType accountType) throws Exception ;
    void deposit(String accountNumber, double amount) throws Exception;
    void withdraw(String accountNumber, double amount) throws Exception;
    void transfer(String fromAccountNumber, String toAccountNumber,double transferAmount);
    void viewBalance(String accountNumber) throws Exception;
    void registerCustomer(String customerId, String name)throws IllegalArgumentException;
    void viewTransactionList(String customerId)throws Exception;
    void saveTransaction(Transaction transaction) throws IOException;




}
