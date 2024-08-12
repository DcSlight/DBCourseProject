package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Components.Contact;
import Components.Customer;
import Components.Contact;
import DB.Entities.*;
import Order.Order;
import Products.Product;
import Products.ProductSoldThroughWebsite;
import Products.ProductSoldToWholesalers;
import Shipping.CompanyFactory;
import Shipping.FedEx;
import Shipping.ShippingCompany;
import Shipping.ShippingFactory;
import DB.DatabaseConnection;

public class test {

	public static void findBy(Connection conn) {
        try {
            ContactTable contactTable = new ContactTable(conn);
            contactTable.findContactByFullName("John Doe");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}
	
	public static Contact create(Connection conn,String name , String mobile) {
        try {
            ContactTable contactTable = new ContactTable(conn);
            Contact c = new Contact(name , mobile);
            contactTable.createContact(c);
            return c;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
		return null;
	}
	
	public static void update(Connection conn,Contact c) {
        try {
            ContactTable contactTable = new ContactTable(conn);
            contactTable.updateContactPhoneNumber("050-1114545", c);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}
	
	public static void findAll(Connection conn) {
        try {
            ContactTable contactTable = new ContactTable(conn);
            contactTable.printAllContacts();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}
	
	public static void deleteContact(Connection conn) {
        try {
            ContactTable contactTable = new ContactTable(conn);
            contactTable.deleteContact(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
	}
	
	public static void main(String[] args) {
		Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            ShippingContactTable t = new ShippingContactTable(conn);
            ShippingCompany f = CompanyFactory.createCompany(1, "FedEx");
            t.findAllContactOfCompany(f);
            System.out.println(f.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
            System.out.println("Close");
        }  
	}

}
