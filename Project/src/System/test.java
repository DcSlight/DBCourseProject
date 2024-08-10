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
	
	public static Customer create(Connection conn) {
        try {
            CustomerTable customerTable = new CustomerTable(conn);
            Customer c = new Customer("idan" , "0506005050");
            customerTable.createCustomer(c);
            return c;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
		return null;
	}
	
	public static void update(Connection conn,Customer c) {
        try {
            CustomerTable customerTable = new CustomerTable(conn);
            customerTable.updateCustomerPhoneNumber("050-1114545", c);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}
	
	
	
	public static void main(String[] args) {
		Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            //findBy(conn);
            Customer c =create(conn);
            update(conn,c);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
            System.out.println("Close");
        }  
	}

}
