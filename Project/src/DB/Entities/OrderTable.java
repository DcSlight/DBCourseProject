package DB.Entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import Components.Contact;
import Components.Customer;
import DB.BasicTable;
import Order.Order;
import Products.Product;
import System.SystemFacade;

public class OrderTable extends BasicTable<String,Object> {
	
	private String orderID = "order_id";
	private String amount = "amount";
	private String productSerial = "product_serial";
	private String customerId = "customer_id";
	private String profit = "profit";

    public OrderTable(Connection conn) {
        super(conn, "Contact");
    }

    protected Order mapResultSetToEntity(ResultSet rs) throws SQLException {
    	//Product product, Customer customer,int amount,String serial
    	SystemFacade systemFacade = SystemFacade.getInstance(); //singleton
    	Order order = new Order(rs.getString(orderID),rs.getInt(amount),
    			rs.getDouble(profit),rs.getString(productSerial),rs.getInt(customerId));
        return order;
    }
    
    public Contact findContactByFullName(String fullName) throws Exception{
    	Contact c = null;
		ResultSet rs =this.findBy(this.fullName, fullName);
		c = mapResultSetToEntity(rs);
		return c;
    }

    public void createContact(Contact contact) throws Exception {
        // Create a map to hold column-value pairs
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(fullName, contact.getName());
        entityMap.put(this.whatsApp, contact.getWhatsApp());

        // Call the generic create method
        this.create(entityMap);
    }

    public void updateContactPhoneNumber(String whatsApp, Contact contact) throws Exception {
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(fullName, contact.getName());
        entityMap.put(this.whatsApp, whatsApp);
        this.update(entityMap, fullName,contact.getName());
    }

    public void printAllContacts() throws Exception {
        ResultSet rs = this.findAll();
        while (rs.next()) {
            System.out.println(rs.getInt(this.ContactID) + "- " + rs.getString(this.fullName) + "- " + rs.getString(this.whatsApp));
        }
    }

    public int deleteContact(int id) throws Exception {
        return this.delete(this.ContactID, id);
    }
}
