package DB.Entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import Components.Customer;
import DB.BasicTable;

public class CustomerTable extends BasicTable<String,Object> {
	
	private String CustomerID = "customer_id";
	private String fullName = "full_name";
	private String phoneNumber = "phone_number";

    public CustomerTable(Connection conn) {
        super(conn, "Customer");
    }

    protected Customer mapResultSetToEntity(ResultSet rs) throws SQLException {
        Customer customer = new Customer(rs.getString(fullName),rs.getString(phoneNumber));
        return customer;
    }
    
    public Customer findCustomerByFullName(String fullName) throws Exception{
    	Customer c;
		ResultSet rs =this.findBy(this.fullName, fullName);
		c = mapResultSetToEntity(rs);
		return c;
    }

    public void createCustomer(Customer customer) throws Exception {
        // Create a map to hold column-value pairs
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(fullName, customer.getCustomerName());
        entityMap.put(phoneNumber, customer.getMobile());

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
            System.out.println(rs.getInt(this.CustomerID) + "- " + rs.getString(this.fullName) + "- " + rs.getString(this.phoneNumber));
        }
    }

    public int deleteCustomer(int id) throws Exception {
        return this.delete(this.CustomerID, id);
    }
}
