package bank;

import accounts.SavingsAccount;
import accounts.CheckingAccount;
import accounts.AccountType;
import accounts.BankAccount;
import customers.Customer;
import exception.*;
import transactions.*;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bank {
    private Map<String, Customer> customers = new HashMap<>();
    private List<Transaction> transactions = new ArrayList<>();
    private static final String TRANSACTIONS_FILE = "transactions.txt";

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
        System.out.println("Account created successfully: " + account);
    }

    public void deposit(String accountNumber, double amount) throws Exception {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }
        BankAccount account = findAccount(accountNumber);
        if (account != null) {
            Transaction deposit = new DepositTransaction(account, amount);
            deposit.perform();
            transactions.add(deposit);
            saveTransaction(deposit);
        } else {
            throw new InvalidAccountNumberException("Account not found.");
        }
    }

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
            transactions.add(withdrawal);
            saveTransaction(withdrawal);
        } else {
            throw new InvalidAccountNumberException("Account not found.");
        }
    }

    public void viewBalance(String accountNumber) throws Exception {
        BankAccount account = findAccount(accountNumber);
        if (account == null) {
            throw new InvalidAccountNumberException("Account not found.");
        }
        System.out.println("Balance: $" + account.getBalance());
    }

    public void registerCustomer(String customerId, String name) throws IllegalArgumentException {
        if (validateCustomerId(customerId)) {
            customers.put(customerId, new Customer(customerId, name));
            System.out.println("Customer registered successfully.");
        } else {
            throw new IllegalArgumentException("Invalid customer ID format.");
        }
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

    private String generateAccountNumber() {
        String base = "52341";
        int randomNumber = 100 + new Random().nextInt(900);
        return base + randomNumber + "00012";  // Ensure last 5 digits are always 0012
    }

    private boolean validateCustomerId(String customerId) {
        String regex = "CMB\\d{6}[A-Z]{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(customerId);
        return matcher.matches();
    }

    private void saveTransaction(Transaction transaction) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TRANSACTIONS_FILE, true))) {
            oos.writeObject(transaction);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
