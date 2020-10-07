package banking;

import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isLoggedIn;
    private static StringBuilder cardBuilder;
    private static StringBuilder pinBuilder;

    public static void main(String[] args) {
        Database.connectToDb(args);
        while (true) {
            printMainMenu();
            int numberChoice = Integer.parseInt(scanner.nextLine());

            switch (numberChoice) {
                case 0:
                    System.out.println("Bye!");
                    try {
                        Database.statement.close();
                        Database.connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                case 1:
                    generateAndAdd(); //generates Card+PIN and adds it to CardOwner fields
                    break;
                case 2:
                    attemptLogin();
                    while (isLoggedIn) {
                        printLoggedMenu();
                    }
                    break;
                default:
                    System.out.println("You entered a wrong number");
                    break;
            }
        }
    }

    private static void generateAndAdd() {
        Random random = new Random();
        cardBuilder = new StringBuilder("400000");
        pinBuilder = new StringBuilder();

        cardBuilder.append(random.nextInt(999999999 - 111111111 + 1) + 111111111);
        pinBuilder.append(random.nextInt(9999 - 1111 + 1) + 1111);


        int sum = checkForValid(cardBuilder.toString());
        if (sum % 10 == 0) {
            cardBuilder.append(0);
        } else {
            cardBuilder.append(10 - (sum % 10));
        }
        Database.addAccount(cardBuilder, pinBuilder);

        System.out.println("Your card has been created\n" +
                "Your card number:\n" + cardBuilder +
                "\nYour card PIN:\n" + pinBuilder);

    }

    protected static int checkForValid(String cardNumber) { // Luhn algorithm
        int sum = 0;

        for (int i = 0; i < cardNumber.length(); i++) {
            int num = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));

            if (i % 2 == 0) { //odd numbers bc counting starts with 1
                num = num * 2;
                num = num >= 10? num - 9 : num;
            }
            sum += num;
        }
        return sum;
    }


    private static void printMainMenu() {
        System.out.println("1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");
    }

    private static void attemptLogin() {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN");
        String pinCode = scanner.nextLine();

        isLoggedIn = Database.isPresent(cardNumber, pinCode);

        System.out.println(isLoggedIn ? "You have successfully logged in!" : "Wrong card number or PIN!");
    }

    private static void printLoggedMenu() {
        System.out.println("1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
        int numberChoice2 = Integer.parseInt(scanner.nextLine());
        switch (numberChoice2) {
            case 0:
                System.out.println("Bye!");
                    try {
                        Database.connection.close();
                        Database.statement.close();
                        Database.resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                System.exit(0);
            case 1:
                System.out.println("Balance: " + Database.getBalance());
                break;
            case 2:
                System.out.println("Enter income: ");
                long income = Long.parseLong(scanner.nextLine());
                Database.addIncome(income);
                System.out.println("Income was added");
                break;
            case 3:
                System.out.println("Transfer");
                System.out.println("Enter card number:");
                String cardNumber = scanner.nextLine();
                boolean canTransfer = Database.canTransfer(cardNumber);
                if (canTransfer) {
                    System.out.println("Enter how much money you want to transfer: ");
                    long transferMoney = Long.parseLong(scanner.nextLine());
                    if (transferMoney > Database.getBalance()) {
                        System.out.println("Not enough money!");
                        break;
                    } else {
                        Database.doTransfer(cardNumber, transferMoney);
                    }
                }
                break;
            case 4:
                Database.deleteAccount();
                isLoggedIn = false;
                break;
            case 5:
                System.out.println("You have successfully logged out!");
                isLoggedIn = false;
                break;
            default:
                System.out.println("You entered a wrong number");
                break;
        }
    }
}

