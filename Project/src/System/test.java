package System;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import Components.Contact;
import Components.Customer;
import Components.Contact;
import DB.Entities.*;
import Order.Order;
import Order.WebsiteOrder;
import Products.Product;
import Products.ProductSoldThroughWebsite;
import Products.ProductSoldToWholesalers;
import Shipping.CompanyFactory;
import Shipping.FedEx;
import Shipping.ShippingCompany;
import Shipping.ShippingFactory;
import Shipping.Track;
import eNums.eShipMethod;
import eNums.eStatus;
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
            WebsiteOrderTracksView w = new WebsiteOrderTracksView(conn);
            CountryTable ct = new CountryTable(conn);
            System.out.println(ct.getRoute(7));
            //Product product, Customer customer, 
			//int amount,
			//String serial,
            //double profit,eStatus status
            Product p1 = new ProductSoldThroughWebsite("AAB12", "Iphone 15 protector", 7.5,87.58 , 400, 0.25);
            Customer c1 = new Customer("Avi","0506007070", "Jerusalem" , 1);
            WebsiteOrder o = new WebsiteOrder(p1,c1,4,"ORD035",50,eStatus.eOnTheWay,1);
            o.createWebsiteOrder();
//            Track t1 = new Track(eShipMethod.eShip, 1, "2024-08-15 10:00:00", 2,
//            		"2024-08-20 18:00:00");
//            Track t2 = new Track(eShipMethod.ePlane, 2, "2024-08-21 06:00:00", 3,
//            		"2024-08-22 12:00:00");
//            Set<Track> t = new HashSet<>();
//            t.add(t1);
//            t.add(t2);
//            w.insertOrderTransaction(1, "ORD035", t);
            //System.out.println(w.findAllTracksByOrderID("ORD035"));
            //w.updateTrackToArrive(6);
//            System.out.println(w.getAverageVATRate("ORD035"));
//            w.deleteOrderWebsite("ORD035");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
            System.out.println("Close");
        }  
	}

}
