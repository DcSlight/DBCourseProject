package DB;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;


    public static Connection getConnection() throws SQLException , Exception {
		InputStream input;
		input = DatabaseConnection.class.getResourceAsStream("/DB/dbconfig.properties");
        Properties prop = new Properties();
        prop.load(input);
        URL = prop.getProperty("db.url");
        USER = prop.getProperty("db.user");
        PASSWORD = prop.getProperty("db.password");
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
