
import accounts.AccountType;
import bank.Bank;
import java.util.Scanner;

public class OnlineBankingSystem {
    public static void main(String[] args) {

        Bank bank = new Bank();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n======================= Online Banking System ======================");
                System.out.println("1. Register Customer");
                System.out.println("2. Create Account");
                System.out.println("3. Deposit");
                System.out.println("4. Withdraw");
                System.out.println("5. Transfer");
                System.out.println("6. View Balance");
                System.out.println("7. Exit");
                System.out.println("====================================================================");
                System.out.print("Select an option: ");
                int option = scanner.nextInt();

                try {
                    switch (option) {

                        case 1:
                            System.out.print("Enter Customer ID (Format: CMB000123AS): ");
                            String customerId = scanner.next();
                            System.out.print("Enter Customer Name: ");
                            String name = scanner.next();
                                bank.registerCustomer(customerId, name);
                                break;
                                case 2:
                                    System.out.print("Enter Customer ID: ");
                                    customerId = scanner.next();
                                    System.out.print("Enter Account Holder Name: ");
                                    String accountHolderName = scanner.next();
                                    System.out.print("Select Account Type (1. Savings 2. Checking): ");
                                    int accountTypeOption = scanner.nextInt();
                                    AccountType accountType = accountTypeOption == 1 ? AccountType.SAVINGS : AccountType.CHECKING;
                                    bank.createAccount(customerId, accountHolderName, accountType);
                                    break;
                                case 3:
                                    System.out.print("Enter Accounts Number: ");
                                    String accountNumber = scanner.next();
                                    System.out.print("Enter Amount to Deposit: ");
                                    double depositAmount = scanner.nextDouble();
                                    bank.deposit(accountNumber, depositAmount);
                                    break;
                                case 4:
                                    System.out.print("Enter Account Number: ");
                                    accountNumber = scanner.next();
                                    System.out.print("Enter Amount to Withdraw: ");
                                    double withdrawAmount = scanner.nextDouble();
                                    bank.withdraw(accountNumber, withdrawAmount);
                                    break;
//                case 5:
//                    System.out.print("Enter From Account Number: ");
//                    String fromAccountNumber = scanner.next();
//                    System.out.print("Enter To Account Number: ");
//                    String toAccountNumber = scanner.next();
//                    System.out.print("Enter Amount to Transfer: ");
//                    double transferAmount = scanner.nextDouble();
//                    bank.transfer(fromAccountNumber, toAccountNumber, transferAmount);
//                    break;
                                case 6:
                                    System.out.print("Enter Account Number: ");
                                    accountNumber = scanner.next();
                                    bank.viewBalance(accountNumber);
                                    break;
                                case 7:
                                    System.out.println("Exiting the system...");
                                    scanner.close();
                                    return;
                                default:
                                    System.out.println("Invalid option. Please try again.");
                            }
                    }
                catch( Exception e){
                        System.out.println("Error: " + e.getMessage());
                    }
                }
        }
}