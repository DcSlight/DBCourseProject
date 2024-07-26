package Products;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import Components.Customer;
import Components.Order;
import Exception.StockException;

public abstract class Product {
	protected String serial;
	protected String productName;
	protected double costPrice;
	protected double sellingPrice;
	protected int stock;
	protected double weight;
	protected Set<Order> orders;

	
	public Product(String serial, String productName, double costPrice, double sellingPrice, 
			int stock, double weight) {
		this.serial=serial;
		this.productName = productName;
		this.costPrice = costPrice;
		this.sellingPrice = sellingPrice;
		this.stock = stock;
		this.orders = new LinkedHashSet<>();
		this.weight = weight;
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public abstract void addOrder(Customer customer,int amount) throws StockException ;
	
	public String getAllOrders() {
		double totalProfit=0;
		StringBuffer st = new StringBuffer();
		for(Order o : orders) {
			totalProfit+=o.getProfit();
			st.append(o.toString());
		}
		st.append("\nTotal profit is: " + totalProfit);
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
		if(amount > stock)
			throw new StockException();
		this.stock -= amount;
	}


	public Set<Order> getOrders() {
		return orders;
	}


	@Override
	public int hashCode() {
		return Objects.hash(costPrice, orders, productName, sellingPrice, serial, stock);
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
		return (costPrice == other.costPrice)
				&& orders.equals(other.orders)
				&& productName.equals(other.productName)
				&& (sellingPrice ==other.sellingPrice)
				&& serial.equals(other.serial)
				&& stock == other.stock
				&& weight == other.weight;
	}

	@Override
	public String toString() {
		return "Product [serial=" + serial + ", productName=" + productName + ", costPrice=" + costPrice
				+ ", sellingPrice=" + sellingPrice + ", stock=" + stock + ", weight=" + weight + ", orders=" + orders
				+ "]";
	}

	
	
}
