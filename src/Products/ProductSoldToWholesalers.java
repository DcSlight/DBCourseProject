package Products;

import Components.Customer;
import Components.Order;
import Exception.StockException;

public class ProductSoldToWholesalers extends Product{

	public ProductSoldToWholesalers(String serial, String productName, double costPrice, double sellingPrice, int stock,double weight) {
		super(serial, productName, costPrice, sellingPrice, stock,weight);
	}

	@Override
	public void addOrder(Customer customer,int amount) throws StockException {
		decreaseStock(amount);
		Order order = new Order(this, customer,amount);
		orders.add(order);
	}

}
