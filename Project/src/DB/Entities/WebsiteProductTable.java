package DB.Entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import Components.Customer;
import Shipping.ShippingStatus;
import eNums.eStatus;

public class WebsiteProductTable {
	private String statusCode = "status_code";
	private String companyId = "company_id";
	private String orderID = "order_id";
	private String shippingStatus= "shipping_status_enum";

    public ShippingStatusTable(Connection conn) {
        super(conn, "shipping_status");
    }

    protected ShippingStatus mapResultSetToEntity(ResultSet rs) throws SQLException {
    	ShippingStatus st = new ShippingStatus(rs.getInt(statusCode), 
    			rs.getInt(companyId),rs.getString(orderID),
    			eStatus.valueOf(rs.getString(shippingStatus)));
    	return st;
    }

    public void createShippingStatus(ShippingStatus st) throws Exception {
        // Create a map to hold column-value pairs
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(companyId, st.getCompanyId());
        entityMap.put(orderID, st.getOrderID());
        entityMap.put(shippingStatus+"::shipping_status_enum ", st.getShippingStatus());
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
