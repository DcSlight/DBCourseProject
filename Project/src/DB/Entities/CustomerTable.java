package DB.Entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import Components.Country;
import Components.Customer;
import DB.BasicTable;
import DB.DatabaseConnection;

public class CustomerTable extends BasicTable<String,Object> {
	
	private String customerID = "customer_id";
	private String fullName = "full_name";
	private String phoneNumber = "phone_number";
	private String address = "address";
	private String countryID = "country_id";

    public CustomerTable(Connection conn) {
        super(conn, "Customer");
    }

    protected Customer mapResultSetToEntity(ResultSet rs) throws SQLException {
        Customer customer = new Customer(rs.getString(fullName),rs.getString(phoneNumber)
        		,rs.getString(address), rs.getInt(countryID));
        return customer;
    }
    
    public Customer findCustomerByFullName(String fullName) throws Exception{
    	Customer c;
		ResultSet rs =this.findBy(this.fullName, fullName);
		c = mapResultSetToEntity(rs);
		return c;
    }
    
    public Customer findCustomerByID(Integer customerID) throws Exception{
    	Customer c;
		ResultSet rs =this.findBy(this.customerID, customerID);
		if (rs.next()) {
	        // If a row is found, map the result set to a Country entity
	        c = mapResultSetToEntity(rs);
	    } else {
	        // Handle the case where no country is found
	        throw new Exception("Customer with ID " + customerID + " does not exist.");
	    }
		return c;
    }

    public void createCustomer(Customer customer) throws Exception {
        // Create a map to hold column-value pairs
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(fullName, customer.getCustomerName());
        entityMap.put(phoneNumber, customer.getMobile());
        entityMap.put(this.address, customer.getAddress());
        CountryTable ct = new CountryTable(DatabaseConnection.getConnection());
        ct.findCountryByID(customer.getCountryID());// if not found throw an error
        entityMap.put(this.countryID, customer.getCountryID());
        // Call the generic create method
        this.create(entityMap);
    }

    public void updateCustomerPhoneNumber(String mobile, Customer customer) throws Exception {
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(fullName, customer.getCustomerName());
        entityMap.put(phoneNumber, mobile);
        this.update(entityMap, fullName, customer.getCustomerName());
    }

    public void printAllCustomers() throws Exception {
        ResultSet rs = this.findAll();
        while (rs.next()) {
            System.out.println(rs.getInt(this.customerID) + "- " + rs.getString(this.fullName) + "- " + rs.getString(this.phoneNumber));
        }
    }

    public int deleteCustomer(int id) throws Exception {
        return this.delete(this.customerID, id);
    }
}
