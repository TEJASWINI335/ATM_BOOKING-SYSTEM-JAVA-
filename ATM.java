import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class Main {
private static final int MAX_HISTORY = 5;
private static final int MAX_ATTEMPTS = 3;
private static final double INTEREST_RATE = 0.04; // 4% annual interest rate
private static Map<String, UserAccount> accounts = new HashMap<>();
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
accounts.put("1001", new UserAccount("1001", 1234, 10000));
accounts.put("1002", new UserAccount("1002", 5678, 15000));
while (true) {
System.out.print("Enter your account number: ");
String accountNumber = sc.nextLine();
if (accountNumber.isEmpty()) {
System.out.println("Goodbye!");
break;
}
UserAccount user = accounts.get(accountNumber);
if (user != null) {
int attempts = 0;
boolean loggedIn = false; 
while (!loggedIn) {
System.out.print("Enter your 4-digit PIN: ");
int enteredPin = getValidInteger(sc);
if (enteredPin == user.getPin()) {
attempts = 0;
loggedIn = true; // Successfully logged in
while (loggedIn) {
int choice = displayMenu(sc);
switch (choice) {
case 1:
checkBalance(user);
break;
case 2:
user.setBalance(depositMoney(user, sc));
break;
case 3:
user.setBalance(withdrawMoney(user, sc));
break;
case 4:
viewMiniStatement(user);
break;
case 5:
user.setPin(changePin(user, sc));
break;
case 6: // Logout
if (confirmAction(sc, "logout")) {
System.out.println("Logged out.");
loggedIn = false; 
}
break;
case 7: // Show Interest
showInterest(user);
break;
case 8: // Exit
if (confirmAction(sc, "exit")) {
System.out.println("Thank you for using the ATM. Goodbye!");
sc.close();
return;
}
break;
default:
System.out.println("Invalid option. Try again.");
 }}
} else {
attempts++;
if (attempts >= MAX_ATTEMPTS) {
System.out.println("Too many incorrect attempts. Account locked.");
return; }
System.out.println("Incorrect PIN. Attempts left: " + (MAX_ATTEMPTS - attempts));
}}
} else {
System.out.println("Invalid account number. Please try again.");
}}}
private static int getValidInteger(Scanner sc) {
while (!sc.hasNextInt()) {
System.out.println("Invalid input. Please enter a number.");
sc.next(); }
return sc.nextInt();}
private static int displayMenu(Scanner sc) {
System.out.println("\n==== ATM MENU ====");
System.out.println("1. Check Balance");
System.out.println("2. Deposit Money");
System.out.println("3. Withdraw Money");
System.out.println("4. View Mini Statement");
System.out.println("5. Change PIN");
System.out.println("6. Logout");
System.out.println("7. Show Interest");
System.out.println("8. Exit");
System.out.print("\nChoose an option: ");
return getValidInteger(sc);
}
private static void checkBalance(UserAccount user) {
System.out.println("Balance: Rs. " + user.getBalance());
}
private static int depositMoney(UserAccount user, Scanner sc) {
System.out.print("Enter deposit amount: ");
int deposit = getValidInteger(sc);
if (deposit > 0) {
user.addHistory("Deposited Rs. " + deposit);
System.out.println("Rs. " + deposit + " deposited. New balance: Rs. " + (user.getBalance() + deposit));
return user.getBalance() + deposit;
} else {
System.out.println("Invalid amount.");
return user.getBalance();
} }
private static int withdrawMoney(UserAccount user, Scanner sc) {
System.out.print("Enter withdrawal amount: ");
int withdraw = getValidInteger(sc);
if (withdraw > 0 && withdraw <= user.getBalance()) {
user.addHistory("Withdrew Rs. " + withdraw);
System.out.println("Rs. " + withdraw + " withdrawn. New balance: Rs. " + (user.getBalance() - withdraw));
return user.getBalance() - withdraw;
} else {
System.out.println("Insufficient balance or invalid amount.");
return user.getBalance();
}}
private static void viewMiniStatement(UserAccount user) {
System.out.println("=== Mini Statement ===");
if (user.getHistory().isEmpty()) {
System.out.println("No transactions available.");
} else {
int count = 0;
for (String transaction : user.getHistory()) {
System.out.println(transaction);
count++;
if (count == MAX_HISTORY) break;
}}}
private static int changePin(UserAccount user, Scanner sc) {
System.out.print("Enter current PIN: ");
int enteredPin = getValidInteger(sc);
sc.nextLine();
if (enteredPin == user.getPin()) {
System.out.print("Enter new 4-digit PIN: ");
String newPinStr = sc.nextLine();
if (newPinStr.matches("\\d{4}")) {
int newPin = Integer.parseInt(newPinStr);
user.setPin(newPin);
System.out.println("PIN changed successfully.");
return newPin;
} else {
System.out.println("Invalid PIN format. Please enter a 4-digit number.");
return user.getPin();  // Keep the old PIN if the new one is invalid
}
} else {
System.out.println("Incorrect current PIN.");
return user.getPin();
}}
private static boolean confirmAction(Scanner sc, String action) {
System.out.print("Are you sure you want to " + action + "? (y/n): ");
String response = sc.next();
return response.equalsIgnoreCase("y");  }
private static void showInterest(UserAccount user) {
double interest = user.getBalance() * INTEREST_RATE / 12;
System.out.println("Monthly interest on your balance: Rs. " + interest);
}
static class UserAccount implements Serializable {
private String accountNumber;
private int pin;
private int balance;
private List<String> history;
public UserAccount(String accountNumber, int pin, int balance) {
this.accountNumber = accountNumber;
this.pin = pin;
this.balance = balance;
this.history = new ArrayList<>();  }
public String getAccountNumber() { return accountNumber; }
public int getPin() { return pin; }
public int getBalance() { return balance; }
public List<String> getHistory() { return history; }
public void setPin(int pin) { this.pin = pin; }
public void setBalance(int balance) { this.balance = balance; }
public void addHistory(String transaction) {
if (history.size() == MAX_HISTORY) history.remove(0);
String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
history.add(timestamp + " - " + transaction);
}
}
}



