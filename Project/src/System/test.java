package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DB.CustomerTable;
import DB.DatabaseConnection;

public class test {

	public static void connecting() {
		 Connection conn = null;
	        try {
	            conn = DatabaseConnection.getConnection();
	            CustomerTable customerTable = new CustomerTable(conn);
	            customerTable.findCustomerByFullName("John Doe1");
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        } finally {
	            DatabaseConnection.closeConnection(conn);
	            System.out.println("Close");
	        }
	}
	
	public static void main(String[] args) {
		connecting();
	}

}
