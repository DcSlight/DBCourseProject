package Products;

import Components.Customer;
import Components.Order;
import Exception.StockException;
import Interfaces.IInvoice;
import Invoice.InvoiceAdapterFactory;
import eNums.eInvoice;

public class ProductSoldInStore extends Product{
	
	final int DOLLAR_TO_SHEKEL=4;
	
	
	public ProductSoldInStore(String serial, String productName, double costPrice, double sellingPrice, int stock,double weight) {
		super(serial, productName, costPrice, sellingPrice, stock,weight);
	}


	@Override
	public void addOrder(Customer customer, int amount) throws StockException {
		//decreaseStock(amount);
		Order order = new Order(this, customer,amount);
		orders.add(order);		
	}




	
	


}
