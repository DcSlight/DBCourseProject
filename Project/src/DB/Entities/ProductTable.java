package DB.Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Components.Customer;
import DB.BasicTable;
import Products.Product;
import Products.ProductFactory;
import eNums.eProduct;

public class ProductTable extends BasicTable<String,Object> {
	
	private String serial = "serial";
	private String productName = "product_name";
	private String type = "type";
	private String costPrice = "cost_price";
	private String sellingPrice = "selling_price";
	private String stock = "stock";
	private String weight = "weight";

    public ProductTable(Connection conn) {
        super(conn, "Product");
    }

    protected Product mapResultSetToEntity(ResultSet rs) throws SQLException {
    	Product product = ProductFactory.createProduct(eProduct.valueOf(rs.getString(type)), rs.getString(serial),
    			rs.getString(productName), rs.getDouble(costPrice), rs.getDouble(sellingPrice), rs.getInt(stock), rs.getDouble(weight));
        return product;
    }
    
    public Set<Product> getAllProducts() throws Exception{
    	ResultSet rs =findAll();
    	Set<Product> products = new HashSet<Product>();
    	while(rs.next())
    		products.add(mapResultSetToEntity(rs));
    	return products;
    }
    
    public Product findProductBySerial(String serial) throws Exception{
    	Product product = null;
		ResultSet rs =this.findBy(this.serial, serial);
		while(rs.next())
			product = mapResultSetToEntity(rs);
		return product;
    }
    
    public Set<Product> findAllProductByType(eProduct type) throws SQLException{
    	String sql ="SELECT * FROM Product WHERE type=?::product_type";
    	 PreparedStatement stmt = conn.prepareStatement(sql);
         stmt.setObject(1, type.toString());
         ResultSet rs = stmt.executeQuery(); // Return the ResultSet containing all rows
         Set<Product> products = new HashSet<>();
         while(rs.next())
        	 products.add(mapResultSetToEntity(rs));
         return products;
    }

    public void createProduct(Product product) throws Exception {
        // Create a map to hold column-value pairs
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(this.serial, product.getSerial());
        entityMap.put(this.productName, product.getProductName());
        entityMap.put(this.type + "::product_type ", product.getType().toString()); //enum
        entityMap.put(this.costPrice, product.getCostPrice());
        entityMap.put(this.sellingPrice, product.getSellingPrice());
        entityMap.put(this.stock, product.getStock());
        entityMap.put(this.weight, product.getWeight());
        // Call the generic create method
        this.create(entityMap);
    }
    
    public int updateProductStockBySerial(String productSerial , int stock) throws Exception {
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(this.stock, stock);
        return this.update(entityMap, this.serial, productSerial);
    }

    public int deleteProduct(String serial) throws Exception {
        return this.delete(this.serial, serial);
    }
}
