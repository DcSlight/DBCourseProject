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
    private static Connection conn;

    //Singleton for Connection
    public static Connection getConnection() throws SQLException , Exception {
    	if(conn == null) {
			InputStream input;
			input = DatabaseConnection.class.getResourceAsStream("/DB/dbconfig.properties");
	        Properties prop = new Properties();
	        prop.load(input);
	        URL = prop.getProperty("db.url");
	        USER = prop.getProperty("db.user");
	        PASSWORD = prop.getProperty("db.password");
	        conn = DriverManager.getConnection(URL, USER, PASSWORD);
	        return conn;
    	}
    	return conn;
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
