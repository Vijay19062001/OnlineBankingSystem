package services;

import accounts.*;
import customers.Customer;
import exception.*;

import transactions.*;

import java.io.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BankMethods implements Bank {
    private static final String TRANSACTIONS_FILE = "transactions.txt";
    private Map<String, Customer> customers = new HashMap<>();
    private List<Transaction> transactionHistory=new ArrayList<>();
    private static final String CUSTOMERS_FILE = "customers.txt";


    public BankMethods() {
        loadTransactionHistory();
        loadCustomers();

    }



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
            try {
                Transaction deposit = new DepositTransaction(account, amount) {
                    @Override
                    public String getDetails() throws Exception {
                        return "";
                    }
                };
                deposit.perform();
                transactionHistory.add(deposit);
                saveTransaction(deposit);
            } catch (Exception e) {
                System.out.println("Transaction failed: " + e.getMessage());
            }

        } else {
            throw new InvalidAccountNumberException("Account is not found.");
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
            Transaction withdrawal = new WithdrawTransaction(account, amount);
            withdrawal.perform();
            transactionHistory.add(withdrawal);
            saveTransaction(withdrawal);
        } else {
            throw new InvalidAccountNumberException("Account is not a found.");
        }
    }


    @Override
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        try {
            BankAccount fromAccount = findAccountByNumber(fromAccountNumber);
            BankAccount toAccount = findAccountByNumber(toAccountNumber);

            if (fromAccount != null && toAccount != null) {
                Transaction transferTransaction = new TransferTransaction(fromAccount, toAccount, amount);
                transferTransaction.perform();
                transactionHistory.add(transferTransaction);
                saveTransaction(transferTransaction);
            } else {
                throw new InvalidAccountNumberException("One or both accounts not found.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    @Override
    public void viewTransactionList(String accountNumber) {
        BankAccount account = findAccount(accountNumber);
        if (account == null) {
            throw new InvalidAccountNumberException("Account not found.");
        }

        System.out.println("Transaction List for account: " + accountNumber);

        account.getTransactionHistory()
                .stream().forEach(System.out::println);

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
        return base + randomNumber + "00012";  // Ensure last 5 digits are always 0012
    }


    private BankAccount findAccount(String accountNumber) {
        for (Customer customer : customers.values()) {
            for (BankAccount account : customer.getAccounts()) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    return account;
                }
            }
        }
        return null;
    }

    private BankAccount findAccountByNumber(String accountNumber) {
        return findAccount(accountNumber);
    }



    private void saveCustomers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CUSTOMERS_FILE))) {
            oos.writeObject(customers);
            System.out.println("Customer data saved.");
        } catch (IOException e) {
            System.out.println("Failed to save customer data: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CUSTOMERS_FILE))) {
            customers = (Map<String, Customer>) ois.readObject();
            System.out.println("Customer data loaded.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing customer data found. Starting fresh.");
        }
    }



    public void saveTransaction(Transaction transaction) throws IOException {
        transactionHistory.add(transaction);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TRANSACTIONS_FILE))) {
            oos.writeObject(transactionHistory);
            System.out.println("Transaction data saved.");
        } catch (IOException e) {
            System.out.println("Failed to save transaction data: " + e.getMessage());
        }
    }

    private void loadTransactionHistory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TRANSACTIONS_FILE))) {
            transactionHistory = (List<Transaction>) ois.readObject();
            System.out.println("Transaction history loaded.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing transaction data found.");
        }
    }
}
