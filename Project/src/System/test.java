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
	
	public static Customer create(Connection conn,String name , String mobile) {
        try {
            CustomerTable customerTable = new CustomerTable(conn);
            Customer c = new Customer(name , mobile);
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
	
	public static void findAll(Connection conn) {
        try {
            CustomerTable customerTable = new CustomerTable(conn);
            customerTable.printAllCustomers();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}
	
	public static void deleteCustomer(Connection conn) {
        try {
            CustomerTable customerTable = new CustomerTable(conn);
            customerTable.deleteCustomer(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}
	
	
	
	public static void main(String[] args) {
		Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            System.out.println("----------------------");
            findBy(conn);
            System.out.println("----------------------");
            findAll(conn);
            System.out.println("----------------------");
            Customer c = create(conn,"sapir" , "050");
            System.out.println("----------------------");
            update(conn,c);
            System.out.println("----------------------");
            findAll(conn);
            System.out.println("----------------------");
        //    deleteCustomer(conn);
            System.out.println("----------------------");
            findAll(conn);
            System.out.println("----------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
            System.out.println("Close");
        }  
	}

}
