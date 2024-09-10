package services;

import accounts.*;
import customers.Customer;
import exception.*;
import transactions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankMethods implements Bank, Serializable {

    private Map<String, Customer> customers = new HashMap<>();
    private List<Transactions> transactionHistory = new ArrayList<>();
    private static final String TRANSACTIONS_FILE = "transactions.txt";
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final long serialVersionUID = 2L; // Use the correct version


    @Override
    public void createAccount(String customerId, String accountHolderName, AccountType accountType) throws Exception {
        Customer customer = customers.get(customerId);
        if (customerId == null || accountHolderName == null) {
            throw new NullValueException("Customer ID or Account Holder Name cannot be null.");
        }
        if (customer == null) {
            throw new InvalidAccountNumberException("Customer not found. Please register first.");
        }

        String accountNumber = generateAccountNumber();
        BankAccount account;

        switch (accountType) {
            case SAVINGS:
                account = new SavingsAccount(accountNumber, accountHolderName);
                break;
            case CHECKING:
                account = new CheckingAccount(accountNumber, accountHolderName);
                break;
            default:
                throw new IllegalArgumentException("Unsupported account type.");
        }

        customer.addAccount(account);
        saveCustomers();
        System.out.println("Account created successfully: " + account);
    }

    @Override
    public void deposit(String accountNumber, double amount) throws Exception {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }
        BankAccount account = findAccount(accountNumber);
        if (account != null) {
            Transactions deposit = new DepositTransaction(account, amount);
            deposit.perform();
            transactionHistory.add(deposit);
            saveTransaction(deposit);
        } else {
            throw new InvalidAccountNumberException("Account not found.");
        }
    }

    @Override
    public void withdraw(String accountNumber, double amount) throws Exception {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }
        BankAccount account = findAccount(accountNumber);
        if (account != null) {
            if (amount > account.getBalance()) {
                throw new InsufficientFundsException("Insufficient funds in the account.");
            }
            Transactions withdrawal = new WithdrawTransaction(account, amount);
            withdrawal.perform();
            transactionHistory.add(withdrawal);
            saveTransaction(withdrawal);
        } else {
            throw new InvalidAccountNumberException("Account not found.");
        }
    }

    @Override
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) throws Exception {
        BankAccount fromAccount = findAccount(fromAccountNumber);
        BankAccount toAccount = findAccount(toAccountNumber);

        if (fromAccount != null && toAccount != null) {
            Transactions transferTransaction = new TransferTransaction(fromAccount, toAccount, amount);
            transferTransaction.perform();
            transactionHistory.add(transferTransaction);
            saveTransaction(transferTransaction);
        } else {
            throw new InvalidAccountNumberException("One or both accounts not found.");
        }
    }

    @Override
    public void viewTransactionList(String accountNumber) {
        BankAccount account = findAccount(accountNumber);
        if (account == null) {
            throw new InvalidAccountNumberException("Account not found.");
        }

        System.out.println("Transaction List for account: " + accountNumber);
        List<String> transactions = account.getTransactionHistory();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
        } else {
            transactions.forEach(System.out::println);
        }
    }

    @Override
    public void viewBalance(String accountNumber) throws Exception {
        BankAccount account = findAccount(accountNumber);
        if (account == null) {
            throw new InvalidAccountNumberException("Account not found.");
        }
        System.out.println("Balance: $" + account.getBalance());
    }

    @Override
    public void registerCustomer(String customerId, String name) {
        try {
            if (validateCustomerId(customerId)) {
                customers.put(customerId, new Customer(customerId, name));
                saveCustomers();
                System.out.println("Customer registered successfully.");
            } else {
                throw new IllegalArgumentException("Invalid customer ID format.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private boolean validateCustomerId(String customerId) {
        String regex = "CMB\\d{6}[A-Z]{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(customerId);
        return matcher.matches();
    }

    private String generateAccountNumber() {
        String base = "52341";
        int randomNumber = 100 + new Random().nextInt(900);
        return base + randomNumber + "00012";
    }

    private BankAccount findAccount(String accountNumber) {
        return customers.values().stream()
                .flatMap(customer -> customer.getAccounts().stream())
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }

    private void saveCustomers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(CUSTOMERS_FILE)))) {
            oos.writeObject(customers);
            System.out.println("Customer data saved.");
        } catch (IOException e) {
            System.out.println("Failed to save customer data: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(CUSTOMERS_FILE)))) {
            customers = (Map<String, Customer>) ois.readObject();
            System.out.println("Customer data loaded.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing customer data found. Starting fresh.");
        }
    }

    private void saveTransaction(Transactions transaction) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(TRANSACTIONS_FILE, true);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(transaction);
            System.out.println("Transaction data saved.");
        } catch (IOException e) {
            System.out.println("Failed to save transaction data: " + e.getMessage());
        }
    }

    public void loadTransactionHistory() {
//        List<Transactions> tempTransactionHistory = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(TRANSACTIONS_FILE)))) {

while (true) {

    try {
        Transactions transaction = (Transactions) ois.readObject();
        transactionHistory.add(transaction);
    } catch (ClassNotFoundException e) {
        System.out.println("error");
    }


    // Replace the old transaction history with the newly loaded on
    System.out.println("Transaction history loaded.");

    // Display transactions using Stream API
    System.out.println("Deserialized Transactions:");
    transactionHistory.forEach(System.out::println);

}
        } catch (Exception e) {
            System.out.println("Error loading transaction history: " + e.getMessage());

        }
    }
}