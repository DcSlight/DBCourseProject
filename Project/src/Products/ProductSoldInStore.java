package Products;
import Exception.StockException;
import Order.Order;
import eNums.eProduct;

public class ProductSoldInStore extends Product{
	
	final static int DOLLAR_TO_SHEKEL=4;
	
	
	public ProductSoldInStore(String serial, String productName, double costPrice, double sellingPrice, int stock,double weight) {
		super(eProduct.eProductStore,serial, productName, costPrice*DOLLAR_TO_SHEKEL, sellingPrice*DOLLAR_TO_SHEKEL, stock,weight);
	}

	@Override
	public void addOrder(Order order) throws StockException {
		decreaseStock(order.getAmount());
		orders.add(order);		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer st = new StringBuffer();
		st.append("Product type: " + this.getClass().getSimpleName() + "\n");
		st.append("Total Profit: " + (float)getTotalProfit() + "₪\n");
		st.append(super.toString());
		return st.toString();
	}




	
	


}
