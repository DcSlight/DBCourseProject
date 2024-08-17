package DB.Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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
        		,rs.getString(address), rs.getInt(countryID),rs.getInt(customerID));
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
    
    public int getCustomerID(String name, String mobile) throws Exception {
    	String sql ="select * from customer where full_name=? AND phone_number=?";
    	PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setObject(1, name);
        stmt.setObject(2,mobile);
        ResultSet rs = stmt.executeQuery();
        while(rs.next())
        	return mapResultSetToEntity(rs).getCustomerID();
        throw new Exception("Can not find customer");
    }

    public void createCustomer(String name,String mobile,String address,int countryID) throws Exception {
        // Create a map to hold column-value pairs
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(fullName, name);
        entityMap.put(phoneNumber, mobile);
        entityMap.put(this.address, address);
        CountryTable ct = new CountryTable(DatabaseConnection.getConnection());
        ct.findCountryByID(countryID);// if not found throw an error
        entityMap.put(this.countryID, countryID);
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
