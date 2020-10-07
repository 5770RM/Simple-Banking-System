package banking;

import org.sqlite.SQLiteDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    protected static SQLiteDataSource dataSource = null;
    protected static Connection connection = null;
    protected static Statement statement = null;
    protected static ResultSet resultSet = null;
    private static int loggedId;

    public static void connectToDb(String[] args) {
        dataSource = new SQLiteDataSource();
        String url = "jdbc:sqlite:" + args[1];
        dataSource.setUrl(url);
        try {
            connection = dataSource.getConnection();
            try  {
                statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card ("
                        + "id INTEGER PRIMARY KEY,"
                        + "number TEXT NOT NULL,"
                        + "pin TEXT NOT NULL,"
                        + "balance INTEGER DEFAULT 0);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAccount(StringBuilder card, StringBuilder pin) {
        try {
            statement.executeUpdate("INSERT INTO card(number,pin) VALUES (" + card + ", "  + pin + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addIncome(Long income) {
        try {
         statement.executeUpdate("UPDATE card SET balance = " + income + " WHERE id = " + loggedId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean isPresent(String cardNumber, String pinCode) {
        try  {
            resultSet = statement.executeQuery("SELECT * FROM card");
            while (resultSet.next()) {
                String number = resultSet.getString("number");
                String pin = resultSet.getString("pin");

                if (cardNumber.equals(number) && pinCode.equals(pin)) {
                    loggedId = resultSet.getInt("id");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isPresent(String cardNumber) {
        try  {
            resultSet = statement.executeQuery("SELECT * FROM card");
            while (resultSet.next()) {
                String number = resultSet.getString("number");

                if (cardNumber.equals(number)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static long getBalance() {
        try {
            resultSet = statement.executeQuery("SELECT balance FROM card WHERE id = " + loggedId);
            if (resultSet.next()) {
                return resultSet.getInt("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean canTransfer(String cardNumber) {
        String currentCardNumber = "";
        int sumOfCardDigits = Main.checkForValid(cardNumber);
        try {
            resultSet = statement.executeQuery("SELECT number FROM card where id = " + loggedId);
            if (resultSet.next()) {
                currentCardNumber = resultSet.getString("number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (currentCardNumber.equals(cardNumber)) {
            System.out.println("You can't transfer money to the same account!");
        } else if (sumOfCardDigits % 10 != 0) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
        } else if (!isPresent(cardNumber)) {
            System.out.println("Such a card does not exist.");
        } else {
            return true;
        }
        return false;
    }

    public static void doTransfer(String receiverCard, long transferMoney) {
        String currentCardNumber = "";
        try {
            resultSet = statement.executeQuery("SELECT number FROM card WHERE id = " + loggedId);
            if (resultSet.next()) {
                currentCardNumber = resultSet.getString("number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.executeUpdate("UPDATE card SET balance = balance +" + transferMoney + " WHERE number = " + receiverCard);
            statement.executeUpdate("UPDATE card SET balance = balance -" + transferMoney + " WHERE number = " + currentCardNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deleteAccount() {
        try {
            statement.executeUpdate("DELETE FROM card WHERE id = " + loggedId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
