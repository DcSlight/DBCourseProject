package Products;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import DB.DatabaseConnection;
import DB.Entities.OrderTable;
import DB.Entities.ProductTable;
import Exception.StockException;
import Order.Order;
import Utils.FormatsUtils;
import eNums.eProduct;

public abstract class Product implements Comparable<Product> {
	protected String serial;
	protected String productName;
	protected eProduct type;
	protected double costPrice;
	protected double sellingPrice;
	protected int stock;
	protected double weight;
	protected Set<Order> orders;
	protected ProductMemento memento;

	public Product(eProduct type,String serial, String productName, double costPrice, double sellingPrice, int stock, double weight) {
		this.serial = serial;
		this.productName = productName;
		this.costPrice = costPrice;
		this.sellingPrice = sellingPrice;
		this.stock = stock;
		this.orders = new LinkedHashSet<>();
		this.weight = weight;
		this.type = type;
	}
	
	public eProduct getType() {
		return this.type;
	}
	
	@Override
    public int compareTo(Product other) {
        return this.serial.compareTo(other.serial);
    }
	
	public boolean isSerialOrderExist(String serial) {
		for(Order order: orders) {
			if(order.getSerial().equals(serial))
				return true;
		}
		return false;
	}
	
	public String getDetails() {
		StringBuffer st = new StringBuffer();
		st.append("Product serial: " + serial + "\n");
		st.append("Product Name: " + productName + "\n");
		st.append("Product Weight: " + weight + "kg\n");
		st.append("Current Stock: " + stock + "\n");
		return st.toString();
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void removeOrder(Order order) {
		this.orders.remove(order);
	}

	public abstract void addOrder(Order order) throws StockException;

	public String getAllOrders() throws SQLException, Exception {
		int num = 1;
		StringBuffer st = new StringBuffer();
		OrderTable ot = new OrderTable(DatabaseConnection.getConnection());
		Set<Order> orders = ot.getAllOrdersByProdustSerial(this.serial);
		if (orders.isEmpty())
			return "There are no orders!";
		for (Order o : orders) {
			st.append(FormatsUtils.ANSI_YELLOW + "\nOrder " + num + ":\n" + FormatsUtils.ANSI_RESET);
			st.append(o.toString() + "\n");
			num++;
		}
		return st.toString();
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void decreaseStock(int amount) throws StockException {
		if (amount > stock)
			throw new StockException();
		this.stock -= amount;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrder(Set<Order> orders) {
		this.orders = orders;
	}

	public double getTotalProfit() throws Exception {
		OrderTable ot = new OrderTable(DatabaseConnection.getConnection());
		return ot.getTotalProfitBySerial(this.getSerial());
	}

	public void createMemento() {
		memento= new ProductMemento(serial, productName, costPrice, sellingPrice, stock, weight, orders);
	}

	public void setMemento() {
		this.serial = memento.serial;
		this.productName = memento.productName;
		this.costPrice=memento.costPrice;
		this.sellingPrice=memento.sellingPrice;
		this.stock=memento.stock;
		this.weight=memento.weight;
		this.orders=memento.orders;
	}

	public static class ProductMemento {
		private String serial;
		private String productName;
		private double costPrice;
		private double sellingPrice;
		private int stock;
		private double weight;
		private Set<Order> orders;

		private ProductMemento(String serial, String productName, double costPrice, double sellingPrice, int stock,
				double weight, Set<Order> orders) {
			this.serial = serial;
			this.productName = productName;
			this.costPrice = costPrice;
			this.sellingPrice = sellingPrice;
			this.stock = stock;
			this.weight = weight;
			this.orders = new LinkedHashSet<>(orders);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(serial);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (!serial.equals(other.getSerial()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer st = new StringBuffer();
		st.append("Product serial: " + serial + "\n");
		st.append("Product Name: " + productName + "\n");
		st.append("Product Weight: " + weight + "kg\n");
		st.append("Current Stock: " + stock + "\n");
		st.append(FormatsUtils.ANSI_YELLOW_UNDERLINED + "\nAll orders:\n" + FormatsUtils.ANSI_RESET);
		try {
			st.append(getAllOrders() + "\n\n");
		}catch(Exception e) {
			st.append("Error getting Orders: "+ e.getMessage() + "\n\n");
		}
		return st.toString();
	}

}
