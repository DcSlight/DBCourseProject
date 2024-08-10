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
        Customer customer = new Customer(rs.getString("full_name"),rs.getString("phone_number"));
        return customer;
    }
    
    public void findCustomerByFullName(String fullName) {
    	try {
			ResultSet rs =this.findBy("full_name", fullName);
			 while (rs.next()) {
				 System.out.println((rs.getInt("id") + "- " + rs.getString("full_name")+ "- "  + rs.getString("phone_number")));
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void createCustomer(Customer customer) throws Exception {
        // Create a map to hold column-value pairs
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put("full_name", customer.getCustomerName());
        entityMap.put("phone_number", customer.getMobile());

        // Call the generic create method
        this.create(entityMap);
    }

    @Override
    public void update(Map<String, Object> entity) throws Exception {
//        String sql = "UPDATE " + this.tableName + " SET full_name = ?, phone_number = ? WHERE id = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, entity.get("full_name").getFullName());
//            stmt.setString(2, entity.get("phone_number").getPhoneNumber());
//            stmt.setInt(3, entity.get("id").getId());
//            stmt.executeUpdate();
//        }
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
}
