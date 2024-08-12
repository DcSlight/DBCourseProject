package DB.Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import Components.Contact;
import Components.Customer;
import DB.BasicTable;
import Order.Order;
import Products.Product;
import Products.ProductFactory;
import System.SystemFacade;
import eNums.eProduct;

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
    	  eProduct pType = eProduct.valueOf(rs.getString("type"));
          Product product = ProductFactory.createProduct(pType, rs.getString(this.productSerial),
          		rs.getString("product_name"), rs.getDouble("cost_price"),
          		rs.getDouble("selling_price"),rs.getInt("stock"), rs.getDouble("weight"));
          Customer customer = new Customer(rs.getString("full_name"),rs.getString("phone_number"),
        		  rs.getString("address"),rs.getInt("country_id"));
          Order order = new Order(product,customer,rs.getInt(this.amount),rs.getString(this.orderID),
        		  rs.getDouble(this.profit));
          return order;
    }
    
    public Order findOrderBySerial(String serialOrder) throws Exception{
    	 String sql = "SELECT * FROM orders\r\n"
     			+ "INNER JOIN product ON product.serial = orders.product_serial\r\n"
     			+ "INNER JOIN customer ON customer.customer_id = orders.customer_id\r\n"
     			+ "WHERE order_id = ?";
    	 PreparedStatement stmt = conn.prepareStatement(sql);
         stmt.setObject(1, serialOrder);
         ResultSet rs = stmt.executeQuery(); // Return the ResultSet containing all rows
         while(rs.next())
        	 return mapResultSetToEntity(rs);
         return null;
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
