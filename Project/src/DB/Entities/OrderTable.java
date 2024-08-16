package DB.Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        super(conn, "orders");
    }

    protected Order mapResultSetToEntity(ResultSet rs) throws SQLException {
    	  eProduct pType = eProduct.valueOf(rs.getString("type"));
          Product product = ProductFactory.createProduct(pType, rs.getString(this.productSerial),
          		rs.getString("product_name"), rs.getDouble("cost_price"),
          		rs.getDouble("selling_price"),rs.getInt("stock"), rs.getDouble("weight"));
          Customer customer = new Customer(rs.getString("full_name"),rs.getString("phone_number"),
        		  rs.getString("address"),rs.getInt("country_id"));
          Order order = new Order(product,customer,rs.getInt(amount),rs.getString(orderID));
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
    
    public Set<Order> getAllOrdersByProdustSerial(String productSerial) throws SQLException{
    	String sql = "SELECT * FROM orders\r\n"
      			+ "INNER JOIN product ON product.serial = orders.product_serial\r\n"
      			+ "INNER JOIN customer ON customer.customer_id = orders.customer_id\r\n"
      			+ "WHERE product_serial = ?";
    	PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setObject(1, productSerial);
        ResultSet rs = stmt.executeQuery();
        Set<Order> orders = new HashSet<>();
        while(rs.next())
        	orders.add(mapResultSetToEntity(rs));
        return orders;
    }

    public void createOrder(Order order) throws Exception {
        // Create a map to hold column-value pairs
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(this.orderID, order.getSerial());
        entityMap.put(this.amount, order.getAmount());
        entityMap.put(this.productSerial, order.getProduct().getSerial());
        entityMap.put(this.customerId, order.getCustomer().getCountryID());

        // Call the generic create method
        this.create(entityMap);
    }

    public void updateOrderById(String orderID, Order order) throws Exception {
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(this.amount, order.getAmount());
        entityMap.put(this.productSerial, order.getProduct().getSerial());
        entityMap.put(this.customerId, order.getCustomer().getCountryID());
        this.update(entityMap, this.orderID,orderID);
    }
    
    public double getTotalProfitBySerial(String productID) throws Exception {
    	String sql = "select sum(profit) as total_profit from orders WHERE product_serial=?";
    	PreparedStatement stmt = conn.prepareStatement(sql);
    	stmt.setObject(1, productID);
        ResultSet rs = stmt.executeQuery(); // Return the ResultSet containing all rows
        while(rs.next())
        	return rs.getDouble("total_profit");
        throw new Exception("error getting total profit from the DB");
    }

    public int deleteOrder(String orderID) throws Exception {
        return this.delete(this.orderID, orderID);
    }
}
