package DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import Components.Customer;

public class CustomerTable extends BasicTable<String,Object> {
	
	private String fullName = "full_name";
	private String phoneNumber = "phone_number";

    public CustomerTable(Connection conn) {
        super(conn, "Customer");
    }

    protected Customer mapResultSetToEntity(ResultSet rs) throws SQLException {
        Customer customer = new Customer(rs.getString(fullName),rs.getString(phoneNumber));
        return customer;
    }
    
    public void findCustomerByFullName(String fullName) {
    	try {
			ResultSet rs =this.findBy(fullName, fullName);
			 while (rs.next()) {
				 System.out.println((rs.getInt("id") + "- " + rs.getString(fullName)+ "- "  + rs.getString(phoneNumber)));
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
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

    @Override
    public ResultSet findAll() throws Exception {
//        String sql = "SELECT * FROM " + this.tableName;
//        PreparedStatement stmt = conn.prepareStatement(sql);
//        ResultSet rs = stmt.executeQuery();
//
//        List<Customer> customers = new ArrayList<>();
//        while (rs.next()) {
//            customers.add(mapResultSetToEntity(rs));
//        }
//        return customers;
    	return null;
    }

    @Override
    public void delete() throws Exception {
        // Implement deletion logic based on your requirements
    }

	@Override
	public void update(Map<String, Object> entity) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
