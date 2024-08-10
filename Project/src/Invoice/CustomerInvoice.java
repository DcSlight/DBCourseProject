package Invoice;

import Components.Customer;
import Interfaces.IInvoice;

public class CustomerInvoice implements IInvoice {
	final double VAT = 0.17;
	private Customer customer;
	private String productName;
	private double sellingPrice;
	private int amount;
	private char currency;
	
	public CustomerInvoice(Customer customer,String productName, double sellingPrice,int amount,char currency) {
		this.customer = customer;
		this.productName = productName;
		this.sellingPrice = sellingPrice;
		this.amount = amount;
		this.currency= currency;
	}

	@Override
	public String showInvoice() {
		StringBuffer st = new StringBuffer();
		st.append("\n------Customer Invoice------\n");
		st.append("Customer details:\n");
		st.append(customer.toString());
		st.append("\nThe product name is: " + productName);
		st.append("\nThe price is: "+String.format("%.2f", sellingPrice * amount) +currency+ " \nVAT is: "+  String.format("%.2f", sellingPrice*VAT)+currency+"\n");
		return st.toString();
	}
	
}
