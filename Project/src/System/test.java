package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Components.Customer;
import DB.CustomerTable;
import DB.DatabaseConnection;

public class test {

	public static void findBy(Connection conn) {
        try {
            CustomerTable customerTable = new CustomerTable(conn);
            customerTable.findCustomerByFullName("John Doe");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}
	
	public static void create(Connection conn) {
        try {
            CustomerTable customerTable = new CustomerTable(conn);
            Customer c = new Customer("idan" , "0506005050");
            customerTable.createCustomer(c);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}
	
	
	
	public static void main(String[] args) {
		Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            //findBy(conn);
            create(conn);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
            System.out.println("Close");
        }  
	}

}
