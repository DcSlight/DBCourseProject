package Order;

import Exception.StockException;
import Interfaces.IUndoCommand;
import Products.Product;
import Utils.FormatsUtils;

public class OrderUpdateCommand implements IUndoCommand{
	private Product product;
	private Order order;
	private int stock;
	private int previousStock;
	
	public OrderUpdateCommand(Product product,Order order) {
		this.product=product;
		this.order=order;
		this.stock = product.getStock();
	}
	
	@Override
	public Product getProduct() {
		return this.product;
	}

	@Override
	public void execute() throws StockException {
		previousStock = stock;
		product.addOrder(order);
	}

	@Override
	public void undo() {
		product.removeOrder(order);
		product.setStock(previousStock);
		//Note, according to 4.6 we need to print a message to the user that the transaction happen.
		System.out.println(FormatsUtils.ANSI_CYAN_BRIGHT+"\nOrder has been canceled to customer: " + order.getCustomer().getCustomerName());
		System.out.println("Due to warehouse issue."+FormatsUtils.ANSI_RESET);
	}
}
