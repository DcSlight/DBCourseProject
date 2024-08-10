package DB;
//DatabaseConnection.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
 private static final String URL = "jdbc:postgresql://localhost:5432/your_database_name";
 private static final String USER = "your_username";
 private static final String PASSWORD = "your_password";

 public static Connection getConnection() throws SQLException {
     return DriverManager.getConnection(URL, USER, PASSWORD);
 }

 public static void closeConnection(Connection connection) {
     if (connection != null) {
         try {
             connection.close();
         } catch (SQLException e) {
             System.out.println("Failed to close connection: " + e.getMessage());
         }
     }
 }
}
